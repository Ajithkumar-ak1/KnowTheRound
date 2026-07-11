package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.repository.InterviewRoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewRoundService {
    private final InterviewRoundRepository interviewRoundRepository;
}
