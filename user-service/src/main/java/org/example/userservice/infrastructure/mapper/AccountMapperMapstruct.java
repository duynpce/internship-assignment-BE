package org.example.userservice.infrastructure.mapper;

import org.example.userservice.application.command.CreateAccountCommand;
import org.example.userservice.domain.model.Account;
import org.example.userservice.domain.valueobject.PhoneNumber;
import org.example.userservice.infrastructure.web.entity.AccountEntity;
import org.example.userservice.infrastructure.web.dto.CreateAccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AccountMapperMapstruct {

    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "stringToPhoneNumber")
    Account toDomain (AccountEntity accountEntity);

    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "stringToPhoneNumber")
    Account toDomain (CreateAccountCommand command);

    @Mapping(target = "id", source = "userId")
    CreateAccountCommand toCommand (CreateAccountRequest  request);

    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "phoneNumberToString")
    AccountEntity toEntity(Account account);

    @Named("phoneNumberToString")
    default String phoneNumberToString(PhoneNumber phoneNumber) {
        return phoneNumber != null ? phoneNumber.getValue() : null;
    }

    @Named("stringToPhoneNumber")
    default PhoneNumber stringToPhoneNumber(String phoneNumberRaw) {
        return phoneNumberRaw != null ? new PhoneNumber(phoneNumberRaw) : null;
    }
}
