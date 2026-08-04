package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.user.UpdateProfileRequestDto;
import com.ajith.KnowTheRound.dto.user.UserProfileResponseDto;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.mapper.UserMapper;
import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.model.User;
import com.ajith.KnowTheRound.repository.InterviewExperienceRepository;
import com.ajith.KnowTheRound.repository.LikeRepository;
import com.ajith.KnowTheRound.repository.UserRepository;
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
    private final UserMapper userMapper;

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

        user = userRepository.save(user);

        return mapToProfile(user);
    }

    private UserProfileResponseDto mapToProfile(User user) {

        List<InterviewExperience> experiences = interviewExperienceRepository.findByUser(user);

        List<String> technologies = experiences.stream()
                .flatMap(exp -> exp.getTechnologies().stream())
                .map(technology -> technology.getName())
                .distinct()
                .toList();

        List<String> companies = experiences.stream()
                .map(exp -> exp.getCompany().getName())
                .distinct()
                .toList();

        UserProfileResponseDto dto = userMapper.toUserProfileResponseDto(user);

        dto.setTotalExperiences(experiences.size());

        dto.setTotalLikesReceived(
                likeRepository.countByInterviewExperienceUser(user)
        );

        dto.setTechnologies(technologies);

        dto.setCompanies(companies);

        return dto;
    }
}