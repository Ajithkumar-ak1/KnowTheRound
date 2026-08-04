package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.admin.AdminDashboardResponseDto;
import com.ajith.KnowTheRound.dto.admin.AdminUserStatusRequestDto;
import com.ajith.KnowTheRound.dto.experience.InterviewExperienceResponse;
import com.ajith.KnowTheRound.dto.user.UserProfileResponseDto;
import com.ajith.KnowTheRound.enums.Difficulty;
import com.ajith.KnowTheRound.enums.Role;
import com.ajith.KnowTheRound.exception.BadRequestException;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.mapper.InterviewExperienceMapper;
import com.ajith.KnowTheRound.mapper.UserMapper;
import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.repository.*;
import com.ajith.KnowTheRound.specification.InterviewExperienceSpecification;
import com.ajith.KnowTheRound.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.ajith.KnowTheRound.dto.admin.AdminUserResponseDto;
import com.ajith.KnowTheRound.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

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
    private final InterviewExperienceMapper interviewExperienceMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserMapper userMapper;
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

    public Page<InterviewExperienceResponse> getAllExperiences(
            String company,
            String jobRole,
            List<String> technologies,
            Difficulty difficulty,
            Pageable pageable) {

        Specification<InterviewExperience> specification =
                InterviewExperienceSpecification.filterBy(
                        company,
                        jobRole,
                        technologies,
                        difficulty
                );

        return interviewExperienceRepository
                .findAll(specification, pageable)
                .map(experience -> interviewExperienceMapper.toResponse(experience, null));
    }

    public void deleteExperience(Long id) {

        InterviewExperience experience = interviewExperienceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Interview Experience not found"));

        interviewExperienceRepository.delete(experience);
    }

    @Transactional
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with ID: " + userId));

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getId().equals(userId)) {
            throw new BadRequestException("You cannot delete your own account.");
        }

        likeRepository.deleteByUser(user);
        refreshTokenRepository.deleteByUser(user);
        emailVerificationTokenRepository.deleteByUser(user);

        userRepository.delete(user);
    }

    public UserProfileResponseDto updateUserStatus(Long userId, AdminUserStatusRequestDto request){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentUserEmail = authentication.getName();

        if (user.getEmail().equals(currentUserEmail) && !request.getEnabled()) {
            throw new RuntimeException("You cannot disable your own account");
        }

        if (user.getRole().equals(Role.ADMIN) && !request.getEnabled()) {
            throw new RuntimeException("Admin account cannot be disabled");
        }

        user.setAccountActive(request.getEnabled());

        User updatedUser = userRepository.save(user);

        return userMapper.toUserProfileResponseDto(updatedUser);
    }

    public Page<AdminUserResponseDto> getUsers(
            String search,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return userRepository
                .findAll(UserSpecification.searchUsers(search), pageable)
                .map(user -> AdminUserResponseDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .createdAt(user.getCreatedAt())
                        .build()
                );
    }
}