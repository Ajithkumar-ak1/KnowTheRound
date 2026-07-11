package com.ajith.KnowTheRound.dto.experience;

import com.ajith.KnowTheRound.enums.Difficulty;
import com.ajith.KnowTheRound.enums.InterviewResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterviewRoundRequest {

    private Integer roundNumber;
    private String roundName;
    private Difficulty difficulty;
    private String questionsAsked;
    private String tips;
    private InterviewResult result;
}