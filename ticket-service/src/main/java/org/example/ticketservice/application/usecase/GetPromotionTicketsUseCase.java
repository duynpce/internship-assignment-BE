package org.example.ticketservice.application.usecase;

import org.example.ticketservice.domain.model.PromotionTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetPromotionTicketsUseCase {
    Page<PromotionTicket> execute(Pageable pageable);
}
