package org.example.userservice.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.userservice.application.usecase.AccountUseCase;
import org.example.userservice.infrastructure.mapper.AccountMapperMapstruct;
import org.example.userservice.infrastructure.web.dto.CreateAccountRequest;
import org.example.userservice.infrastructure.web.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountUseCase accountUseCase;
    private final AccountMapperMapstruct accountMapperMapstruct;

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createContributorAccount(@Valid @RequestBody CreateAccountRequest request) {
        accountUseCase.createAccount(accountMapperMapstruct.toCommand(request));
        return new ResponseEntity<>(ResponseDto.success(null, "Account created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/phone-number/{phoneNumber}")
    public ResponseEntity<ResponseDto<Boolean>> existPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        return ResponseEntity.ok(ResponseDto.success(accountUseCase.existsByPhoneNumber(phoneNumber)));
    }

}
