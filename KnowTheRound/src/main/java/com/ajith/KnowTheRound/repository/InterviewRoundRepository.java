package com.ajith.KnowTheRound.repository;

import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.model.InterviewRound;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterviewRoundRepository extends JpaRepository<InterviewRound, Long> {

    List<InterviewRound> findByInterviewExperienceOrderByRoundNumberAsc(
            InterviewExperience interviewExperience
    );
}