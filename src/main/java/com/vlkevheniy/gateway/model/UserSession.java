package com.vlkevheniy.gateway.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@Table(name = "user_session")
public class UserSession {

    @Id
    private String id;
    private String email;
    private String name;
    private Instant expiresAt;

    public boolean isExpired() {
        return expiresAt.isBefore(Instant.now());
    }
}
