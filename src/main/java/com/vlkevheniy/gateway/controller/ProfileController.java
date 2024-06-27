package com.vlkevheniy.gateway.controller;

import com.vlkevheniy.gateway.auth.dto.UserInfo;
import com.vlkevheniy.gateway.exception.UnauthorizedException;
import com.vlkevheniy.gateway.model.UserSession;
import com.vlkevheniy.gateway.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final SessionService sessionService;

    @GetMapping("/profile")
    public Mono<UserInfo> profile(ServerWebExchange exchange) {
        return sessionService.checkSession(exchange)
                .flatMap(this::toUserInfo)
                .onErrorResume(UnauthorizedException.class, e -> {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
                });
    }

    private Mono<UserInfo> toUserInfo(UserSession session) {
        return Mono.just(UserInfo.builder()
                .email(session.getEmail())
                .name(session.getName())
                .build());
    }



}
