package com.ajith.KnowTheRound.dto.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserStatusRequestDto {

    @NotNull(message = "Enabled status is required")
    private Boolean enabled;

}