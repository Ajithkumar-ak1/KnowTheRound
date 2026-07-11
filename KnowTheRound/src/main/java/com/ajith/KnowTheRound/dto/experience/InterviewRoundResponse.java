package com.ajith.KnowTheRound.dto.experience;

import com.ajith.KnowTheRound.enums.Difficulty;
import com.ajith.KnowTheRound.enums.InterviewResult;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InterviewRoundResponse {

    private Long id;
    private Integer roundNumber;
    private String roundName;
    private Difficulty difficulty;
    private String questionsAsked;
    private String tips;
    private InterviewResult result;
}