package org.example.userservice.infrastructure.data.adapter;

import lombok.RequiredArgsConstructor;
import org.example.userservice.application.command.PageCommand;
import org.example.userservice.application.criteria.AccountSearchCriteria;
import org.example.userservice.application.mapper.AccountMapper;
import org.example.userservice.application.repository.AccountRepository;
import org.example.userservice.domain.model.Account;
import org.example.userservice.infrastructure.data.specification.AccountSpecification;
import org.example.userservice.infrastructure.data.springdata.SpringDataAccountRepository;
import org.example.userservice.infrastructure.web.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @Override
    public PageCommand<Account> search(AccountSearchCriteria criteria) {
        Pageable pageable = PageRequest.of(criteria.page(), criteria.limit());
        Specification<AccountEntity> spec = AccountSpecification.fromCriteria(criteria);
        Page<AccountEntity> entityPage = springDataAccountRepository.findAll(spec, pageable);

        List<Account> accounts = entityPage.getContent().stream()
                .map(accountMapper::toDomain)
                .toList();

        // This is the only place in the codebase that converts Spring Data's Page
        // into the framework-agnostic PageCommand used by the application layer.
        return PageCommand.of(
                accounts,
                entityPage.getTotalElements(),
                entityPage.getNumber(),
                entityPage.getSize()
        );
    }
}