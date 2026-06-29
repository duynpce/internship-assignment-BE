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
        RemoteAccountCredentialEntity entity = springDataRepo.findById(domain.getId())
                .orElseGet(() -> {
                    RemoteAccountCredentialEntity e = new RemoteAccountCredentialEntity();
                    e.setId(domain.getId());
                    e.setEmail(domain.getEmail().getValue());
                    e.setRoles(new HashSet<>());
                    return e;
                });

        // Attach role entities from the shared roles table
        Set<RoleEntity> roleEntities = domain.getRoles().stream()
                .map(role -> springDataRoleRepository.findById(role.getId())
                        .orElseThrow(() -> new NotFoundException("Role not found: " + role.getId())))
                .collect(Collectors.toSet());
        entity.setRoles(roleEntities);

        return authMapper.toRemoteDomain(springDataRepo.save(entity));
    }
}

