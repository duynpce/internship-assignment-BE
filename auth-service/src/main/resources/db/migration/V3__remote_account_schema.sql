-- ── Drop FK constraint so auth_tokens can hold both local and remote user IDs ──
ALTER TABLE auth_tokens DROP CONSTRAINT IF EXISTS fk_at_account;

-- ── remote_account_credentials ─────────────────────────────────────────────────
-- id is the Keycloak sub UUID (no auto-generation)
CREATE TABLE IF NOT EXISTS remote_account_credentials (
    id    UUID         NOT NULL,
    email VARCHAR(255) NOT NULL,

    CONSTRAINT pk_remote_account_credentials PRIMARY KEY (id),
    CONSTRAINT uq_remote_account_email       UNIQUE      (email)
);

-- ── remote_account_roles (M2M: remote_account_credentials ↔ roles) ────────────
CREATE TABLE IF NOT EXISTS remote_account_roles (
    account_id UUID NOT NULL,
    role_id    UUID NOT NULL,

    CONSTRAINT pk_remote_account_roles PRIMARY KEY (account_id, role_id),
    CONSTRAINT fk_rar_account FOREIGN KEY (account_id) REFERENCES remote_account_credentials(id) ON DELETE CASCADE,
    CONSTRAINT fk_rar_role    FOREIGN KEY (role_id)    REFERENCES roles(id)                      ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_remote_account_roles_account ON remote_account_roles (account_id);

