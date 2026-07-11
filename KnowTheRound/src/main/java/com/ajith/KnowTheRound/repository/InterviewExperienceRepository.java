package com.ajith.KnowTheRound.repository;

import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterviewExperienceRepository extends JpaRepository<InterviewExperience, Long> {

    List<InterviewExperience> findByUser(User user);

    List<InterviewExperience> findByCompanyIgnoreCase(String company);

    List<InterviewExperience> findByRoleIgnoreCase(String role);

    List<InterviewExperience> findByResultIgnoreCase(String result);
}