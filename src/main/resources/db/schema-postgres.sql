-- PostgreSQL Database DDL and Initial DML Script for CTH Enterprise Platform

-- Table: cth_users
CREATE TABLE IF NOT EXISTS cth_users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(150),
    email VARCHAR(150),
    phone_number VARCHAR(50),
    role VARCHAR(50) DEFAULT 'ROLE_USER',
    locked BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT TRUE,
    password_last_changed TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    password_expiration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP + INTERVAL '90 days',
    require2fa BOOLEAN DEFAULT FALSE
);

-- Table: cth_report_configs
CREATE TABLE IF NOT EXISTS cth_report_configs (
    id BIGSERIAL PRIMARY KEY,
    report_name VARCHAR(150) NOT NULL UNIQUE,
    description VARCHAR(500),
    table_name VARCHAR(100) NOT NULL,
    selected_fields VARCHAR(2000) NOT NULL,
    filter_condition VARCHAR(1000),
    order_by_field VARCHAR(500),
    sort_direction VARCHAR(10) DEFAULT 'ASC',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: cth_db_configs
CREATE TABLE IF NOT EXISTS cth_db_configs (
    id BIGSERIAL PRIMARY KEY,
    db_type VARCHAR(50) DEFAULT 'POSTGRESQL',
    host VARCHAR(100) DEFAULT 'localhost',
    port INT DEFAULT 5432,
    database_name VARCHAR(100) DEFAULT 'cthdb',
    username VARCHAR(100) DEFAULT 'postgres',
    password VARCHAR(100),
    jdbc_url VARCHAR(500),
    driver_class_name VARCHAR(150) DEFAULT 'org.postgresql.Driver'
);

-- Initial Admin Seed Data
INSERT INTO cth_users (username, password, full_name, email, role, locked, enabled)
VALUES ('admin', '$2a$10$eD2M94xJ1tA6I.KxX9.j9O7c6/J4v4mD8P4p7E9Q9x8x8x8x8x8x8', 'System Administrator', 'admin@cth.com', 'ROLE_ADMIN', FALSE, TRUE)
ON CONFLICT (username) DO NOTHING;
