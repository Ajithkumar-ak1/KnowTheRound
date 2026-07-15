package com.ajith.KnowTheRound.dto.jobrole;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JobRoleResponseDto {

    private Long id;
    private String name;
}