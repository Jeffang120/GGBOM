package com.idme.controller;

import com.idme.request.LoginRequest;
import com.idme.request.RegisterRequest;
import com.idme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理接口
 *
 * 提供用户注册和登录基本认证功能
 * 所有接口返回统一格式：Result<T>
 */
@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     *
     * @param request 注册请求体，包含用户名、密码等信息
     * @return Result<String> 注册结果，成功返回 "注册成功"，失败返回错误信息
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    /**
     * 用户登录接口
     *
     * @param request 登录请求体，包含用户名和密码
     * @return Result<String> 登录结果，成功返回 token 或登录凭证，失败返回错误信息
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }


}
