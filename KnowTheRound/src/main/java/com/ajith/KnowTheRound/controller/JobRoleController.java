package com.ajith.KnowTheRound.controller;

import com.ajith.KnowTheRound.dto.jobrole.JobRoleRequestDto;
import com.ajith.KnowTheRound.dto.jobrole.JobRoleResponseDto;
import com.ajith.KnowTheRound.service.JobRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-roles")
@RequiredArgsConstructor
public class JobRoleController {

    private final JobRoleService jobRoleService;

    @GetMapping
    public ResponseEntity<List<JobRoleResponseDto>> getAllJobRoles() {
        return ResponseEntity.ok(jobRoleService.getAllJobRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobRoleResponseDto> getJobRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(jobRoleService.getJobRoleById(id));
    }

    @PostMapping
    public ResponseEntity<JobRoleResponseDto> createJobRole(
            @Valid @RequestBody JobRoleRequestDto request) {

        return new ResponseEntity<>(
                jobRoleService.createJobRole(request),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobRoleResponseDto> updateJobRole(
            @PathVariable Long id,
            @Valid @RequestBody JobRoleRequestDto request) {

        return ResponseEntity.ok(
                jobRoleService.updateJobRole(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteJobRole(@PathVariable Long id) {

        jobRoleService.deleteJobRole(id);
        return ResponseEntity.noContent().build();
    }
}
