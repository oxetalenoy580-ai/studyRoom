package com.study.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtUtil {
  private static final SecretKey KEY =
      Keys.hmacShaKeyFor("study-room-jwt-secret-key-2026-123456".getBytes(StandardCharsets.UTF_8));
  private static final long EXPIRE = 24 * 60 * 60 * 1000L;

  public static String createToken(String identifier, String username) {
    return createToken(identifier, username, null);
  }

  public static String createToken(String identifier, String username, String role) {
    return Jwts.builder()
        .claim("identifier", identifier)
        .claim("username", username)
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + EXPIRE))
        .signWith(KEY)
        .compact();
  }

  public static Claims parseToken(String token) {
    return Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token).getPayload();
  }

  public static String getIdentifier(String token) {
    return parseToken(token).get("identifier", String.class);
  }

  public static String getUsername(String token) {
    return parseToken(token).get("username", String.class);
  }

  public static String getRole(String token) {
    return parseToken(token).get("role", String.class);
  }
}
