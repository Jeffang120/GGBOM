package com.idme.service;

import com.huawei.innovation.rdm.coresdk.basic.dto.PersistObjectIdModifierDTO;
import com.huawei.innovation.rdm.coresdk.basic.enums.ConditionType;
import com.huawei.innovation.rdm.minibom.delegator.BOMUsersDelegator;
import com.huawei.innovation.rdm.minibom.dto.entity.BOMUsersCreateDTO;
import com.idme.controller.Result;
import com.idme.request.RegisterRequest;
import com.idme.util.PasswordUtil;
import com.idme.util.LoginAttemptService;
import com.idme.util.JwtUtil;
import com.huawei.innovation.rdm.coresdk.basic.vo.QueryRequestVo;
import com.huawei.innovation.rdm.coresdk.basic.vo.RDMPageVO;
import com.huawei.innovation.rdm.minibom.dto.entity.BOMUsersViewDTO;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private BOMUsersDelegator bomUsersDelegator;

    public Result<String> register(RegisterRequest request) {
        if (request == null || request.getAccount() == null || request.getAccount().isEmpty()) {
            return Result.error("账号不能为空");
        }

        QueryRequestVo queryRequestVo = new QueryRequestVo();
        queryRequestVo.addCondition("account", ConditionType.EQUAL, request.getAccount());

        List<BOMUsersViewDTO> existingUsers = bomUsersDelegator.find(queryRequestVo, new RDMPageVO(1, 1));
        if (existingUsers != null && !existingUsers.isEmpty()) {
            return Result.error("账号已存在");
        }

        String password = request.getPassword();
        if (password == null || password.length() < 8 ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*[a-z].*") ||
                !password.matches(".*\\d.*")) {
            return Result.error("密码不符合要求：需同时包含大小写字母、数字且长度不少于8位");
        }

        try {
            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(password, salt);

            BOMUsersCreateDTO createDTO = new BOMUsersCreateDTO();
            createDTO.setAccount(request.getAccount());
            createDTO.setPaasword(hashedPassword);
            createDTO.setSalt(salt);

            bomUsersDelegator.create(createDTO);
            return Result.success("注册成功");

        } catch (Exception e) {
            return Result.error("注册失败：" + e.getMessage());
        }
    }
    public Result<String> login(com.idme.request.LoginRequest request) {
        String account = request.getAccount();
        String rawPassword = request.getPassword();
        try {
            // 0. 检查账号是否被锁定
            if (LoginAttemptService.isAccountLocked(account)) {
                return Result.error("账号已被锁定，请10分钟后重试");
            }
            QueryRequestVo queryRequestVo = new QueryRequestVo();
            queryRequestVo.addCondition("account", ConditionType.EQUAL, request.getAccount());

            List<BOMUsersViewDTO> users = bomUsersDelegator.find(queryRequestVo, new RDMPageVO(1, 1));
            if (users == null || users.isEmpty()) {
                LoginAttemptService.addFailedAttempt(account);
                int attemptsLeft = 6 - LoginAttemptService.getFailedAttempts(account);
                if (LoginAttemptService.isMaxAttemptsExceeded(account)) {
                    LoginAttemptService.lockAccount(account);
                    return Result.error("密码错误次数过多，账号已锁定，请10分钟后重试");
                }
                return Result.error("账号或密码错误，剩余尝试次数：" + attemptsLeft);
            }

            BOMUsersViewDTO user = users.get(0);
            String storedHash = user.getPaasword(); // 数据库中的加密密码
            String salt = user.getSalt(); // 数据库中的盐值

            // 对输入密码再次加密比对
            String inputHash = PasswordUtil.hashPassword(request.getPassword(), salt);

            if (!inputHash.equals(storedHash)) {
                LoginAttemptService.addFailedAttempt(account);
                int attemptsLeft = 6 - LoginAttemptService.getFailedAttempts(account);
                if (LoginAttemptService.isMaxAttemptsExceeded(account)) {
                    LoginAttemptService.lockAccount(account);
                    return Result.error("密码错误次数过多，账号已锁定，请10分钟后重试");
                }
                return Result.error("密码错误，剩余尝试次数：" + attemptsLeft);
            }

            // 生成 JWT
            String token = JwtUtil.generateToken(user.getAccount());
            return Result.success(token); // 返回 token 给前端

        } catch (JwtException e) {
            System.err.println("JWT 异常: " + e.getMessage());
            e.printStackTrace();
            return Result.error("身份令牌生成失败");
        } catch (IllegalArgumentException e) {
            System.err.println("Base64 解码失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("Base64 解码失败，请检查密码格式");
        } catch (Exception e) {
            System.err.println("未知异常: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            return Result.error("登录失败: " + e.getMessage());
        }
    }

}
