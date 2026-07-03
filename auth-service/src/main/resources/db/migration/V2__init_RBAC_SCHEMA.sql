
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- ── 2. permissions ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS permissions (
                                           id       UUID         NOT NULL DEFAULT gen_random_uuid(),
    resource VARCHAR(100) NOT NULL,   -- USER | ORDER | PRODUCT | ROLE …
    action   VARCHAR(50)  NOT NULL,   -- READ | WRITE | UPDATE | DELETE | MANAGE | BUY | SELL

    CONSTRAINT pk_permissions        PRIMARY KEY (id),
    CONSTRAINT uq_permission_res_act UNIQUE (resource, action)
    );

-- ── 3. roles (UUID – thay thế bảng BIGSERIAL từ V1) ──────────────────────────
CREATE TABLE IF NOT EXISTS roles (
                                     id   UUID         NOT NULL DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,

    CONSTRAINT pk_roles     PRIMARY KEY (id),
    CONSTRAINT uq_role_name UNIQUE (name)
    );

-- ── 4. role_permissions (M2M: roles ↔ permissions) ───────────────────────────
CREATE TABLE IF NOT EXISTS role_permissions (
                                                role_id       UUID NOT NULL,
                                                permission_id UUID NOT NULL,

                                                CONSTRAINT pk_role_permissions PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_rp_role          FOREIGN KEY (role_id)       REFERENCES roles(id)       ON DELETE CASCADE,
    CONSTRAINT fk_rp_permission    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
    );

-- ── 5. account_credentials (thay thế bảng users từ V1) ───────────────────────
-- AccountStatus enum: ACTIVE | INACTIVE | BANNED | …  (map @Enumerated(STRING))
CREATE TABLE IF NOT EXISTS account_credentials (
                                                   id         UUID         NOT NULL DEFAULT gen_random_uuid(),
    username   VARCHAR(255),
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    status     VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE',  -- AccountStatus enum
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_account_credentials PRIMARY KEY (id),
    CONSTRAINT uq_account_username    UNIQUE (username),
    CONSTRAINT uq_account_email       UNIQUE (email)
    );

-- ── 6. account_roles (M2M: account_credentials ↔ roles) ──────────────────────
CREATE TABLE IF NOT EXISTS account_roles (
                                             account_id UUID NOT NULL,
                                             role_id    UUID NOT NULL,

                                             CONSTRAINT pk_account_roles PRIMARY KEY (account_id, role_id),
    CONSTRAINT fk_ar_account    FOREIGN KEY (account_id) REFERENCES account_credentials(id) ON DELETE CASCADE,
    CONSTRAINT fk_ar_role       FOREIGN KEY (role_id)    REFERENCES roles(id)               ON DELETE CASCADE
    );

-- ── 7. auth_tokens ────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS auth_tokens (
                                           user_id                UUID        NOT NULL,
                                           auth_refresh_token     TEXT        NOT NULL,
                                           keycloak_refresh_token TEXT        NOT NULL,
                                           session_expire_at      TIMESTAMPTZ NOT NULL,

                                           CONSTRAINT pk_auth_tokens PRIMARY KEY (user_id),
    CONSTRAINT fk_at_account  FOREIGN KEY (user_id) REFERENCES account_credentials(id) ON DELETE CASCADE
    );

-- =============================================================================
-- Indexes
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_auth_tokens_expire    ON auth_tokens     (session_expire_at);
CREATE INDEX IF NOT EXISTS idx_account_roles_account ON account_roles   (account_id);
CREATE INDEX IF NOT EXISTS idx_account_roles_role    ON account_roles   (role_id);
CREATE INDEX IF NOT EXISTS idx_role_permissions_role ON role_permissions (role_id);

-- =============================================================================
-- Seed data
-- =============================================================================

INSERT INTO permissions (resource, action) VALUES
                                               ('USER',       'READ'),
                                               ('USER',       'WRITE'),
                                               ('USER',       'UPDATE'),
                                               ('USER',       'DELETE'),
                                               ('ROLE',       'READ'),
                                               ('ROLE',       'MANAGE'),
                                               ('PERMISSION', 'READ'),
                                               ('PERMISSION', 'MANAGE'),
                                               ('ORDER',      'READ'),
                                               ('ORDER',      'MANAGE'),
                                               ('PRODUCT',    'READ'),
                                               ('PRODUCT',    'MANAGE'),
                                               ('PRODUCT',    'BUY'),
                                               ('PRODUCT',    'SELL')
    ON CONFLICT (resource, action) DO NOTHING;

INSERT INTO roles (name) VALUES
                             ('SUPER_ADMIN'),
                             ('ADMIN'),
                             ('CONTRIBUTOR'),
                             ('CUSTOMER')
    ON CONFLICT (name) DO NOTHING;

-- SUPER_ADMIN → toàn bộ permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles r, permissions p
WHERE  r.name = 'SUPER_ADMIN'
    ON CONFLICT DO NOTHING;

-- ADMIN → Quản lý hệ thống, KHÔNG có BUY / SELL
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles r
           JOIN   permissions p ON (p.resource, p.action) IN (
                                                              ('USER',    'READ'),
                                                              ('USER',    'WRITE'),
                                                              ('USER',    'UPDATE'),
                                                              ('ORDER',   'READ'),
                                                              ('ORDER',   'MANAGE'),
                                                              ('PRODUCT', 'READ'),
                                                              ('PRODUCT', 'MANAGE')
    )
WHERE  r.name = 'ADMIN'
    ON CONFLICT DO NOTHING;

-- CONTRIBUTOR → Tạo & bán sản phẩm, có thể mua
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles r
           JOIN   permissions p ON (p.resource, p.action) IN (
                                                              ('USER',    'READ'),
                                                              ('PRODUCT', 'READ'),
                                                              ('PRODUCT', 'BUY'),
                                                              ('PRODUCT', 'SELL')
    )
WHERE  r.name = 'CONTRIBUTOR'
    ON CONFLICT DO NOTHING;

-- CUSTOMER → Tài khoản thông thường, chỉ mua được
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM   roles r
           JOIN   permissions p ON (p.resource, p.action) IN (
                                                              ('USER',    'READ'),
                                                              ('ORDER',   'READ'),
                                                              ('PRODUCT', 'READ'),
                                                              ('PRODUCT', 'BUY')
    )
WHERE  r.name = 'CUSTOMER'
    ON CONFLICT DO NOTHING;