package com.ajith.KnowTheRound.dto.jobrole;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobRoleRequestDto {

    @NotBlank(message = "Job role name is required")
    private String name;
}