-- Allow nullable username and password to support third-party (e.g. Google) authentication.
-- OAuth users have no local password, and username may not be required.

ALTER TABLE account_credentials ALTER COLUMN username DROP NOT NULL;
ALTER TABLE account_credentials ALTER COLUMN password DROP NOT NULL;
