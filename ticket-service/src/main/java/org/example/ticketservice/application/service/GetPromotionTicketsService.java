package org.example.ticketservice.application.service;

import lombok.RequiredArgsConstructor;
import org.example.ticketservice.application.repository.PromotionTicketRepository;
import org.example.ticketservice.application.usecase.GetPromotionTicketsUseCase;
import org.example.ticketservice.domain.model.PromotionTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetPromotionTicketsService implements GetPromotionTicketsUseCase {

    private final PromotionTicketRepository promotionTicketRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<PromotionTicket> execute(Pageable pageable) {
        return promotionTicketRepository.findAll(pageable);
    }
}
