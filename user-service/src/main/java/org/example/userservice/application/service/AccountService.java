package org.example.userservice.application.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.application.command.CreateAccountCommand;
import org.example.userservice.application.mapper.AccountMapper; 
import org.example.userservice.application.repository.AccountRepository; 
import org.example.userservice.application.usecase.AccountUseCase;
import org.example.userservice.domain.exception.ConflictException;
import org.example.userservice.domain.model.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}