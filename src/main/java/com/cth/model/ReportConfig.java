package com.cth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cth_report_configs")
public class ReportConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reportName;

    private String description;

    @Column(nullable = false)
    private String tableName;

    @Column(length = 2000, nullable = false)
    private String selectedFields; // Comma-separated field names

    @Column(length = 1000)
    private String filterCondition; // Optional WHERE clause

    @Column(length = 500)
    private String orderByField;

    private String sortDirection = "ASC";

    private LocalDateTime createdAt = LocalDateTime.now();

    public ReportConfig() {}

    public ReportConfig(String reportName, String description, String tableName, String selectedFields, String filterCondition, String orderByField, String sortDirection) {
        this.reportName = reportName;
        this.description = description;
        this.tableName = tableName;
        this.selectedFields = selectedFields;
        this.filterCondition = filterCondition;
        this.orderByField = orderByField;
        this.sortDirection = sortDirection != null ? sortDirection : "ASC";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public String getSelectedFields() { return selectedFields; }
    public void setSelectedFields(String selectedFields) { this.selectedFields = selectedFields; }

    public String getFilterCondition() { return filterCondition; }
    public void setFilterCondition(String filterCondition) { this.filterCondition = filterCondition; }

    public String getOrderByField() { return orderByField; }
    public void setOrderByField(String orderByField) { this.orderByField = orderByField; }

    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
