package com.ajith.KnowTheRound.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {

    private String accessToken;
    private String refreshToken;
    private String type;

    private Long userId;
    private String name;
    private String email;
    private String role;
    private String message;
}
