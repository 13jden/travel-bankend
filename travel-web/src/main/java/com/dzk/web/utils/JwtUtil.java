package com.dzk.web.utils;

import com.dzk.common.redis.RedisComponent;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000; // 24小时
    private static final String SECRET = "idouso-secret-key-32-bytes-long-long";  // 确保密钥长度至少为 32 字节
    
    // Redis前缀，用于存储无效token
    private static final String INVALID_TOKEN_KEY_PREFIX = "idouso:invalidated:";

    @Autowired
    private RedisComponent redisComponent;

    // 生成token
    public  String generateToken(String username) throws Exception {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRE_TIME);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issueTime(now)
                .expirationTime(expiration)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(new MACSigner(SECRET.getBytes()));

        return signedJWT.serialize();
    }

    // 验证token
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            MACVerifier verifier = new MACVerifier(SECRET.getBytes());
            
            // 检查token是否被加入黑名单
            String username = signedJWT.getJWTClaimsSet().getSubject();
            if (isTokenInvalidated(username)) {
                return false;
            }
            
            return signedJWT.verify(verifier);
        } catch (Exception e) {
            return false;
        }
    }

    // 从token中获取用户名
    public static String getUsernameFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public String getUserId(String token){
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 使指定用户的所有token失效
     * @param userId 用户ID
     */
    public void invalidateUserTokens(String userId) {
        // 将用户ID加入Redis黑名单，设置与token相同的过期时间
        redisComponent.saveNoTokenInfo(INVALID_TOKEN_KEY_PREFIX,userId);
    }
    
    /**
     * 检查用户token是否已被加入黑名单
     * @param userId 用户ID
     * @return 如果token已失效返回true，否则返回false
     */
    private boolean isTokenInvalidated(String userId) {
        if(redisComponent.getBlackUser(userId)!=null)
            return true;
        return false;
    }

    public void invalidateDeleteUserTokens(String userId) {
        // 将用户ID加入Redis黑名单，设置与token相同的过期时间
        redisComponent.deleteNoTokenInfo(INVALID_TOKEN_KEY_PREFIX,userId);
    }
}