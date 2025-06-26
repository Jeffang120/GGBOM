package com.idme.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

public class JwtUtil {
    // 确保密钥足够长（建议至少 256 bits，即 32 字符以上）
    private static final String SECRET_KEY = "veryStrongSecretKeyThatIsLongEnough";
    private static final long EXPIRATION = 864_000_000; // 10天

    public static String generateToken(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())) // 使用 SecretKey 替代字符串
                .compact();
    }

    public static String parseUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
