package com.ajith.KnowTheRound.dto.technology;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TechnologyResponseDto {

    private Long id;
    private String name;
}