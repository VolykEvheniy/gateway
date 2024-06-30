package com.vlkevheniy.gateway.service;

import com.vlkevheniy.gateway.auth.dto.UserInfo;
import com.vlkevheniy.gateway.exception.UnauthorizedException;
import com.vlkevheniy.gateway.model.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final SessionService sessionService;


    public Mono<UserInfo> getAuthUserData(ServerWebExchange exchange) {
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
                .authorities(List.of("ENABLE_SEE_SECRET_PAGE", "ENABLE_SEE_CARS_PAGE", "ENABLE_SEE_CAR_DETAIL_PAGE"))
                .build());
    }
}
