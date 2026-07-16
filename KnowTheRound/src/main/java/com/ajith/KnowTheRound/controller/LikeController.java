package com.ajith.KnowTheRound.controller;

import com.ajith.KnowTheRound.dto.experience.InterviewExperienceResponse;
import com.ajith.KnowTheRound.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{experienceId}")
    public ResponseEntity<String> likeExperience(
            @PathVariable Long experienceId
    ) {

        likeService.likeExperience(experienceId);

        return ResponseEntity.ok("Interview experience liked successfully");
    }

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<String> removeLike(
            @PathVariable Long experienceId
    ) {

        likeService.removeLike(experienceId);

        return ResponseEntity.ok("Like removed successfully");
    }

    @GetMapping("/my")
    public ResponseEntity<List<InterviewExperienceResponse>> getMyLikes() {

        return ResponseEntity.ok(likeService.getMyLikes());
    }

    @GetMapping("/count/{experienceId}")
    public ResponseEntity<Long> getLikeCount(
            @PathVariable Long experienceId
    ) {

        return ResponseEntity.ok(likeService.getLikeCount(experienceId));
    }
}