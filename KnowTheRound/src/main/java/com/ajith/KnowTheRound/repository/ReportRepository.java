package com.ajith.KnowTheRound.repository;

import com.ajith.KnowTheRound.enums.ReportStatus;
import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.model.Report;
import com.ajith.KnowTheRound.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findByStatus(ReportStatus status, Pageable pageable);

    Page<Report> findByReportedBy(User user, Pageable pageable);

    Page<Report> findByExperience(InterviewExperience experience, Pageable pageable);

    long countByStatus(ReportStatus status);

    boolean existsByReportedByAndExperience(User reportedBy, InterviewExperience experience);
}