package com.cth.service;

import com.cth.model.ReportConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private DatabaseMetadataService metadataService;

    @Test
    public void testAvailableTablesAndCustomQueryExecution() {
        List<String> tables = metadataService.getAvailableTables();
        assertNotNull(tables);
        assertTrue(tables.contains("CTH_USERS") || tables.contains("cth_users") || !tables.isEmpty());

        List<Map<String, Object>> result = reportService.executeCustomQuery("cth_users", List.of("username", "role"), null, "id", "ASC");
        assertNotNull(result);
    }

    @Test
    public void testReportConfigSaveAndRetrieval() {
        ReportConfig saved = reportService.saveReportConfig("User Activity Report", "Active users in system", "cth_users", List.of("username", "email"), "enabled=true", "username", "ASC");
        assertNotNull(saved.getId());
        assertEquals("User Activity Report", saved.getReportName());

        List<ReportConfig> all = reportService.getAllReportConfigs();
        assertFalse(all.isEmpty());
    }
}
