package com.NA.social.core.service.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public interface JwtService {


    String extractUserName(String token);

    String signToken(UserDetails userDetails);

    String signToken(Map<String, Objects> claims, UserDetails userDetails);

    <T> T extractClaims(String token, Function<Claims, T> claimsTFunction);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    Date extractExpired(String token);

    Claims extractAll(String token);

    Key getSignKey();

}
