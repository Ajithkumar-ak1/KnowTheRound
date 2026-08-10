package com.ajith.KnowTheRound.mapper;

import com.ajith.KnowTheRound.dto.report.ReportResponseDto;
import com.ajith.KnowTheRound.model.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public ReportResponseDto toDto(Report report) {

        return ReportResponseDto.builder()
                .id(report.getId())
                .reason(report.getReason())
                .description(report.getDescription())
                .status(report.getStatus())

                .reportedById(report.getReportedBy().getId())
                .reportedByName(report.getReportedBy().getName())

                .experienceId(report.getExperience().getId())
                .companyName(report.getExperience()
                        .getCompany()
                        .getName())

                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }
}