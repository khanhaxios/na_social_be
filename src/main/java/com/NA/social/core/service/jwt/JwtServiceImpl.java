package com.NA.social.core.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${app.jwtSecret}")
    private String SECRET_KEY;
    @Value("${app.jwtExpirationInMs}")
    private long SECRET_EXPIRED;


    @Override
    public String extractUserName(String token)
     {
        return extractClaims(token, Claims::getSubject);
    }

    @Override
    public String signToken(UserDetails userDetails) 
    {
        return signToken(new HashMap<>(), userDetails);
    }

    @Override
    public String signToken(Map<String, Objects> claims, UserDetails userDetails)
     {
        return Jwts.builder()
        .setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + SECRET_EXPIRED))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    @Override
    public <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) 
    {
        Claims claims = extractAll(token);
        return claimsTFunction.apply(claims);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) 
    {
        String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public boolean isTokenExpired(String token) 
    {
        Date expired = extractExpired(token);
        return expired.before(new Date(System.currentTimeMillis()));
    }

    @Override
    public Date extractExpired(String token) 
    {
        return extractClaims(token, Claims::getExpiration);
    }

    @Override
    public Claims extractAll(String token)
     {
        return Jwts.parser()
        .setSigningKey(getSignKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
    }

    @Override
    public Key getSignKey() 
    {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);

    }
}
