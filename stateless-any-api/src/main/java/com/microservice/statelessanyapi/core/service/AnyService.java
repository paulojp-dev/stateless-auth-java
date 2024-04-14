package com.microservice.statelessanyapi.core.service;

import com.microservice.statelessanyapi.core.dto.AnyResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnyService {

    private final JwtService jwtService;

    public AnyResponse getData(String token) {
        jwtService.validateToken(token);
        var authUser = jwtService.getAuthenticatedUser(token);
        var ok = HttpStatus.OK;
        return new AnyResponse(ok.name(), ok.value(), authUser);
    }
}
