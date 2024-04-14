package com.microservice.statelessanyapi.core.service;

import com.microservice.statelessanyapi.core.dto.AuthUserResponse;
import com.microservice.statelessanyapi.infra.exception.AuthenticationException;
import com.microservice.statelessanyapi.infra.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;

    @Value("${app.token.secret-key}")
    private String secretKey;

    public AuthUserResponse getAuthenticatedUser(String token) {
        var claims = getClaims(token);
        var userId = Integer.valueOf(claims.get("id").toString());
        var username = claims.get("username").toString();
        return new AuthUserResponse(userId, username);
    }

    private Claims getClaims(String token) {
        var accessToken = extractToken(token);
        try {
            return Jwts
                .parser()
                .verifyWith(generateSign())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
        } catch (Exception e) {
            throw new AuthenticationException("Invalid token " + e.getMessage());
        }
    }

    public void validateToken(String token) {
        getClaims(token);
    }

    private SecretKey generateSign() {
        return Keys.hmacShaKeyFor(this.secretKey.getBytes());
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
