package org.example.ticketservice.domain.model;

import org.example.ticketservice.domain.constant.TicketStatus;
import org.example.ticketservice.domain.constant.TicketType;

import java.time.Instant;
import java.util.UUID;

public class Ticket {

    private UUID id;
    private TicketType type;
    private TicketStatus status;
    private Instant createdAt;
    private UUID userId;

    public Ticket() {
        this.status    = TicketStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        if (type == null) {
            throw new IllegalArgumentException("Ticket type cannot be null.");
        }
        this.type = type;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Ticket status cannot be null.");
        }
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }
        this.userId = userId;
    }

    public boolean isPending() {
        return TicketStatus.PENDING.equals(this.status);
    }

    public boolean isApproved() {
        return TicketStatus.APPROVED.equals(this.status);
    }

    public boolean isRejected() {
        return TicketStatus.REJECTED.equals(this.status);
    }

    public void approve() {
        if (!isPending()) {
            throw new IllegalStateException("Only pending tickets can be approved.");
        }
        this.status = TicketStatus.APPROVED;
    }

    public void reject() {
        if (!isPending()) {
            throw new IllegalStateException("Only pending tickets can be rejected.");
        }
        this.status = TicketStatus.REJECTED;
    }
}
