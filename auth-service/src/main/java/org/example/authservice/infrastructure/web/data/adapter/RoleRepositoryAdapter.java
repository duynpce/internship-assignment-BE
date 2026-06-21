package org.example.authservice.infrastructure.web.data.adapter;

import lombok.RequiredArgsConstructor;
import org.example.authservice.application.mapper.AuthMapper;
import org.example.authservice.application.repository.RoleRepository;
import org.example.authservice.domain.model.Role;
import org.example.authservice.infrastructure.web.data.springdata.SpringDataRoleRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final SpringDataRoleRepository springDataRepo;
    private final AuthMapper mapper;

    @Override
    public Optional<Role> findByName(String name) {
        return springDataRepo.findByName(name)
                .map(mapper::toDomain);
    }
}