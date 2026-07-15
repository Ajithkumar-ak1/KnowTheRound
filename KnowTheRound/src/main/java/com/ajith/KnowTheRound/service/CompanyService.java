package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.dto.company.CompanyRequestDto;
import com.ajith.KnowTheRound.dto.company.CompanyResponseDto;
import com.ajith.KnowTheRound.exception.DuplicateResourceException;
import com.ajith.KnowTheRound.exception.ResourceNotFoundException;
import com.ajith.KnowTheRound.model.Company;
import com.ajith.KnowTheRound.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyResponseDto createCompany(CompanyRequestDto request) {

        if (companyRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateResourceException("Company already exists");
        }

        Company company = Company.builder()
                .name(request.getName())
                .logoUrl(request.getLogoUrl())
                .website(request.getWebsite())
                .build();

        return mapToDto(companyRepository.save(company));
    }

    public List<CompanyResponseDto> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public CompanyResponseDto getCompanyById(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        return mapToDto(company);
    }

    public CompanyResponseDto updateCompany(Long id, CompanyRequestDto request) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        companyRepository.findByName(request.getName())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new DuplicateResourceException("Company already exists");
                    }
                });

        company.setName(request.getName());
        company.setLogoUrl(request.getLogoUrl());
        company.setWebsite(request.getWebsite());

        return mapToDto(companyRepository.save(company));
    }

    public void deleteCompany(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        companyRepository.delete(company);
    }

    private CompanyResponseDto mapToDto(Company company) {
        return CompanyResponseDto.builder()
                .id(company.getId())
                .name(company.getName())
                .logoUrl(company.getLogoUrl())
                .website(company.getWebsite())
                .build();
    }
}