package com.example.flightapi.util;

import com.example.flightapi.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        logger.debug("Using secret key: {}", secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        String email = user.getEmail();
        logger.debug("Generating token for user with email: {}", email);
        claims.put("sub", email);
        claims.put("created", new Date());
        claims.put("userId", user.getId());
        logger.debug("Token claims: {}", claims);
        String token = generateToken(claims);
        logger.debug("Generated token: {}", token);
        return token;
    }

    private String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);
        logger.debug("Token expiry date: {}", expiryDate);
        
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
        
        logger.debug("Final generated token: {}", token);
        return token;
    }

    public String getUsernameFromToken(String token) {
        try {
            logger.debug("Attempting to extract username from token: {}", token);
            Claims claims = getClaimsFromToken(token);
            logger.debug("Extracted claims from token: {}", claims);
            String username = claims.getSubject();
            logger.debug("Extracted subject (username) from token: {}", username);
            return username;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token has expired: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.warn("JWT token is unsupported: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.warn("JWT claims string is empty: {}", e.getMessage());
            throw e;
        } catch (SignatureException e) {
            logger.warn("JWT signature validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.warn("Error extracting username from token: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            logger.debug("Validating token for user: {}, extracted username: {}", userDetails.getUsername(), username);
            boolean isValid = (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
            logger.debug("Token validation result: {}", isValid);
            return isValid;
        } catch (Exception e) {
            logger.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private Claims getClaimsFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.debug("Successfully parsed claims from token: {}", claims);
            return claims;
        } catch (Exception e) {
            logger.warn("Failed to parse claims from token: {}", e.getMessage());
            throw e;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getClaimsFromToken(token).getExpiration();
        boolean isExpired = expiration.before(new Date());
        logger.debug("Token expiration check - Expiration date: {}, Is expired: {}", expiration, isExpired);
        return isExpired;
    }
}
