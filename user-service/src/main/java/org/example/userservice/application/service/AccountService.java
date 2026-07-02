package org.example.userservice.application.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.application.command.CreateAccountCommand;
import org.example.userservice.application.command.PageCommand;
import org.example.userservice.application.criteria.AccountSearchCriteria;
import org.example.userservice.application.mapper.AccountMapper;
import org.example.userservice.application.repository.AccountRepository;
import org.example.userservice.application.usecase.AccountUseCase;
import org.example.userservice.domain.exception.ConflictException;
import org.example.userservice.domain.model.Account;
import org.example.userservice.infrastructure.web.dto.AccountReportResponsive;
import org.example.userservice.infrastructure.web.dto.MetaDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountUseCase {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public void createAccount(CreateAccountCommand command) {
        if (accountRepository.existsByPhoneNumber(command.phoneNumber())) {
            throw new ConflictException("Phone number already exists: " + command.phoneNumber());
        }

        Account account = accountMapper.toDomain(command);
        accountRepository.save(account);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return accountRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public PageCommand<Account> getAccountReport(AccountSearchCriteria criteria) {
        return accountRepository.search(criteria);
    }
}