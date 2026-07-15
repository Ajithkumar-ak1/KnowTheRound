package com.ajith.KnowTheRound.dto.company;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequestDto {

    @NotBlank(message = "Company name is required")
    private String name;

    private String logoUrl;

    private String website;
}