package org.example.ticketservice.application.mapper;

import org.example.ticketservice.application.command.SavePromotionRequestCommand;
import org.example.ticketservice.domain.model.PromotionTicket;
import org.example.ticketservice.infrastructure.web.dto.PromotionTicketResponse;
import org.example.ticketservice.infrastructure.web.dto.SavePromotionRequest;
import org.example.ticketservice.infrastructure.web.entity.PromotionTicketEntity;
import org.example.ticketservice.infrastructure.web.entity.TicketEntity;

import java.util.UUID;

public interface TicketMapper {

    TicketEntity toEntity(PromotionTicket domain);

    PromotionTicketEntity toPromotionEntity(PromotionTicket domain, TicketEntity ticketEntity);

    PromotionTicket toDomain(PromotionTicketEntity entity);
    PromotionTicket toDomain(SavePromotionRequestCommand command);

    SavePromotionRequestCommand toCommand(SavePromotionRequest request, UUID userId);

    PromotionTicketResponse  toResponse(PromotionTicket promotionTicket);
}
