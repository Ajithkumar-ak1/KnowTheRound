package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.technology.TechnologyRequestDto;
import com.ajith.KnowTheRound.dto.technology.TechnologyResponseDto;
import com.ajith.KnowTheRound.exception.DuplicateResourceException;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.model.Technology;
import com.ajith.KnowTheRound.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyResponseDto createTechnology(TechnologyRequestDto request) {

        if (technologyRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateResourceException("Technology already exists");
        }

        Technology technology = Technology.builder()
                .name(request.getName())
                .build();

        return mapToDto(technologyRepository.save(technology));
    }

    public List<TechnologyResponseDto> getAllTechnologies() {

        return technologyRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public TechnologyResponseDto getTechnologyById(Long id) {

        Technology technology = technologyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technology not found"));

        return mapToDto(technology);
    }

    public TechnologyResponseDto updateTechnology(Long id, TechnologyRequestDto request) {

        Technology technology = technologyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technology not found"));

        technologyRepository.findByName(request.getName())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new DuplicateResourceException("Technology already exists");
                    }
                });

        technology.setName(request.getName());

        return mapToDto(technologyRepository.save(technology));
    }

    public void deleteTechnology(Long id) {

        Technology technology = technologyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technology not found"));

        technologyRepository.delete(technology);
    }

    private TechnologyResponseDto mapToDto(Technology technology) {

        return TechnologyResponseDto.builder()
                .id(technology.getId())
                .name(technology.getName())
                .build();
    }
}