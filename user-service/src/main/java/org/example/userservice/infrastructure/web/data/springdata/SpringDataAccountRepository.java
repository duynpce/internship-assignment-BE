package org.example.userservice.infrastructure.web.data.springdata;

import org.example.userservice.infrastructure.web.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SpringDataAccountRepository extends JpaRepository<AccountEntity, UUID>, JpaSpecificationExecutor<AccountEntity> {
    boolean existsByPhoneNumber(String phoneNumber);
}