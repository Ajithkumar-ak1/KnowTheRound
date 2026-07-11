package com.ajith.KnowTheRound.dto.experience;

import com.ajith.KnowTheRound.enums.Difficulty;
import com.ajith.KnowTheRound.enums.InterviewResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class InterviewExperienceResponse {

    private Long id;

    private String title;
    private String overallExperience;
    private String preparationStrategy;

    private Difficulty difficulty;
    private InterviewResult result;

    private Double cgpa;
    private Integer yearsOfExperience;
    private BigDecimal packageOffered;
    private String location;

    private String companyName;
    private String jobRoleName;

    private List<String> technologies;

    private UserResponseDto user;

    private List<InterviewRoundResponse> interviewRounds;

    private LocalDateTime createdAt;
}