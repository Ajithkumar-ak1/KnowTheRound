package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.user.UpdateProfileRequestDto;
import com.ajith.KnowTheRound.dto.user.UserProfileResponseDto;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.model.User;
import com.ajith.KnowTheRound.repository.InterviewExperienceRepository;
import com.ajith.KnowTheRound.repository.LikeRepository;
import com.ajith.KnowTheRound.repository.UserRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final InterviewExperienceRepository interviewExperienceRepository;
    private final LikeRepository likeRepository;
    @Column
    private String profilePicture;

    public UserProfileResponseDto getMyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToProfile(user);
    }

    public UserProfileResponseDto getUserProfile(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToProfile(user);
    }

    public UserProfileResponseDto updateProfile(UpdateProfileRequestDto request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setName(request.getName());
        user.setProfilePicture(request.getProfilePicture());

        userRepository.save(user);

        return mapToProfile(user);
    }

    private UserProfileResponseDto mapToProfile(User user) {

        List<InterviewExperience> experiences =
                interviewExperienceRepository.findByUser(user);

        List<String> technologies = experiences.stream()
                .flatMap(exp -> exp.getTechnologies().stream())
                .map(technology -> technology.getName())
                .distinct()
                .toList();

        List<String> companies = experiences.stream()
                .map(exp -> exp.getCompany().getName())
                .distinct()
                .toList();

        return UserProfileResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .joinedAt(user.getCreatedAt())
                .totalExperiences(interviewExperienceRepository.countByUser(user))
                .totalLikesReceived(likeRepository.countByInterviewExperienceUser(user))
                .technologies(technologies)
                .companies(companies)
                .build();
    }
}