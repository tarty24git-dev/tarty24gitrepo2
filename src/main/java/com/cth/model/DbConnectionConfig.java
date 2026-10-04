package com.cth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cth_db_configs")
public class DbConnectionConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dbType = "H2"; // ORACLE, POSTGRESQL, SQLSERVER, H2
    private String host = "localhost";
    private int port = 1521;
    private String databaseName = "cthdb";
    private String username = "sa";
    private String password = "";
    private String jdbcUrl = "jdbc:h2:mem:cthdb;DB_CLOSE_DELAY=-1";
    private String driverClassName = "org.h2.Driver";

    public DbConnectionConfig() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDbType() { return dbType; }
    public void setDbType(String dbType) { this.dbType = dbType; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getDatabaseName() { return databaseName; }
    public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getJdbcUrl() { return jdbcUrl; }
    public void setJdbcUrl(String jdbcUrl) { this.jdbcUrl = jdbcUrl; }

    public String getDriverClassName() { return driverClassName; }
    public void setDriverClassName(String driverClassName) { this.driverClassName = driverClassName; }
}
