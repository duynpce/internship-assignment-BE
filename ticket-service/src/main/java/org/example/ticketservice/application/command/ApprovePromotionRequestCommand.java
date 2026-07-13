package org.example.ticketservice.application.command;

import java.util.UUID;

public record ApprovePromotionRequestCommand(
        UUID promotionTicketId
) {}
