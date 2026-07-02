package org.example.userservice.infrastructure.mapper;

import org.example.userservice.application.command.CreateAccountCommand;
import org.example.userservice.application.criteria.AccountSearchCriteria;
import org.example.userservice.domain.model.Account;
import org.example.userservice.domain.valueobject.PhoneNumber;
import org.example.userservice.infrastructure.web.dto.AccountReportFilter;
import org.example.userservice.infrastructure.web.entity.AccountEntity;
import org.example.userservice.infrastructure.web.dto.AccountReportResponsive;
import org.example.userservice.infrastructure.web.dto.CreateAccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface AccountMapperMapstruct {

    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "stringToPhoneNumber")
    Account toDomain(AccountEntity accountEntity);

    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "stringToPhoneNumber")
    Account toDomain(CreateAccountCommand command);

    @Mapping(target = "id", source = "userId")
    CreateAccountCommand toCommand(CreateAccountRequest request);

    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "phoneNumberToString")
    AccountEntity toEntity(Account account);

    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "phoneNumberToString")
    AccountReportResponsive toReportResponse(Account account);

    @Mapping(target = "createdFrom", source = "createdFrom", qualifiedByName = "localDateToInstantStartOfDay")
    @Mapping(target = "createdTo", source = "createdTo", qualifiedByName = "localDateToInstantEndOfDay")
    AccountSearchCriteria toCriteria(AccountReportFilter filter);

    @Named("phoneNumberToString")
    default String phoneNumberToString(PhoneNumber phoneNumber) {
        return phoneNumber != null ? phoneNumber.getValue() : null;
    }

    @Named("stringToPhoneNumber")
    default PhoneNumber stringToPhoneNumber(String phoneNumberRaw) {
        return phoneNumberRaw != null ? new PhoneNumber(phoneNumberRaw) : null;
    }

    @Named("localDateToInstantStartOfDay")
    default Instant localDateToInstantStartOfDay(LocalDate date) {
        return date != null ? date.atStartOfDay(ZoneId.systemDefault()).toInstant() : null;
    }

    @Named("localDateToInstantEndOfDay")
    default Instant localDateToInstantEndOfDay(LocalDate date) {
        return date != null ? date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().minusMillis(1) : null;
    }
}
