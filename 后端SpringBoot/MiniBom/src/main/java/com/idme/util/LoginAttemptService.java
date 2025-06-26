package com.idme.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class LoginAttemptService {

    // 存储账号 -> 失败次数
    private static final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();

    // 存储账号 -> 锁定结束时间戳
    private static final Map<String, Long> lockedAccounts = new ConcurrentHashMap<>();

    // 最大失败次数
    private static final int MAX_ATTEMPTS = 6;

    // 锁定时间（毫秒）
    private static final long LOCK_TIME = TimeUnit.MINUTES.toMillis(10);

    /**
     * 记录一次登录失败
     */
    public static void addFailedAttempt(String account) {
        if (isAccountLocked(account)) {
            return;
        }
        loginAttempts.put(account, getFailedAttempts(account) + 1);
    }

    /**
     * 获取失败次数
     */
    public static int getFailedAttempts(String account) {
        return loginAttempts.getOrDefault(account, 0);
    }

    /**
     * 成功登录时清除失败记录
     */
    public static void resetFailedAttempts(String account) {
        loginAttempts.remove(account);
    }

    /**
     * 判断账号是否被锁定
     */
    public static boolean isAccountLocked(String account) {
        Long lockEndTime = lockedAccounts.get(account);
        if (lockEndTime != null && System.currentTimeMillis() < lockEndTime) {
            return true;
        } else {
            // 锁定时间已过，清理记录
            lockedAccounts.remove(account);
            return false;
        }
    }

    /**
     * 锁定账号
     */
    public static void lockAccount(String account) {
        lockedAccounts.put(account, System.currentTimeMillis() + LOCK_TIME);
        loginAttempts.remove(account); // 清除失败次数
    }
    public static boolean isMaxAttemptsExceeded(String account) {
        return getFailedAttempts(account) >= MAX_ATTEMPTS;
    }

}
