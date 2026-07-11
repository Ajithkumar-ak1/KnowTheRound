package com.ajith.KnowTheRound.repository;

import com.ajith.KnowTheRound.model.JobRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRole, Long> {

    Optional<JobRole> findByName(String name);
}