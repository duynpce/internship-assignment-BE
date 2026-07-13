package org.example.ticketservice.application.command;

import java.util.UUID;

public record RejectPromotionRequestCommand(
        UUID promotionTicketId
) {}
