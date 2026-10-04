package com.cth.service;

import com.cth.model.ReportConfig;
import com.cth.repository.ReportConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReportConfigRepository reportConfigRepository;

    public ReportConfig saveReportConfig(String reportName, String description, String tableName,
                                         List<String> fields, String filterCondition,
                                         String orderBy, String sortDirection) {
        String selectedFields = (fields != null && !fields.isEmpty()) ? String.join(",", fields) : "*";

        Optional<ReportConfig> existing = reportConfigRepository.findByReportName(reportName);
        ReportConfig config = existing.orElse(new ReportConfig());
        config.setReportName(reportName);
        config.setDescription(description);
        config.setTableName(tableName);
        config.setSelectedFields(selectedFields);
        config.setFilterCondition(filterCondition);
        config.setOrderByField(orderBy);
        config.setSortDirection(sortDirection != null ? sortDirection : "ASC");

        return reportConfigRepository.save(config);
    }

    public List<ReportConfig> getAllReportConfigs() {
        return reportConfigRepository.findAll();
    }

    public Optional<ReportConfig> getReportConfigById(Long id) {
        return reportConfigRepository.findById(id);
    }

    public List<Map<String, Object>> executeReport(ReportConfig config) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(config.getSelectedFields()).append(" FROM ").append(config.getTableName());

        if (config.getFilterCondition() != null && !config.getFilterCondition().trim().isEmpty()) {
            sql.append(" WHERE ").append(config.getFilterCondition());
        }

        if (config.getOrderByField() != null && !config.getOrderByField().trim().isEmpty()) {
            sql.append(" ORDER BY ").append(config.getOrderByField()).append(" ").append(config.getSortDirection());
        }

        JdbcTemplate template = new JdbcTemplate(jdbcTemplate.getDataSource());
        template.setMaxRows(500); // Cross-database compatible max rows limit (Oracle, Postgres, SQL Server, H2)

        return template.queryForList(sql.toString());
    }

    public List<Map<String, Object>> executeCustomQuery(String tableName, List<String> fields, String filter, String orderBy, String dir) {
        String selectedFields = (fields != null && !fields.isEmpty()) ? String.join(",", fields) : "*";
        StringBuilder sql = new StringBuilder("SELECT ").append(selectedFields).append(" FROM ").append(tableName);

        if (filter != null && !filter.trim().isEmpty()) {
            sql.append(" WHERE ").append(filter);
        }

        if (orderBy != null && !orderBy.trim().isEmpty()) {
            sql.append(" ORDER BY ").append(orderBy).append(" ").append(dir != null ? dir : "ASC");
        }

        JdbcTemplate template = new JdbcTemplate(jdbcTemplate.getDataSource());
        template.setMaxRows(500); // Cross-database compatible max rows limit

        return template.queryForList(sql.toString());
    }
}
