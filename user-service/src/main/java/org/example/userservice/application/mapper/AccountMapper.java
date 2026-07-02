package org.example.userservice.application.mapper;

import org.example.userservice.application.command.CreateAccountCommand;
import org.example.userservice.application.criteria.AccountSearchCriteria;
import org.example.userservice.domain.model.Account;
import org.example.userservice.infrastructure.web.entity.AccountEntity;
import org.example.userservice.infrastructure.web.dto.AccountReportFilter;
import org.example.userservice.infrastructure.web.dto.AccountReportResponsive;
import org.example.userservice.infrastructure.web.dto.CreateAccountRequest;

public interface AccountMapper {
    Account toDomain(AccountEntity accountEntity);
    Account toDomain(CreateAccountCommand command);
    CreateAccountCommand toCommand(CreateAccountRequest request);
    AccountEntity toEntity(Account account);
    AccountReportResponsive toReportResponse(Account account);
    AccountSearchCriteria toCriteria(AccountReportFilter filter);
}
