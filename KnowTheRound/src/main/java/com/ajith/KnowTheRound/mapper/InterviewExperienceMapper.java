package com.ajith.KnowTheRound.mapper;

import com.ajith.KnowTheRound.dto.experience.InterviewExperienceResponse;
import com.ajith.KnowTheRound.dto.experience.InterviewRoundResponse;
import com.ajith.KnowTheRound.dto.experience.UserResponseDto;
import com.ajith.KnowTheRound.model.InterviewExperience;
import com.ajith.KnowTheRound.model.InterviewRound;
import com.ajith.KnowTheRound.model.Technology;
import com.ajith.KnowTheRound.model.User;
import com.ajith.KnowTheRound.repository.BookmarkRepository;
import com.ajith.KnowTheRound.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InterviewExperienceMapper {

    private final BookmarkRepository bookmarkRepository;
    private final LikeRepository likeRepository;

    public InterviewExperienceResponse toResponse(
            InterviewExperience experience,
            User currentUser
    ) {

        List<String> technologyNames = experience.getTechnologies()
                .stream()
                .map(Technology::getName)
                .toList();

        UserResponseDto userDto = UserResponseDto.builder()
                .id(experience.getUser().getId())
                .name(experience.getUser().getName())
                .profilePicture(experience.getUser().getProfilePicture())
                .build();

        List<InterviewRoundResponse> rounds = experience.getInterviewRounds()
                .stream()
                .map(this::mapRoundToResponse)
                .toList();

        boolean bookmarked = false;
        boolean liked = false;

        if (currentUser != null) {
            bookmarked = bookmarkRepository.existsByUserAndInterviewExperience(
                    currentUser,
                    experience
            );

            liked = likeRepository.existsByUserAndInterviewExperience(
                    currentUser,
                    experience
            );
        }

        Long likeCount = likeRepository.countByInterviewExperience(experience);

        return InterviewExperienceResponse.builder()
                .id(experience.getId())
                .title(experience.getTitle())
                .overallExperience(experience.getOverallExperience())
                .preparationStrategy(experience.getPreparationStrategy())
                .difficulty(experience.getDifficulty())
                .result(experience.getResult())
                .cgpa(experience.getCgpa())
                .yearsOfExperience(experience.getYearsOfExperience())
                .packageOffered(experience.getPackageOffered())
                .location(experience.getLocation())
                .companyName(experience.getCompany().getName())
                .jobRoleName(experience.getJobRole().getName())
                .technologies(technologyNames)
                .user(userDto)
                .interviewRounds(rounds)
                .createdAt(experience.getCreatedAt())
                .bookmarked(bookmarked)
                .liked(liked)
                .likeCount(likeCount)
                .build();
    }

    private InterviewRoundResponse mapRoundToResponse(InterviewRound round) {

        return InterviewRoundResponse.builder()
                .id(round.getId())
                .roundNumber(round.getRoundNumber())
                .roundName(round.getRoundName())
                .difficulty(round.getDifficulty())
                .questionsAsked(round.getQuestionsAsked())
                .tips(round.getTips())
                .result(round.getResult())
                .build();
    }
}