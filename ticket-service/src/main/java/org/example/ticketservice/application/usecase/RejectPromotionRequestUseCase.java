package org.example.ticketservice.application.usecase;

import org.example.ticketservice.application.command.RejectPromotionRequestCommand;

public interface RejectPromotionRequestUseCase {
    void reject(RejectPromotionRequestCommand command);
}
