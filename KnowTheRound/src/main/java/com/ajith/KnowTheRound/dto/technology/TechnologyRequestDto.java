package com.ajith.KnowTheRound.dto.technology;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnologyRequestDto {

    @NotBlank(message = "Technology name is required")
    private String name;
}