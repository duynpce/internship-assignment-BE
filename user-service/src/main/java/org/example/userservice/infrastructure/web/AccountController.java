package org.example.userservice.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.application.command.PageCommand;
import org.example.userservice.application.criteria.AccountSearchCriteria;
import org.example.userservice.application.mapper.AccountMapper;
import org.example.userservice.application.usecase.AccountUseCase;
import org.example.userservice.domain.model.Account;
import org.example.userservice.infrastructure.web.dto.AccountReportFilter;
import org.example.userservice.infrastructure.web.dto.AccountReportResponsive;
import org.example.userservice.infrastructure.web.dto.CreateAccountRequest;
import org.example.userservice.infrastructure.web.dto.MetaDto;
import org.example.userservice.infrastructure.web.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountUseCase accountUseCase;
    private final AccountMapper accountMapper;

    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createContributorAccount(@Valid @RequestBody CreateAccountRequest request) {
        accountUseCase.createAccount(accountMapper.toCommand(request));
        return new ResponseEntity<>(ResponseDto.success(null, "Account created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/phone-number/{phoneNumber}")
    public ResponseEntity<ResponseDto<Boolean>> existPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        return ResponseEntity.ok(ResponseDto.success(accountUseCase.existsByPhoneNumber(phoneNumber)));
    }

    /**
     * Filterable, paginated account report.
     * Example: GET /accounts/report?firstName=an&gender=FEMALE&createdFrom=2026-06-01&createdTo=2026-06-30&page=0&limit=20
     */
    @GetMapping("/report")
    @PreAuthorize("hasAuthority('EXPORT:READ_ALL')")
    public ResponseEntity<ResponseDto<List<AccountReportResponsive>>> getAccountReport(
            @Valid @ModelAttribute AccountReportFilter filter) {

        log.info(filter.toString());
        AccountSearchCriteria criteria = accountMapper.toCriteria(filter);

        PageCommand<Account> accountPage = accountUseCase.getAccountReport(criteria);

        List<AccountReportResponsive> data = accountPage.getContent().stream()
                .map(accountMapper::toReportResponse)
                .toList();

        MetaDto metaDto = MetaDto.builder()
                .totalItems(accountPage.getTotalElements())
                .totalPages(accountPage.getTotalPages())
                .paginationDto(filter.getPaginationDto())
                .build();

        return ResponseEntity.ok(ResponseDto.success(data, "Account report fetched successfully", metaDto));
    }

}
