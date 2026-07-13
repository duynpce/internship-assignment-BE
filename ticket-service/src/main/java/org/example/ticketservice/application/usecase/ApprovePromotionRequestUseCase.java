package org.example.ticketservice.application.usecase;

import org.example.ticketservice.application.command.ApprovePromotionRequestCommand;

public interface ApprovePromotionRequestUseCase {
    void approve(ApprovePromotionRequestCommand command);
}
