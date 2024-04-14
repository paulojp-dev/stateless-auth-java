package com.microservice.statelessauthapi.core.service;

import com.microservice.statelessauthapi.core.model.User;
import com.microservice.statelessauthapi.infra.exception.AuthenticationException;
import com.microservice.statelessauthapi.infra.exception.ValidationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class JWTService {

    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;
    private static final Integer ONE_DAY_IN_HOURS = 24;

    @Value("${app.token.secret-key}")
    private String secretKey;

    public String createToken(User user) {
        var payload = new HashMap<String, String>();
        payload.put("id", user.getId().toString());
        payload.put("username", user.getUsername());
        return Jwts
            .builder()
            .claims(payload)
            .expiration(generateExpireAt())
            .signWith(generateSign())
            .compact();
    }

    private Date generateExpireAt() {
        return Date.from(
            LocalDateTime.now()
                .plusHours(ONE_DAY_IN_HOURS)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        );
    }

    private SecretKey generateSign() {
        return Keys.hmacShaKeyFor(this.secretKey.getBytes());
    }

    public void validateAccessToken(String token) {
        var accessToken = extractToken(token);
        try {
            Jwts
                .parser()
                .verifyWith(generateSign())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
        } catch (Exception e) {
            throw new AuthenticationException("Invalid token " + e.getMessage());
        }
    }

    private String extractToken(String token) {
        if (isEmpty(token)) {
            throw new ValidationException("The access token was not informed.");
        }
        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }
}
