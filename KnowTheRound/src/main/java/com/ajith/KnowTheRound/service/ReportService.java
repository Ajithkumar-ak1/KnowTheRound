package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.report.CreateReportRequest;
import com.ajith.KnowTheRound.dto.report.ReportResponseDto;
import com.ajith.KnowTheRound.dto.report.UpdateReportStatusRequest;
import com.ajith.KnowTheRound.enums.ReportStatus;
import com.ajith.KnowTheRound.exception.BadRequestException;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.mapper.ReportMapper;
import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.model.Report;
import com.ajith.KnowTheRound.model.User;
import com.ajith.KnowTheRound.repository.InterviewExperienceRepository;
import com.ajith.KnowTheRound.repository.ReportRepository;
import com.ajith.KnowTheRound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final InterviewExperienceRepository interviewExperienceRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;

    public ReportResponseDto createReport(
            Long experienceId,
            CreateReportRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        InterviewExperience experience =
                interviewExperienceRepository.findById(experienceId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Interview Experience not found"));

        if (reportRepository.existsByReportedByAndExperience(user, experience)) {
            throw new BadRequestException("You have already reported this interview experience.");
        }

        Report report = Report.builder()
                .reason(request.getReason())
                .description(request.getDescription())
                .reportedBy(user)
                .experience(experience)
                .build();

        reportRepository.save(report);

        return reportMapper.toDto(report);
    }

    public Page<ReportResponseDto> getMyReports(
            String email,
            Pageable pageable) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return reportRepository.findByReportedBy(user, pageable)
                .map(reportMapper::toDto);
    }

    public Page<ReportResponseDto> getReportsByStatus(
            ReportStatus status,
            Pageable pageable) {

        return reportRepository.findByStatus(status, pageable)
                .map(reportMapper::toDto);
    }

    public Page<ReportResponseDto> getAllReports(Pageable pageable) {

        return reportRepository.findAll(pageable)
                .map(reportMapper::toDto);
    }

    public ReportResponseDto getReportById(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Report not found"));

        return reportMapper.toDto(report);
    }

    public ReportResponseDto updateStatus(
            Long reportId,
            UpdateReportStatusRequest request) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Report not found"));

        report.setStatus(request.getStatus());

        reportRepository.save(report);

        return reportMapper.toDto(report);
    }

    public void deleteReport(Long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Report not found"));

        reportRepository.delete(report);
    }

}
