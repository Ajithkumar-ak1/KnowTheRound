package com.ajith.KnowTheRound.dto.experience;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDto {

    private Long id;
    private String name;
    private String profilePicture;
}