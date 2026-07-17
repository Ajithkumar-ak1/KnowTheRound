package com.ajith.KnowTheRound.repository;

import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InterviewExperienceRepository
        extends JpaRepository<InterviewExperience, Long>,
        JpaSpecificationExecutor<InterviewExperience> {

    List<InterviewExperience> findByUser(User user);

    long countByUser(User user);
}