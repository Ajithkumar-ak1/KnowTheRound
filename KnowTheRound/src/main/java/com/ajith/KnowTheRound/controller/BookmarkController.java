package com.ajith.KnowTheRound.controller;

import com.ajith.KnowTheRound.dto.experience.InterviewExperienceResponse;
import com.ajith.KnowTheRound.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{experienceId}")
    public ResponseEntity<String> bookmark(@PathVariable Long experienceId) {
        bookmarkService.bookmarkExperience(experienceId);
        return ResponseEntity.ok("Interview experience bookmarked successfully.");
    }

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<String> removeBookmark(@PathVariable Long experienceId) {
        bookmarkService.removeBookmark(experienceId);
        return ResponseEntity.ok("Bookmark removed successfully.");
    }

    @GetMapping("/my")
    public ResponseEntity<List<InterviewExperienceResponse>> getMyBookmarks() {
        return ResponseEntity.ok(bookmarkService.getMyBookmarks());
    }
}