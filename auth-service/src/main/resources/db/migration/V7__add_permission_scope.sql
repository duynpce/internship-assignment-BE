-- =============================================================================
-- V7 : Introduce `scope` column into permissions
-- =============================================================================
-- Goal
--   Before : (resource, action)          e.g. ('EXPORT', 'READ_ALL')
--   After  : (resource, action, scope)   e.g. ('EXPORT', 'READ', 'ALL')
--
-- Steps
--   1. Add `scope` column as nullable (required while we backfill)
--   2. Drop the old unique constraint on (resource, action)
--   3. Backfill scope + normalise action for ALL existing rows
--   4. Add the new unique constraint on (resource, action, scope)
--   5. Make scope NOT NULL now that every row has a value
-- =============================================================================


-- ── Step 1 : Add scope column (nullable for now) ──────────────────────────────
ALTER TABLE permissions
    ADD COLUMN IF NOT EXISTS scope VARCHAR(50);


-- ── Step 2 : Drop the old unique constraint ───────────────────────────────────
ALTER TABLE permissions
    DROP CONSTRAINT IF EXISTS uq_permission_res_act;


-- ── Step 3 : Backfill — split baked-in action_scope → (action, scope) ─────────

-- 3a. Rows whose action already ends with _ALL  →  strip suffix, scope = ALL
UPDATE permissions
SET    action = REPLACE(action, '_ALL',  ''),
       scope  = 'ALL'
WHERE  action LIKE '%\_ALL'
  AND  scope IS NULL;

-- 3b. Rows whose action already ends with _SELF →  strip suffix, scope = SELF
UPDATE permissions
SET    action = REPLACE(action, '_SELF', ''),
       scope  = 'SELF'
WHERE  action LIKE '%\_SELF'
  AND  scope IS NULL;

-- 3c. Legacy V2 rows (READ, WRITE, UPDATE, DELETE, MANAGE, BUY, SELL, DOWNLOAD, UPLOAD)
--     These are system-wide operations with no natural SELF scope → scope = ALL
UPDATE permissions
SET    scope = 'ALL'
WHERE  scope IS NULL;


-- ── Step 4 : Add the new unique constraint on (resource, action, scope) ────────
ALTER TABLE permissions
    ADD CONSTRAINT uq_permission_res_act_scope UNIQUE (resource, action, scope);


-- ── Step 5 : Enforce NOT NULL on scope ────────────────────────────────────────
ALTER TABLE permissions
    ALTER COLUMN scope SET NOT NULL;
