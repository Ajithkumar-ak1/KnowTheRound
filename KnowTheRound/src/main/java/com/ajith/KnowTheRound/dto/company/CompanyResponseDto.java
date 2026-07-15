package com.ajith.KnowTheRound.dto.company;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyResponseDto {

    private Long id;

    private String name;

    private String logoUrl;

    private String website;
}