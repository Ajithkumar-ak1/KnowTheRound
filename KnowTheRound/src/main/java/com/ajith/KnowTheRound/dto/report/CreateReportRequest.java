package com.ajith.KnowTheRound.dto.report;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReportRequest {

    @NotBlank(message = "Reason is required")
    private String reason;

    private String description;
}