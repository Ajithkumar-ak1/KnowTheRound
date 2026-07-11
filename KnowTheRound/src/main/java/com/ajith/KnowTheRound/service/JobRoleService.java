package com.ajith.KnowTheRound.service;

import com.ajith.KnowTheRound.repository.JobRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobRoleService {
    private final JobRoleRepository jobRoleRepository;
}
