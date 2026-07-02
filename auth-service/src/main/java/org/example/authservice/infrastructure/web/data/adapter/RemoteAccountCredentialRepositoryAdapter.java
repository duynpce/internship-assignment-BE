package org.example.authservice.infrastructure.web.data.adapter;

import lombok.RequiredArgsConstructor;
import org.example.authservice.application.mapper.AuthMapper;
import org.example.authservice.application.repository.RemoteAccountCredentialRepository;
import org.example.authservice.domain.exception.NotFoundException;
import org.example.authservice.domain.model.RemoteAccountCredential;
import org.example.authservice.infrastructure.web.data.springdata.SpringDataRemoteAccountCredentialRepository;
import org.example.authservice.infrastructure.web.data.springdata.SpringDataRoleRepository;
import org.example.authservice.infrastructure.web.entity.RemoteAccountCredentialEntity;
import org.example.authservice.infrastructure.web.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RemoteAccountCredentialRepositoryAdapter implements RemoteAccountCredentialRepository {

    private final SpringDataRemoteAccountCredentialRepository springDataRepo;
    private final SpringDataRoleRepository springDataRoleRepository;
    private final AuthMapper authMapper;

    @Override
    public Optional<RemoteAccountCredential> findById(UUID id) {
        return springDataRepo.findById(id).map(authMapper::toRemoteDomain);
    }

    @Override
    public Optional<RemoteAccountCredential> findByIdWithRoles(UUID id) {
        return springDataRepo.findWithRolesById(id).map(authMapper::toRemoteDomain);
    }

    @Override
    public RemoteAccountCredential save(RemoteAccountCredential domain) {
        RemoteAccountCredentialEntity entity = authMapper.toEntity(domain);

        return authMapper.toRemoteDomain(springDataRepo.save(entity));
    }
}

