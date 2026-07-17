package com.ajith.KnowTheRound.dto.user;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponseDto {

    private Long id;

    private String name;

    private String email;

    private String profilePicture;

    private LocalDateTime joinedAt;

    private long totalExperiences;

    private long totalLikesReceived;

    private List<String> technologies;

    private List<String> companies;
}