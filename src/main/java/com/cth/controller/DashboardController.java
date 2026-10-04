package com.cth.controller;

import com.cth.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Value("${cth.app.name:CTH Enterprise Reporting System}")
    private String appName;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("metrics", dashboardService.getMetrics());
        return "dashboard";
    }
}
