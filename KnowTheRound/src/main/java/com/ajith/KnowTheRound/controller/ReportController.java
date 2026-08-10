package com.ajith.KnowTheRound.controller;

import com.ajith.KnowTheRound.dto.report.CreateReportRequest;
import com.ajith.KnowTheRound.dto.report.ReportResponseDto;
import com.ajith.KnowTheRound.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/experience/{experienceId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ReportResponseDto createReport(
            @PathVariable Long experienceId,
            @Valid @RequestBody CreateReportRequest request,
            Authentication authentication) {

        return reportService.createReport(
                experienceId,
                request,
                authentication.getName()
        );
    }

    @GetMapping("/my")
    public Page<ReportResponseDto> getMyReports(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable) {

        return reportService.getMyReports(
                authentication.getName(),
                pageable
        );
    }

}