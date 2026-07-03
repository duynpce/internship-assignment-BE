package org.example.userservice.application.repository;

import org.example.userservice.application.command.PageCommand;
import org.example.userservice.application.criteria.AccountSearchCriteria;
import org.example.userservice.domain.model.Account;

import java.util.UUID;

public interface AccountRepository {
    void save(Account account);
    Account findById(UUID id);

    boolean existsById(UUID id);
    boolean existsByPhoneNumber(String phoneNumber);

    PageCommand<Account> search(AccountSearchCriteria criteria);
}