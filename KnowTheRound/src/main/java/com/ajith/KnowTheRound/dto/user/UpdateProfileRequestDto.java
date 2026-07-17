package com.ajith.KnowTheRound.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequestDto {

    @NotBlank
    private String name;

    private String profilePicture;
}