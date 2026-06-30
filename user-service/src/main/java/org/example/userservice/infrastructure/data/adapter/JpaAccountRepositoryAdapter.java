package org.example.userservice.infrastructure.data.adapter;

import lombok.RequiredArgsConstructor;
import org.example.userservice.application.mapper.AccountMapper;
import org.example.userservice.domain.model.Account;
import org.example.userservice.application.repository.AccountRepository;
import org.example.userservice.infrastructure.data.springdata.SpringDataAccountRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaAccountRepositoryAdapter implements AccountRepository {

    private final SpringDataAccountRepository springDataAccountRepository;
    private final AccountMapper accountMapper;

    @Override
    public void save(Account account) {
        springDataAccountRepository.save(accountMapper.toEntity(account));
    }

    @Override
    public Account findById(UUID id) {
        return accountMapper.toDomain(springDataAccountRepository.findById(id).orElse(null));
    }

    @Override
    public boolean existsById(UUID id) {
        return springDataAccountRepository.existsById(id);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return springDataAccountRepository.existsByPhoneNumber(phoneNumber);
    }

}