package com.ajith.KnowTheRound.dto.admin;

import com.ajith.KnowTheRound.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserResponseDto {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}