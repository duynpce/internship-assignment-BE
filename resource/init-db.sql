-- =============================================================================
-- PostgreSQL bootstrap - runs once on first container start
-- Creates separate databases for each service
-- =============================================================================

CREATE DATABASE auth_db;
CREATE DATABASE user_db;
CREATE DATABASE report_db;
CREATE DATABASE ticket_db;
