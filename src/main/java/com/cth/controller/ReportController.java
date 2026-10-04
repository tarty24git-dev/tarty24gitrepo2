package com.cth.controller;

import com.cth.model.ReportConfig;
import com.cth.service.DatabaseMetadataService;
import com.cth.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private DatabaseMetadataService metadataService;

    @Autowired
    private ReportService reportService;

    @GetMapping
    public String reportsPage(Model model) {
        model.addAttribute("tables", metadataService.getAvailableTables());
        model.addAttribute("savedReports", reportService.getAllReportConfigs());
        return "reports";
    }

    @GetMapping("/api/columns")
    @ResponseBody
    public List<Map<String, String>> getColumns(@RequestParam("table") String tableName) {
        return metadataService.getColumnsForTable(tableName);
    }

    @PostMapping("/run")
    public String runQuery(@RequestParam("tableName") String tableName,
                           @RequestParam(value = "fields", required = false) List<String> fields,
                           @RequestParam(value = "filter", required = false) String filter,
                           @RequestParam(value = "orderBy", required = false) String orderBy,
                           @RequestParam(value = "sortDir", required = false, defaultValue = "ASC") String sortDir,
                           Model model) {

        List<Map<String, Object>> reportData = reportService.executeCustomQuery(tableName, fields, filter, orderBy, sortDir);

        model.addAttribute("tables", metadataService.getAvailableTables());
        model.addAttribute("savedReports", reportService.getAllReportConfigs());
        model.addAttribute("reportData", reportData);
        model.addAttribute("selectedTable", tableName);
        model.addAttribute("selectedFields", fields != null ? fields : Collections.emptyList());

        if (reportData != null && !reportData.isEmpty()) {
            model.addAttribute("columns", new ArrayList<>(reportData.get(0).keySet()));
        } else {
            model.addAttribute("columns", Collections.emptyList());
        }

        return "reports";
    }

    @PostMapping("/save")
    public String saveReport(@RequestParam("reportName") String reportName,
                             @RequestParam("description") String description,
                             @RequestParam("tableName") String tableName,
                             @RequestParam(value = "fields", required = false) List<String> fields,
                             @RequestParam(value = "filter", required = false) String filter,
                             @RequestParam(value = "orderBy", required = false) String orderBy,
                             @RequestParam(value = "sortDir", required = false, defaultValue = "ASC") String sortDir) {
        reportService.saveReportConfig(reportName, description, tableName, fields, filter, orderBy, sortDir);
        return "redirect:/reports";
    }

    @GetMapping("/view/{id}")
    public String viewSavedReport(@PathVariable("id") Long id, Model model) {
        Optional<ReportConfig> configOpt = reportService.getReportConfigById(id);
        if (configOpt.isPresent()) {
            ReportConfig config = configOpt.get();
            List<Map<String, Object>> reportData = reportService.executeReport(config);
            model.addAttribute("savedReport", config);
            model.addAttribute("reportData", reportData);
            if (reportData != null && !reportData.isEmpty()) {
                model.addAttribute("columns", new ArrayList<>(reportData.get(0).keySet()));
            } else {
                model.addAttribute("columns", Collections.emptyList());
            }
        }
        model.addAttribute("tables", metadataService.getAvailableTables());
        model.addAttribute("savedReports", reportService.getAllReportConfigs());
        return "reports";
    }
}
