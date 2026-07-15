package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.experience.*;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.model.*;
import com.ajith.KnowTheRound.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewExperienceService {

    private final InterviewExperienceRepository interviewExperienceRepository;
    private final CompanyRepository companyRepository;
    private final TechnologyRepository technologyRepository;
    private final JobRoleRepository jobRoleRepository;
    private final UserRepository userRepository;

    public InterviewExperienceResponse createInterviewExperience(CreateInterviewExperienceRequest request) {

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        JobRole jobRole = jobRoleRepository.findById(request.getJobRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Job Role not found"));

        List<Technology> technologies =
                technologyRepository.findAllById(request.getTechnologyIds());

        if (technologies.size() != request.getTechnologyIds().stream().distinct().count()) {
            throw new ResourceNotFoundException("One or more technologies not found.");
        }

        InterviewExperience experience = new InterviewExperience();

        experience.setTitle(request.getTitle());
        experience.setOverallExperience(request.getOverallExperience());
        experience.setPreparationStrategy(request.getPreparationStrategy());
        experience.setDifficulty(request.getDifficulty());
        experience.setResult(request.getResult());
        experience.setCgpa(request.getCgpa());
        experience.setYearsOfExperience(request.getYearsOfExperience());
        experience.setPackageOffered(request.getPackageOffered());
        experience.setLocation(request.getLocation());

        experience.setUser(user);
        experience.setCompany(company);
        experience.setJobRole(jobRole);
        experience.setTechnologies(technologies);

        List<InterviewRound> rounds = new ArrayList<>();

        if (request.getInterviewRounds() != null) {

            for (InterviewRoundRequest roundRequest : request.getInterviewRounds()) {

                InterviewRound round = new InterviewRound();

                round.setRoundNumber(roundRequest.getRoundNumber());
                round.setRoundName(roundRequest.getRoundName());
                round.setDifficulty(roundRequest.getDifficulty());
                round.setQuestionsAsked(roundRequest.getQuestionsAsked());
                round.setTips(roundRequest.getTips());
                round.setResult(roundRequest.getResult());

                round.setInterviewExperience(experience);

                rounds.add(round);
            }
        }

        experience.setInterviewRounds(rounds);

        InterviewExperience saved = interviewExperienceRepository.save(experience);

        return mapToResponse(saved);
    }

    public List<InterviewExperienceResponse> getAllInterviewExperiences() {

        return interviewExperienceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public InterviewExperienceResponse getInterviewExperienceById(Long id) {

        InterviewExperience experience = interviewExperienceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Interview Experience not found"));

        return mapToResponse(experience);
    }

    private InterviewExperienceResponse mapToResponse(InterviewExperience experience) {

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

    private InterviewExperience getOwnedInterviewExperience(Long id) {

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        InterviewExperience experience = interviewExperienceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Interview Experience not found"));

        if (!experience.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to modify this interview experience.");
        }

        return experience;
    }

    public InterviewExperienceResponse updateInterviewExperience(
            Long id,
            UpdateInterviewExperienceRequest request) {

        InterviewExperience experience = getOwnedInterviewExperience(id);

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company not found"));

        JobRole jobRole = jobRoleRepository.findById(request.getJobRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job Role not found"));

        List<Technology> technologies =
                technologyRepository.findAllById(request.getTechnologyIds());

        if (technologies.size() != request.getTechnologyIds().size()) {
            throw new ResourceNotFoundException("One or more technologies not found");
        }

        experience.setTitle(request.getTitle());
        experience.setOverallExperience(request.getOverallExperience());
        experience.setPreparationStrategy(request.getPreparationStrategy());
        experience.setDifficulty(request.getDifficulty());
        experience.setResult(request.getResult());
        experience.setCgpa(request.getCgpa());
        experience.setYearsOfExperience(request.getYearsOfExperience());
        experience.setPackageOffered(request.getPackageOffered());
        experience.setLocation(request.getLocation());

        experience.setCompany(company);
        experience.setJobRole(jobRole);

        experience.getTechnologies().clear();
        experience.getTechnologies().addAll(technologies);

        experience.getInterviewRounds().clear();

        if (request.getInterviewRounds() != null) {

            for (InterviewRoundRequest roundRequest : request.getInterviewRounds()) {

                InterviewRound round = new InterviewRound();

                round.setRoundNumber(roundRequest.getRoundNumber());
                round.setRoundName(roundRequest.getRoundName());
                round.setDifficulty(roundRequest.getDifficulty());
                round.setQuestionsAsked(roundRequest.getQuestionsAsked());
                round.setTips(roundRequest.getTips());
                round.setResult(roundRequest.getResult());

                round.setInterviewExperience(experience);

                experience.getInterviewRounds().add(round);
            }
        }

        InterviewExperience saved =
                interviewExperienceRepository.save(experience);

        return mapToResponse(saved);
    }

    public void deleteInterviewExperience(Long id) {

        InterviewExperience experience = getOwnedInterviewExperience(id);

        interviewExperienceRepository.delete(experience);
    }

}