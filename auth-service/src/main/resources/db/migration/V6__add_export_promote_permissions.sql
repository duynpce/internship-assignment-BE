-- =============================================================================
-- V6 : Add EXPORT and PROMOTE permissions
-- =============================================================================
-- New permissions
--   EXPORT   : READ_ALL, DOWNLOAD_ALL           → SUPER_ADMIN, ADMIN
--   EXPORT   : READ_SELF, DOWNLOAD_SELF         → SUPER_ADMIN, CONTRIBUTOR, CUSTOMER
--   PROMOTE  : READ_ALL, WRITE_ALL              → SUPER_ADMIN, ADMIN
--   PROMOTE  : WRITE_SELF, READ_SELF            → SUPER_ADMIN, CONTRIBUTOR, CUSTOMER
-- =============================================================================

-- ── 1. Insert new permissions ─────────────────────────────────────────────────
INSERT INTO permissions (resource, action) VALUES
                                               ('EXPORT',  'READ_ALL'),
                                               ('EXPORT',  'DOWNLOAD_ALL'),
                                               ('EXPORT',  'READ_SELF'),
                                               ('EXPORT',  'DOWNLOAD_SELF'),
                                               ('PROMOTE', 'READ_ALL'),
                                               ('PROMOTE', 'WRITE_ALL'),
                                               ('PROMOTE', 'WRITE_SELF'),
                                               ('PROMOTE', 'READ_SELF')
    ON CONFLICT (resource, action) DO NOTHING;

-- ── 2. SUPER_ADMIN → all new permissions ─────────────────────────────────────
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles       r
           JOIN   permissions p ON (p.resource, p.action) IN (
                                                              ('EXPORT',  'READ_ALL'),
                                                              ('EXPORT',  'DOWNLOAD_ALL'),
                                                              ('EXPORT',  'READ_SELF'),
                                                              ('EXPORT',  'DOWNLOAD_SELF'),
                                                              ('PROMOTE', 'READ_ALL'),
                                                              ('PROMOTE', 'WRITE_ALL'),
                                                              ('PROMOTE', 'WRITE_SELF'),
                                                              ('PROMOTE', 'READ_SELF')
    )
WHERE  r.name = 'SUPER_ADMIN'
    ON CONFLICT DO NOTHING;

-- ── 3. ADMIN → all-scoped EXPORT + PROMOTE only ───────────────────────────────
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles       r
           JOIN   permissions p ON (p.resource, p.action) IN (
                                                              ('EXPORT',  'READ_ALL'),
                                                              ('EXPORT',  'DOWNLOAD_ALL'),
                                                              ('PROMOTE', 'READ_ALL'),
                                                              ('PROMOTE', 'WRITE_ALL')
    )
WHERE  r.name = 'ADMIN'
    ON CONFLICT DO NOTHING;

-- ── 4. CONTRIBUTOR → self-scoped EXPORT + PROMOTE ────────────────────────────
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles       r
           JOIN   permissions p ON (p.resource, p.action) IN (
                                                              ('EXPORT',  'READ_SELF'),
                                                              ('EXPORT',  'DOWNLOAD_SELF'),
                                                              ('PROMOTE', 'WRITE_SELF'),
                                                              ('PROMOTE', 'READ_SELF')
    )
WHERE  r.name = 'CONTRIBUTOR'
    ON CONFLICT DO NOTHING;

-- ── 5. CUSTOMER → self-scoped EXPORT + PROMOTE ───────────────────────────────
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles       r
           JOIN   permissions p ON (p.resource, p.action) IN (
                                                              ('EXPORT',  'READ_SELF'),
                                                              ('EXPORT',  'DOWNLOAD_SELF'),
                                                              ('PROMOTE', 'WRITE_SELF'),
                                                              ('PROMOTE', 'READ_SELF')
    )
WHERE  r.name = 'CUSTOMER'
    ON CONFLICT DO NOTHING;