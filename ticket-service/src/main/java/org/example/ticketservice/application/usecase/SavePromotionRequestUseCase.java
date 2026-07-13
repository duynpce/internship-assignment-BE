package org.example.ticketservice.application.usecase;

import org.example.ticketservice.application.command.SavePromotionRequestCommand;

public interface SavePromotionRequestUseCase {
    void execute(SavePromotionRequestCommand command);
}
