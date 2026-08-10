package com.ajith.KnowTheRound.controller;

import com.ajith.KnowTheRound.dto.admin.AdminDashboardResponseDto;
import com.ajith.KnowTheRound.dto.admin.AdminUserStatusRequestDto;
import com.ajith.KnowTheRound.dto.experience.InterviewExperienceResponse;
import com.ajith.KnowTheRound.dto.experience.UserResponseDto;
import com.ajith.KnowTheRound.dto.report.ReportResponseDto;
import com.ajith.KnowTheRound.dto.user.UserProfileResponseDto;
import com.ajith.KnowTheRound.enums.Difficulty;
import com.ajith.KnowTheRound.service.AdminService;
import com.ajith.KnowTheRound.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ajith.KnowTheRound.dto.admin.AdminUserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import com.ajith.KnowTheRound.dto.report.ReportResponseDto;
import com.ajith.KnowTheRound.dto.report.UpdateReportStatusRequest;
import com.ajith.KnowTheRound.enums.ReportStatus;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final ReportService reportService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponseDto> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<AdminUserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @GetMapping("/experiences")
    public ResponseEntity<Page<InterviewExperienceResponse>> getAllExperiences(
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String jobRole,
            @RequestParam(required = false) List<String> technologies,
            @RequestParam(required = false) Difficulty difficulty,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(
                adminService.getAllExperiences(
                        company,
                        jobRole,
                        technologies,
                        difficulty,
                        pageable
                )
        );
    }

    @DeleteMapping("/experiences/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {

        adminService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {

        adminService.deleteUser(userId);

        return ResponseEntity.ok("User deleted successfully.");
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<UserProfileResponseDto> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody AdminUserStatusRequestDto request
    ) {

        return ResponseEntity.ok(
                adminService.updateUserStatus(userId, request)
        );
    }

    @GetMapping("/users")
    public ResponseEntity<Page<AdminUserResponseDto>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        return ResponseEntity.ok(
                adminService.getUsers(
                        search,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/reports")
    public Page<ReportResponseDto> getAllReports(
            @PageableDefault(size = 10) Pageable pageable) {

        return reportService.getAllReports(pageable);
    }

    @GetMapping("/reports/{reportId}")
    public ReportResponseDto getReportById(
            @PathVariable Long reportId) {

        return reportService.getReportById(reportId);
    }

    @GetMapping("/reports/status/{status}")
    public Page<ReportResponseDto> getReportsByStatus(
            @PathVariable ReportStatus status,
            @PageableDefault(size = 10) Pageable pageable) {

        return reportService.getReportsByStatus(status, pageable);
    }

    @PutMapping("/reports/{reportId}/status")
    public ReportResponseDto updateReportStatus(
            @PathVariable Long reportId,
            @Valid @RequestBody UpdateReportStatusRequest request) {

        return reportService.updateStatus(reportId, request);
    }

    @DeleteMapping("/reports/{reportId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(
            @PathVariable Long reportId) {

        reportService.deleteReport(reportId);
    }

}