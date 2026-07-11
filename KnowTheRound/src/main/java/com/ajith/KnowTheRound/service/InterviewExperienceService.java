package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.repository.InterviewExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewExperienceService {
    private final InterviewExperienceRepository interviewExperienceRepository;
}
