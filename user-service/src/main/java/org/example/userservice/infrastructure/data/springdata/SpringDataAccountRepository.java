package org.example.userservice.infrastructure.data.springdata;

import org.example.userservice.infrastructure.web.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataAccountRepository extends JpaRepository<AccountEntity, UUID> {
    boolean existsByPhoneNumber(String phoneNumber);
}