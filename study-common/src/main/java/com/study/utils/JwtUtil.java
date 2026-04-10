package com.study.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtUtil {
  private static final SecretKey KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
  private static final long EXPIRE = 24 * 60 * 60 * 1000L;

  public static String createToken(String identifier, String username) {
    return Jwts.builder()
        .claim("identifier", identifier)
        .claim("username", username)
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
}

