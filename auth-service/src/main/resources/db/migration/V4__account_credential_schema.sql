-- Align account_credentials with AccountCredentialEntity constraints.
-- V2 allowed nullable username, while the entity requires username/email/password non-null.

-- Backfill missing or blank usernames with deterministic values.
UPDATE account_credentials
SET username = 'user_' || substring(replace(id::text, '-', '') from 1 for 12)
WHERE username IS NULL OR btrim(username) = '';

-- Normalize leading/trailing spaces to reduce accidental mismatches.
UPDATE account_credentials
SET username = btrim(username),
    email = btrim(email)
WHERE username <> btrim(username)
   OR email <> btrim(email);

-- Enforce non-null constraints expected by JPA validation.
ALTER TABLE account_credentials ALTER COLUMN username SET NOT NULL;
ALTER TABLE account_credentials ALTER COLUMN email SET NOT NULL;
ALTER TABLE account_credentials ALTER COLUMN password SET NOT NULL;

-- Ensure expected unique constraints exist (safe for environments with drift).
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uq_account_username'
          AND conrelid = 'account_credentials'::regclass
    ) THEN
        ALTER TABLE account_credentials
            ADD CONSTRAINT uq_account_username UNIQUE (username);
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uq_account_email'
          AND conrelid = 'account_credentials'::regclass
    ) THEN
        ALTER TABLE account_credentials
            ADD CONSTRAINT uq_account_email UNIQUE (email);
    END IF;
END $$;

