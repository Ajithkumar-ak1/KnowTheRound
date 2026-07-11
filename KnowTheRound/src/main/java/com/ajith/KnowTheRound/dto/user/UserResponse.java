package com.ajith.KnowTheRound.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String profilePicture;
    private LocalDateTime createdAt;
}