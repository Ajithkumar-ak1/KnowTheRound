package com.ajith.KnowTheRound.repository;

import com.ajith.KnowTheRound.model.InterviewExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewExperienceRepository extends JpaRepository<InterviewExperience, Long> {

}