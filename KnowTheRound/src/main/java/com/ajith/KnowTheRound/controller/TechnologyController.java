package com.ajith.KnowTheRound.controller;

import com.ajith.KnowTheRound.dto.technology.TechnologyRequestDto;
import com.ajith.KnowTheRound.dto.technology.TechnologyResponseDto;
import com.ajith.KnowTheRound.service.TechnologyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
@RequiredArgsConstructor
public class TechnologyController {

    private final TechnologyService technologyService;

    @GetMapping
    public ResponseEntity<List<TechnologyResponseDto>> getAllTechnologies() {
        return ResponseEntity.ok(technologyService.getAllTechnologies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnologyResponseDto> getTechnologyById(@PathVariable Long id) {
        return ResponseEntity.ok(technologyService.getTechnologyById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TechnologyResponseDto> createTechnology(
            @Valid @RequestBody TechnologyRequestDto request) {

        return new ResponseEntity<>(
                technologyService.createTechnology(request),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TechnologyResponseDto> updateTechnology(
            @PathVariable Long id,
            @Valid @RequestBody TechnologyRequestDto request) {

        return ResponseEntity.ok(
                technologyService.updateTechnology(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTechnology(@PathVariable Long id) {

        technologyService.deleteTechnology(id);
        return ResponseEntity.noContent().build();
    }
}