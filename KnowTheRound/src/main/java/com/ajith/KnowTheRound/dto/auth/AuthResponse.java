package com.ajith.KnowTheRound.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String type;
    private Long userId;
    private String name;
    private String email;
    private String role;
}
