package org.example.userservice.application.repository;

import org.example.userservice.domain.model.Account;

import java.util.UUID;

public interface AccountRepository {
    void save(Account account);
    Account findById(UUID id);

    boolean existsById(UUID id);
    boolean existsByPhoneNumber(String phoneNumber);
}