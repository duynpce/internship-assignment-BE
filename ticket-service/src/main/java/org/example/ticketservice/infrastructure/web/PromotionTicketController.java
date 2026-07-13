package org.example.ticketservice.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ticketservice.application.client.TokenGeneratorClient;
import org.example.ticketservice.application.command.ApprovePromotionRequestCommand;
import org.example.ticketservice.application.command.RejectPromotionRequestCommand;
import org.example.ticketservice.application.mapper.TicketMapper;
import org.example.ticketservice.application.usecase.ApprovePromotionRequestUseCase;
import org.example.ticketservice.application.usecase.GetPromotionTicketsUseCase;
import org.example.ticketservice.application.usecase.RejectPromotionRequestUseCase;
import org.example.ticketservice.application.usecase.SavePromotionRequestUseCase;
import org.example.ticketservice.domain.exception.UnauthorizedException;
import org.example.ticketservice.domain.model.PromotionTicket;
import org.example.ticketservice.infrastructure.web.dto.MetaDto;
import org.example.ticketservice.infrastructure.web.dto.PaginationDto;
import org.example.ticketservice.infrastructure.web.dto.PromotionTicketResponse;
import org.example.ticketservice.infrastructure.web.dto.ResponseDto;
import org.example.ticketservice.infrastructure.web.dto.SavePromotionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionTicketController {

    private final SavePromotionRequestUseCase    savePromotionRequestUseCase;
    private final GetPromotionTicketsUseCase     getPromotionTicketsUseCase;
    private final ApprovePromotionRequestUseCase approvePromotionRequestUseCase;
    private final RejectPromotionRequestUseCase  rejectPromotionRequestUseCase;
    private final TokenGeneratorClient           tokenGeneratorClient;
    private final TicketMapper                   ticketMapper;

    @PreAuthorize("hasAuthority('TICKET:WRITE_SELF')")
    @PostMapping
    public ResponseEntity<Void> save(
            @Valid @RequestBody SavePromotionRequest request,
            @CookieValue(value = "accessToken", required = false) String accessToken
    ) {
        if (accessToken == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        UUID userId = tokenGeneratorClient.extractUserIdFromAccessToken(accessToken);
        savePromotionRequestUseCase.execute(ticketMapper.toCommand(request, userId));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAuthority('TICKET:READ_ALL')")
    @GetMapping
    public ResponseEntity<ResponseDto<List<PromotionTicketResponse>>> getAll(
            @Valid PaginationDto pagination
    ) {
        Page<PromotionTicket> page = getPromotionTicketsUseCase.execute(
                PageRequest.of(pagination.getPage(), pagination.getLimit(),
                               Sort.by("ticket.createdAt").descending())
        );

        List<PromotionTicketResponse> data = page.getContent()
                .stream()
                .map(ticketMapper::toResponse)
                .toList();

        MetaDto meta = MetaDto.builder()
                .paginationDto(pagination)
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return ResponseEntity.ok(ResponseDto.success(data, "Fetched successfully", meta));
    }

    @PreAuthorize("hasAuthority('TICKET:WRITE_ALL')")
    @PostMapping("/approve/{promotionTicketId}")
    public ResponseEntity<Void> approve(@PathVariable UUID promotionTicketId) {
        approvePromotionRequestUseCase.approve(new ApprovePromotionRequestCommand(promotionTicketId));
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('TICKET:WRITE_ALL')")
    @PostMapping("/reject/{promotionTicketId}")
    public ResponseEntity<Void> reject(@PathVariable UUID promotionTicketId) {
        rejectPromotionRequestUseCase.reject(new RejectPromotionRequestCommand(promotionTicketId));
        return ResponseEntity.ok().build();
    }
}
