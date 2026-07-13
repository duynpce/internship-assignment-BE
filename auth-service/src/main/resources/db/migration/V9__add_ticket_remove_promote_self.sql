-- =============================================================================
-- V9 : Add TICKET resource + remove PROMOTE self-scoped permissions
-- =============================================================================
-- Context
--   V6 introduced PROMOTE with READ/WRITE in both ALL and SELF scopes.
--   SELF-scoped PROMOTE permissions are removed — only ALL-scoped ones are kept
--   for SUPER_ADMIN and ADMIN.
--   TICKET is introduced as a new resource with READ/WRITE in ALL and SELF.
--
-- Steps
--   1. Insert TICKET permissions (idempotent)
--   2. Assign TICKET permissions to roles
--   3. Remove PROMOTE:READ:SELF and PROMOTE:WRITE:SELF from role_permissions
--   4. Delete the orphaned PROMOTE SELF-scoped permission rows
--   5. Guarantee SUPER_ADMIN and ADMIN have TICKET READ+WRITE / ALL
-- =============================================================================


-- ── Step 1 : Ensure TICKET permissions exist ──────────────────────────────────
INSERT INTO permissions (resource, action, scope) VALUES
    ('TICKET', 'READ',  'ALL'),
    ('TICKET', 'WRITE', 'ALL'),
    ('TICKET', 'READ',  'SELF'),
    ('TICKET', 'WRITE', 'SELF')
ON CONFLICT (resource, action, scope) DO NOTHING;


-- ── Step 2 : Assign TICKET permissions to roles ───────────────────────────────
-- SUPER_ADMIN → all TICKET permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles       r
JOIN   permissions p ON p.resource = 'TICKET'
WHERE  r.name = 'SUPER_ADMIN'
ON CONFLICT DO NOTHING;

-- ADMIN → TICKET READ+WRITE / ALL only
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles       r
JOIN   permissions p ON p.resource = 'TICKET'
                    AND p.scope    = 'ALL'
WHERE  r.name = 'ADMIN'
ON CONFLICT DO NOTHING;

-- CONTRIBUTOR → TICKET READ+WRITE / SELF only
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles       r
JOIN   permissions p ON p.resource = 'TICKET'
                    AND p.scope    = 'SELF'
WHERE  r.name = 'CONTRIBUTOR'
ON CONFLICT DO NOTHING;

-- CUSTOMER → TICKET READ+WRITE / SELF only
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles       r
JOIN   permissions p ON p.resource = 'TICKET'
                    AND p.scope    = 'SELF'
WHERE  r.name = 'CUSTOMER'
ON CONFLICT DO NOTHING;


-- ── Step 3 : Remove PROMOTE SELF-scoped rows from role_permissions ────────────
DELETE FROM role_permissions
WHERE permission_id IN (
    SELECT id FROM permissions
    WHERE  resource = 'PROMOTE'
    AND    scope    = 'SELF'
);


-- ── Step 4 : Delete orphaned PROMOTE SELF-scoped permission rows ──────────────
DELETE FROM permissions
WHERE resource = 'PROMOTE'
AND   scope    = 'SELF';
