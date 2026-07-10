package com.ajith.KnowTheRound.model;

import com.ajith.KnowTheRound.enums.Difficulty;
import com.ajith.KnowTheRound.enums.InterviewResult;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interview_rounds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer roundNumber;

    @Column(nullable = false)
    private String roundName;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(columnDefinition = "TEXT")
    private String questionsAsked;

    @Column(columnDefinition = "TEXT")
    private String tips;

    @Enumerated(EnumType.STRING)
    private InterviewResult result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_experience_id", nullable = false)
    private InterviewExperience interviewExperience;
}