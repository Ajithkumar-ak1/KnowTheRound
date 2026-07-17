package com.ajith.KnowTheRound.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardResponseDto {

    private long totalUsers;
    private long totalCompanies;
    private long totalJobRoles;
    private long totalTechnologies;
    private long totalExperiences;
    private long totalInterviewRounds;
    private long totalLikes;
    private long totalBookmarks;
}