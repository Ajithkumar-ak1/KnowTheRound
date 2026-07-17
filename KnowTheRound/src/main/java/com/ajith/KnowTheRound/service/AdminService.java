package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.admin.AdminDashboardResponseDto;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.repository.BookmarkRepository;
import com.ajith.KnowTheRound.repository.CompanyRepository;
import com.ajith.KnowTheRound.repository.InterviewExperienceRepository;
import com.ajith.KnowTheRound.repository.InterviewRoundRepository;
import com.ajith.KnowTheRound.repository.JobRoleRepository;
import com.ajith.KnowTheRound.repository.LikeRepository;
import com.ajith.KnowTheRound.repository.TechnologyRepository;
import com.ajith.KnowTheRound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ajith.KnowTheRound.dto.admin.AdminUserResponseDto;
import com.ajith.KnowTheRound.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobRoleRepository jobRoleRepository;
    private final TechnologyRepository technologyRepository;
    private final InterviewExperienceRepository interviewExperienceRepository;
    private final InterviewRoundRepository interviewRoundRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;

    public AdminDashboardResponseDto getDashboardStats() {

        return AdminDashboardResponseDto.builder()
                .totalUsers(userRepository.count())
                .totalCompanies(companyRepository.count())
                .totalJobRoles(jobRoleRepository.count())
                .totalTechnologies(technologyRepository.count())
                .totalExperiences(interviewExperienceRepository.count())
                .totalInterviewRounds(interviewRoundRepository.count())
                .totalLikes(likeRepository.count())
                .totalBookmarks(bookmarkRepository.count())
                .build();
    }

    public Page<AdminUserResponseDto> getAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(this::mapToAdminUserResponse);
    }

    private AdminUserResponseDto mapToAdminUserResponse(User user) {

        return AdminUserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public AdminUserResponseDto getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToAdminUserResponse(user);
    }
}