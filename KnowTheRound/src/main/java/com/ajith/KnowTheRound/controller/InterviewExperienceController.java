package com.ajith.KnowTheRound.controller;

import com.ajith.KnowTheRound.dto.experience.CreateInterviewExperienceRequest;
import com.ajith.KnowTheRound.dto.experience.InterviewExperienceResponse;
import com.ajith.KnowTheRound.dto.experience.UpdateInterviewExperienceRequest;
import com.ajith.KnowTheRound.service.InterviewExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import com.ajith.KnowTheRound.enums.Difficulty;
import com.ajith.KnowTheRound.enums.InterviewResult;
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
    public ResponseEntity<Page<InterviewExperienceResponse>> getAllInterviewExperiences(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "createdAt") String sortBy,

            @RequestParam(defaultValue = "desc") String sortDir,

            @RequestParam(required = false) String company,

            @RequestParam(required = false) String jobRole,

            @RequestParam(required = false) List<String> technology,

            @RequestParam(required = false) Difficulty difficulty,

            @RequestParam(required = false) InterviewResult result,

            @RequestParam(required = false) String location

    ) {

        return ResponseEntity.ok(

                interviewExperienceService.getAllInterviewExperiences(
                        page,
                        size,
                        sortBy,
                        sortDir,
                        company,
                        jobRole,
                        technology,
                        difficulty,
                        result,
                        location
                )

        );

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