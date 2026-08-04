package com.ajith.KnowTheRound.repository;

import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.model.Like;
import com.ajith.KnowTheRound.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndInterviewExperience(
            User user,
            InterviewExperience interviewExperience
    );

    boolean existsByUserAndInterviewExperience(
            User user,
            InterviewExperience interviewExperience
    );

    void deleteByUserAndInterviewExperience(
            User user,
            InterviewExperience interviewExperience
    );

    List<Like> findByUser(User user);
    void deleteByUser(User user);
    Long countByInterviewExperience(InterviewExperience interviewExperience);
    long countByInterviewExperienceUser(User user);
}