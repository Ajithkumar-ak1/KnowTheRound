package com.ajith.KnowTheRound.mapper;

import com.ajith.KnowTheRound.dto.user.UserProfileResponseDto;
import com.ajith.KnowTheRound.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileResponseDto toUserProfileResponseDto(User user) {
        return UserProfileResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .joinedAt(user.getCreatedAt())
                .role(user.getRole())
                .build();
    }
}