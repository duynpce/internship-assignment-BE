package org.example.userservice.application.usecase;

import org.example.userservice.application.command.CreateAccountCommand;
import org.example.userservice.application.command.PageCommand;
import org.example.userservice.application.criteria.AccountSearchCriteria;
import org.example.userservice.domain.model.Account;

public interface AccountUseCase {
    void createAccount(CreateAccountCommand command);

    boolean existsByPhoneNumber(String phoneNumber);

    PageCommand<Account> getAccountReport(AccountSearchCriteria criteria);
}