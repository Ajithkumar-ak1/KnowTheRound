package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.experience.InterviewExperienceResponse;
import com.ajith.KnowTheRound.exception.ResourceAlreadyExistsException;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.mapper.InterviewExperienceMapper;
import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.model.Like;
import com.ajith.KnowTheRound.model.User;
import com.ajith.KnowTheRound.repository.InterviewExperienceRepository;
import com.ajith.KnowTheRound.repository.LikeRepository;
import com.ajith.KnowTheRound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final InterviewExperienceRepository interviewExperienceRepository;
    private final UserRepository userRepository;
    private final InterviewExperienceMapper interviewExperienceMapper;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void likeExperience(Long experienceId) {

        User user = getCurrentUser();

        InterviewExperience experience = interviewExperienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview Experience not found"));

        if (likeRepository.existsByUserAndInterviewExperience(user, experience)) {
            throw new ResourceAlreadyExistsException("Interview experience already liked");
        }

        Like like = Like.builder()
                .user(user)
                .interviewExperience(experience)
                .build();

        likeRepository.save(like);
    }

    public void removeLike(Long experienceId) {

        User user = getCurrentUser();

        InterviewExperience experience = interviewExperienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview Experience not found"));

        Like like = likeRepository
                .findByUserAndInterviewExperience(user, experience)
                .orElseThrow(() -> new ResourceNotFoundException("Like not found"));

        likeRepository.delete(like);
    }

    public List<InterviewExperienceResponse> getMyLikes() {

        User user = getCurrentUser();

        return likeRepository.findByUser(user)
                .stream()
                .map(Like::getInterviewExperience)
                .map(experience -> interviewExperienceMapper.toResponse(experience, user))
                .toList();
    }

    public Long getLikeCount(Long experienceId) {

        InterviewExperience experience = interviewExperienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview Experience not found"));

        return likeRepository.countByInterviewExperience(experience);
    }
}