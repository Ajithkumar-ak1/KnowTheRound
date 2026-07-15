package com.ajith.KnowTheRound.controller;

import com.ajith.KnowTheRound.dto.experience.CreateInterviewExperienceRequest;
import com.ajith.KnowTheRound.dto.experience.InterviewExperienceResponse;
import com.ajith.KnowTheRound.dto.experience.UpdateInterviewExperienceRequest;
import com.ajith.KnowTheRound.service.InterviewExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview-experiences")
@RequiredArgsConstructor
public class InterviewExperienceController {

    private final InterviewExperienceService interviewExperienceService;

    @PostMapping
    public InterviewExperienceResponse createInterviewExperience(
            @Valid @RequestBody CreateInterviewExperienceRequest request) {

        return interviewExperienceService.createInterviewExperience(request);
    }

    @GetMapping
    public List<InterviewExperienceResponse> getAllInterviewExperiences() {
        return interviewExperienceService.getAllInterviewExperiences();
    }

    @GetMapping("/{id}")
    public InterviewExperienceResponse getInterviewExperienceById(
            @PathVariable Long id) {

        return interviewExperienceService.getInterviewExperienceById(id);
    }

    @PutMapping("/{id}")
    public InterviewExperienceResponse updateInterviewExperience(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInterviewExperienceRequest request) {

        return interviewExperienceService.updateInterviewExperience(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteInterviewExperience(@PathVariable Long id) {

        interviewExperienceService.deleteInterviewExperience(id);

        return "Interview Experience deleted successfully.";
    }
}