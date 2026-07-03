-- ── Drop remote_account_credentials and its M2M join table ───────────────────
DROP TABLE IF EXISTS remote_account_roles;
DROP TABLE IF EXISTS remote_account_credentials;

-- ── Add keycloak_id column to account_credentials ────────────────────────────
ALTER TABLE account_credentials
    ADD COLUMN IF NOT EXISTS keycloak_id UUID;

ALTER TABLE account_credentials
    ADD CONSTRAINT uq_account_keycloak_id UNIQUE (keycloak_id);
