package com.ajith.KnowTheRound.dto.report;

import com.ajith.KnowTheRound.enums.ReportStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponseDto {

    private Long id;

    private String reason;

    private String description;

    private ReportStatus status;

    private Long reportedById;

    private String reportedByName;

    private Long experienceId;

    private String companyName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}