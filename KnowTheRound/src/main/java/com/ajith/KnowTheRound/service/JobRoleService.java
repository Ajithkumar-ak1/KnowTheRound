package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.jobrole.JobRoleRequestDto;
import com.ajith.KnowTheRound.dto.jobrole.JobRoleResponseDto;
import com.ajith.KnowTheRound.exception.DuplicateResourceException;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.model.JobRole;
import com.ajith.KnowTheRound.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobRoleService {

    private final JobRoleRepository jobRoleRepository;

    public JobRoleResponseDto createJobRole(JobRoleRequestDto request) {

        if (jobRoleRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateResourceException("Job role already exists");
        }

        JobRole jobRole = JobRole.builder()
                .name(request.getName())
                .build();

        return mapToDto(jobRoleRepository.save(jobRole));
    }

    public List<JobRoleResponseDto> getAllJobRoles() {

        return jobRoleRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public JobRoleResponseDto getJobRoleById(Long id) {

        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job role not found"));

        return mapToDto(jobRole);
    }

    public JobRoleResponseDto updateJobRole(Long id, JobRoleRequestDto request) {

        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job role not found"));

        jobRoleRepository.findByName(request.getName())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new DuplicateResourceException("Job role already exists");
                    }
                });

        jobRole.setName(request.getName());

        return mapToDto(jobRoleRepository.save(jobRole));
    }

    public void deleteJobRole(Long id) {

        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job role not found"));

        jobRoleRepository.delete(jobRole);
    }

    private JobRoleResponseDto mapToDto(JobRole jobRole) {

        return JobRoleResponseDto.builder()
                .id(jobRole.getId())
                .name(jobRole.getName())
                .build();
    }
}