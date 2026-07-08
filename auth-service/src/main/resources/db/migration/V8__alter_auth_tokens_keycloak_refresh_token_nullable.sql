-- V8__alter_auth_tokens_keycloak_refresh_token_nullable.sql
-- Make keycloak_refresh_token nullable — was NOT NULL, now optional
-- Reflects AuthTokenEntity.keycloakRefreshToken having no @Column(nullable = false)

ALTER TABLE auth_tokens
    ALTER COLUMN keycloak_refresh_token DROP NOT NULL;
