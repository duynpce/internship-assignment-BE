-- ============================================================
-- V1__create_ticket_tables.sql
-- Creates the base ticket table and promotion_tickets subtable
-- using JOINED inheritance strategy (mirrors JPA entity design)
-- ============================================================

-- ------------------------------------------------------------
-- ENUM TYPES
-- ------------------------------------------------------------

-- ------------------------------------------------------------
-- TABLE: tickets  (parent)
-- Maps to: TicketEntity
-- ------------------------------------------------------------
CREATE TABLE tickets
(
    id         UUID         NOT NULL DEFAULT gen_random_uuid(),
    type       VARCHAR(255)  NOT NULL,
    status     VARCHAR(255)  NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now(),
    user_id    UUID         NOT NULL,

    CONSTRAINT pk_tickets PRIMARY KEY (id)
);

CREATE INDEX idx_tickets_user_id ON tickets (user_id);
CREATE INDEX idx_tickets_status  ON tickets (status);
CREATE INDEX idx_tickets_type    ON tickets (type);

-- ------------------------------------------------------------
-- TABLE: promotion_tickets  (child — JOINED)
-- Maps to: PromotionTicketEntity
-- ticket_id is both PK and FK → tickets.id
-- ------------------------------------------------------------
CREATE TABLE promotion_tickets
(
    ticket_id            UUID         NOT NULL,
    identity_card_number VARCHAR(50)  NOT NULL,
    bank_name            VARCHAR(100) NOT NULL,
    bank_account_number  VARCHAR(50)  NOT NULL,
    shop_name            VARCHAR(100) NOT NULL,
    delivery_address     TEXT         NOT NULL,
    tax_id               VARCHAR(50)  NOT NULL,

    CONSTRAINT pk_promotion_tickets
        PRIMARY KEY (ticket_id),

    CONSTRAINT fk_promotion_tickets_ticket
        FOREIGN KEY (ticket_id)
        REFERENCES tickets (id)
        ON DELETE CASCADE,

    CONSTRAINT uq_promotion_identity_card
        UNIQUE (identity_card_number),

    CONSTRAINT uq_promotion_bank_account
        UNIQUE (bank_account_number),

    CONSTRAINT uq_promotion_shop_name
        UNIQUE (shop_name),

    CONSTRAINT uq_promotion_tax_id
        UNIQUE (tax_id)
);
