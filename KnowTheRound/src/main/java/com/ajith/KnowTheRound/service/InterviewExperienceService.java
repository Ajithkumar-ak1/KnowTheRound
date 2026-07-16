package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.experience.*;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.model.*;
import com.ajith.KnowTheRound.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.ajith.KnowTheRound.mapper.InterviewExperienceMapper;

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
    private final InterviewExperienceMapper interviewExperienceMapper;
    private final BookmarkRepository bookmarkRepository;

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

        return interviewExperienceMapper.toResponse(saved, isBookmarked(saved));
    }

    public List<InterviewExperienceResponse> getAllInterviewExperiences() {

        return interviewExperienceRepository.findAll()
                .stream()
                .map(experience ->
                        interviewExperienceMapper.toResponse(
                                experience,
                                isBookmarked(experience)
                        ))
                .toList();
    }

    public InterviewExperienceResponse getInterviewExperienceById(Long id) {

        InterviewExperience experience = interviewExperienceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Interview Experience not found"));

        return interviewExperienceMapper.toResponse(
                experience,
                isBookmarked(experience)
        );
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

        return interviewExperienceMapper.toResponse(
                saved,
                isBookmarked(saved)
        );
    }

    public void deleteInterviewExperience(Long id) {

        InterviewExperience experience = getOwnedInterviewExperience(id);

        interviewExperienceRepository.delete(experience);
    }

    private boolean isBookmarked(InterviewExperience experience) {

        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof User user)) {
            return false;
        }

        return bookmarkRepository.existsByUserAndInterviewExperience(user, experience);
    }

}