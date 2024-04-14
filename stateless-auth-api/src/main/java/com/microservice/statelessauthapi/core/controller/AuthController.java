package com.microservice.statelessauthapi.core.controller;

import com.microservice.statelessauthapi.core.dto.AuthRequest;
import com.microservice.statelessauthapi.core.dto.TokenDTO;
import com.microservice.statelessauthapi.core.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public TokenDTO login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("token/validate")
    public TokenDTO login(@RequestHeader String token) {
        return authService.validateToken(token);
    }
}
