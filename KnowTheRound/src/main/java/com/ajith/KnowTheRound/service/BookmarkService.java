package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.experience.InterviewExperienceResponse;
import com.ajith.KnowTheRound.exception.ResourceAlreadyExistsException;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.mapper.InterviewExperienceMapper;
import com.ajith.KnowTheRound.model.Bookmark;
import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.model.User;
import com.ajith.KnowTheRound.repository.BookmarkRepository;
import com.ajith.KnowTheRound.repository.InterviewExperienceRepository;
import com.ajith.KnowTheRound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final InterviewExperienceRepository interviewExperienceRepository;
    private final UserRepository userRepository;
    private final InterviewExperienceMapper interviewExperienceMapper;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void bookmarkExperience(Long experienceId) {

        User user = getCurrentUser();

        InterviewExperience experience = interviewExperienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview Experience not found"));

        if (bookmarkRepository.existsByUserAndInterviewExperience(user, experience)) {
            throw new ResourceAlreadyExistsException("Interview experience already bookmarked");
        }

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .interviewExperience(experience)
                .build();

        bookmarkRepository.save(bookmark);
    }

    public void removeBookmark(Long experienceId) {

        User user = getCurrentUser();

        InterviewExperience experience = interviewExperienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview Experience not found"));

        Bookmark bookmark = bookmarkRepository
                .findByUserAndInterviewExperience(user, experience)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found"));

        bookmarkRepository.delete(bookmark);
    }

    public List<InterviewExperienceResponse> getMyBookmarks() {

        User user = getCurrentUser();

        return bookmarkRepository.findByUser(user)
                .stream()
                .map(Bookmark::getInterviewExperience)
                .map(experience -> interviewExperienceMapper.toResponse(experience, true))
                .toList();
    }
}