package org.example.ticketservice.infrastructure.mapper.adapter;

import lombok.RequiredArgsConstructor;
import org.example.ticketservice.application.command.SavePromotionRequestCommand;
import org.example.ticketservice.application.mapper.TicketMapper;
import org.example.ticketservice.domain.model.PromotionTicket;
import org.example.ticketservice.infrastructure.mapper.mapstruct.TicketMapperMapstruct;
import org.example.ticketservice.infrastructure.web.dto.PromotionTicketResponse;
import org.example.ticketservice.infrastructure.web.dto.SavePromotionRequest;
import org.example.ticketservice.infrastructure.web.entity.PromotionTicketEntity;
import org.example.ticketservice.infrastructure.web.entity.TicketEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TicketMapperAdapter implements TicketMapper {

    private final TicketMapperMapstruct mapstructMapper;

    @Override
    public TicketEntity toEntity(PromotionTicket domain) {
        if (domain == null) {
            return null;
        }
        return mapstructMapper.toTicketEntity(domain);
    }

    @Override
    public PromotionTicketEntity toPromotionEntity(PromotionTicket domain, TicketEntity ticketEntity) {
        if (domain == null || ticketEntity == null) {
            return null;
        }
        return mapstructMapper.toPromotionTicketEntity(domain, ticketEntity);
    }

    @Override
    public PromotionTicket toDomain(PromotionTicketEntity entity) {
        if (entity == null) {
            return null;
        }
        return mapstructMapper.toDomain(entity);
    }

    @Override
    public PromotionTicket toDomain(SavePromotionRequestCommand command) {
        if (command == null) {
            return null;
        }
        return mapstructMapper.toDomain(command);
    }

    @Override
    public SavePromotionRequestCommand toCommand(SavePromotionRequest request, UUID userId) {
        if (request == null) {
            return null;
        }
        return mapstructMapper.toCommand(request, userId);
    }

    @Override
    public PromotionTicketResponse toResponse(PromotionTicket promotionTicket) {
        if (promotionTicket == null) {
            return null;
        }
        return mapstructMapper.toResponse(promotionTicket);
    }


}
