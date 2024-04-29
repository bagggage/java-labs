package com.example.lab.service;

import com.example.lab.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private String generateToken(Map<String, Object> extraClaims, User user) {
        return Jwts.builder().claims(extraClaims)
        .subject(user.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
        .signWith(SignatureAlgorithm.HS256, getSigningKey()).compact();
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
    
        return generateToken(claims, user);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .setSigningKey(getSigningKey()).build()
            .parseClaimsJws(token)
            .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, User user) {
        final String userName = extractUsername(token);
        return (userName.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
