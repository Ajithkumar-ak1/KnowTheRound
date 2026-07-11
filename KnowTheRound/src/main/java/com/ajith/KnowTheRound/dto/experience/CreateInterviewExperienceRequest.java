package com.ajith.KnowTheRound.dto.experience;

import com.ajith.KnowTheRound.enums.Difficulty;
import com.ajith.KnowTheRound.enums.InterviewResult;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreateInterviewExperienceRequest {

    private String title;
    private String overallExperience;
    private String preparationStrategy;

    private Difficulty difficulty;
    private InterviewResult result;

    private Double cgpa;
    private Integer yearsOfExperience;
    private BigDecimal packageOffered;
    private String location;

    private Long companyId;
    private Long jobRoleId;

    private List<Long> technologyIds;

    private List<InterviewRoundRequest> interviewRounds;
}