package com.microservice.statelessanyapi.core.controller;

import com.microservice.statelessanyapi.core.dto.AnyResponse;
import com.microservice.statelessanyapi.core.service.AnyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/resource")
public class AnyController {

    private final AnyService anyService;

    @GetMapping
    public AnyResponse getResource(@RequestHeader String token) {
        return anyService.getData(token);
    }
}
