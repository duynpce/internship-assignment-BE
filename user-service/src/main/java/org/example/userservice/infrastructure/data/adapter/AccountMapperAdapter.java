package org.example.userservice.infrastructure.data.adapter;

import lombok.RequiredArgsConstructor;
import org.example.userservice.application.command.CreateAccountCommand;
import org.example.userservice.application.mapper.AccountMapper;
import org.example.userservice.domain.model.Account;
import org.example.userservice.infrastructure.web.entity.AccountEntity;
import org.example.userservice.infrastructure.mapper.AccountMapperMapstruct;
import org.example.userservice.infrastructure.web.dto.CreateAccountRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapperAdapter implements AccountMapper {
    private final AccountMapperMapstruct accountMapperMapstruct;

    @Override
    public Account toDomain(AccountEntity accountEntity) {
        return accountMapperMapstruct.toDomain(accountEntity);
    }

    @Override
    public Account toDomain(CreateAccountCommand command) {
        return accountMapperMapstruct.toDomain(command);
    }

    @Override
    public AccountEntity toEntity(Account account) {
        return accountMapperMapstruct.toEntity(account);
    }

    @Override
    public CreateAccountCommand toCommand(CreateAccountRequest request) {
        return accountMapperMapstruct.toCommand(request);
    }
}
