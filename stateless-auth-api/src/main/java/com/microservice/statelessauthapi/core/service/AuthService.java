package com.microservice.statelessauthapi.core.service;

import com.microservice.statelessauthapi.core.dto.AuthRequest;
import com.microservice.statelessauthapi.core.dto.TokenDTO;
import com.microservice.statelessauthapi.core.repository.UserRepository;
import com.microservice.statelessauthapi.infra.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public TokenDTO login(AuthRequest request) {
        var user = this.userRepository
            .findByUsername(request.username())
            .orElseThrow(() -> new ValidationException("User not found!"));
        var accessToken = jwtService.createToken(user);
        validatePassword(request.password(), user.getPassword());
        return new TokenDTO(accessToken);
    }

    private void validatePassword(String password, String encodedPassword) {
        if (isEmpty(password)) {
            throw new ValidationException("The password must be informed!");
        }
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new ValidationException("Invalid password!");
        }
    }

    public TokenDTO validateToken(String token) {
        validateExistingToken(token);
        this.jwtService.validateAccessToken(token);
        return new TokenDTO(token);
    }

    private void validateExistingToken(String token) {
        if (isEmpty(token)) {
            throw new ValidationException("The access token must be informed!");
        }
    }
}
