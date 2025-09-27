package com.dzk.web.config;

import com.dzk.common.constants.Constants;
import com.dzk.web.api.user.User;
import com.dzk.web.api.auth.TokenUserDto;
import com.dzk.web.redis.RedisComponent;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String SECRET_KEY = "your-secret-key-here-make-it-long-enough-for-hs256";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private RedisComponent redisComponent;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String token = extractTokenFromRequest(request);
            
            if (token != null && validateToken(token)) {
                // 从Redis获取用户信息
                TokenUserDto tokenUserDto = redisComponent.getUserToken(Constants.REDIS_KEY_TOKEN_WEB + token);
                
                if (tokenUserDto != null) {
                    // 创建用户对象
                    User user = new User();
                    user.setId(tokenUserDto.getId());
                    user.setUsername(tokenUserDto.getUsername());
                    user.setNickname(tokenUserDto.getNickname());
                    user.setAvatar(tokenUserDto.getAvatar());
                    // 设置其他需要的用户属性
                    
                    // 创建认证对象，存储完整的用户信息
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            user,  // 存储完整的用户对象
                            null,  // 密码设为null
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // 记录日志但不抛出异常，让请求继续处理
            logger.warn("JWT token validation failed: " + e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY);
            return signedJWT.verify(verifier);
        } catch (JOSEException | ParseException e) {
            return false;
        }
    }

    private JWTClaimsSet getClaimsFromToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet();
    }
} 