package com.spring.userservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service class to handle JWT token creation, validation, and extraction.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // Token validity duration (1 day)  
    private static final long TOKEN_VALIDITY = 86400000L;

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param user UserDetails object of the authenticated user
     * @return JWT token as a string
     */
    public String generateToken(UserDetails user) {
        return Jwts.builder()
        		.setSubject(user.getUsername())
                .claim("authorities", populateAuthorities(user.getAuthorities()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token JWT token
     * @return Username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts specific claims from a JWT token.
     *
     * @param <T> Return type of the claim
     * @param token JWT token
     * @param claimsResolver Function to resolve the claim
     * @return The extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Validates the JWT token against the provided UserDetails.
     *
     * @param token JWT token
     * @param userDetails UserDetails of the authenticated user
     * @return True if the token is valid, otherwise false
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername());
    }

    /**
     * Retrieves the signing key from the secret for token creation and validation.
     *
     * @return Signing key
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token JWT token
     * @return Claims contained in the token
     * @throws IllegalArgumentException if token parsing fails
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
    }

    /**
     * Populates the authorities of a user as a comma-separated string.
     *
     * @param authorities Collection of GrantedAuthority
     * @return Comma-separated string of authorities
     */
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
