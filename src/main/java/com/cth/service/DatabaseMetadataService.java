package com.cth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseMetadataService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getAvailableTables() {
        List<String> tables = new ArrayList<>();
        try {
            DataSource dataSource = jdbcTemplate.getDataSource();
            if (dataSource == null) return tables;

            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData metaData = conn.getMetaData();
                try (ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE", "VIEW"})) {
                    while (rs.next()) {
                        String tableName = rs.getString("TABLE_NAME");
                        // Exclude system tables where appropriate
                        if (!tableName.startsWith("SYSTEM_") && !tableName.startsWith("SYS_")) {
                            tables.add(tableName);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public List<Map<String, String>> getColumnsForTable(String tableName) {
        List<Map<String, String>> columns = new ArrayList<>();
        try {
            DataSource dataSource = jdbcTemplate.getDataSource();
            if (dataSource == null) return columns;

            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData metaData = conn.getMetaData();
                try (ResultSet rs = metaData.getColumns(null, null, tableName, "%")) {
                    while (rs.next()) {
                        Map<String, String> col = new HashMap<>();
                        col.put("name", rs.getString("COLUMN_NAME"));
                        col.put("type", rs.getString("TYPE_NAME"));
                        col.put("size", String.valueOf(rs.getInt("COLUMN_SIZE")));
                        columns.add(col);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }
}
