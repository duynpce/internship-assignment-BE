package org.example.ticketservice.infrastructure.web.data.springdata;

import org.example.ticketservice.infrastructure.web.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataTicketRepository extends JpaRepository<TicketEntity, UUID> {
}
