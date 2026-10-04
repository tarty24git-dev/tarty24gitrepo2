-- SQL Server Database DDL and Initial DML Script for CTH Enterprise Platform

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'cth_users')
BEGIN
    CREATE TABLE cth_users (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        username VARCHAR(100) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        full_name VARCHAR(150),
        email VARCHAR(150),
        phone_number VARCHAR(50),
        role VARCHAR(50) DEFAULT 'ROLE_USER',
        locked BIT DEFAULT 0,
        enabled BIT DEFAULT 1,
        password_last_changed DATETIME DEFAULT GETDATE(),
        password_expiration_date DATETIME DEFAULT DATEADD(day, 90, GETDATE()),
        require2fa BIT DEFAULT 0
    );
END;

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'cth_report_configs')
BEGIN
    CREATE TABLE cth_report_configs (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        report_name VARCHAR(150) NOT NULL UNIQUE,
        description VARCHAR(500),
        table_name VARCHAR(100) NOT NULL,
        selected_fields VARCHAR(2000) NOT NULL,
        filter_condition VARCHAR(1000),
        order_by_field VARCHAR(500),
        sort_direction VARCHAR(10) DEFAULT 'ASC',
        created_at DATETIME DEFAULT GETDATE()
    );
END;

IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'cth_db_configs')
BEGIN
    CREATE TABLE cth_db_configs (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        db_type VARCHAR(50) DEFAULT 'SQLSERVER',
        host VARCHAR(100) DEFAULT 'localhost',
        port INT DEFAULT 1433,
        database_name VARCHAR(100) DEFAULT 'cthdb',
        username VARCHAR(100) DEFAULT 'sa',
        password VARCHAR(100),
        jdbc_url VARCHAR(500),
        driver_class_name VARCHAR(150) DEFAULT 'com.microsoft.sqlserver.jdbc.SQLServerDriver'
    );
END;

-- Initial Admin Seed Data
IF NOT EXISTS (SELECT * FROM cth_users WHERE username = 'admin')
BEGIN
    INSERT INTO cth_users (username, password, full_name, email, role, locked, enabled)
    VALUES ('admin', '$2a$10$eD2M94xJ1tA6I.KxX9.j9O7c6/J4v4mD8P4p7E9Q9x8x8x8x8x8x8', 'System Administrator', 'admin@cth.com', 'ROLE_ADMIN', 0, 1);
END;
