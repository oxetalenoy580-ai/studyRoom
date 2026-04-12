package com.study.controller;

import io.jsonwebtoken.JwtException;
import com.study.utils.JwtUtil;
import org.springframework.util.StringUtils;

public class BaseController {

    protected String requireRole(String token, String expectedRole) {
        String currentToken = requireToken(token);
        try {
            String username = JwtUtil.getUsername(currentToken);
            String role = JwtUtil.getRole(currentToken);
            if (!StringUtils.hasText(username)) {
                throw new RuntimeException("Login expired");
            }
            if (StringUtils.hasText(expectedRole) && !expectedRole.equals(role)) {
                throw new RuntimeException("Access denied");
            }
            return username;
        } catch (JwtException e) {
            throw new RuntimeException("Login expired");
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Login expired");
        }
    }

    protected String requireToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("Please login first");
        }
        return token;
    }
}
