package com.ajith.KnowTheRound.controller;

import com.ajith.KnowTheRound.dto.user.UpdateProfileRequestDto;
import com.ajith.KnowTheRound.dto.user.UserProfileResponseDto;
import com.ajith.KnowTheRound.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponseDto> updateProfile(
            @Valid @RequestBody UpdateProfileRequestDto request) {

        return ResponseEntity.ok(userService.updateProfile(request));
    }
}