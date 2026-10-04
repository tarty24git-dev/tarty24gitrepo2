package com.cth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    private ConfigurableEnvironment environment;

    @GetMapping
    public String settingsPage(Model model) {
        model.addAttribute("appName", environment.getProperty("cth.app.name", "CTH Enterprise Reporting System"));
        model.addAttribute("authProvider", environment.getProperty("cth.auth.provider", "DATABASE"));
        model.addAttribute("smsEmail2FaEnabled", Boolean.parseBoolean(environment.getProperty("cth.auth.sms-email-2fa-enabled", "false")));
        model.addAttribute("dbType", environment.getProperty("cth.db.type", "H2"));
        model.addAttribute("dbUrl", environment.getProperty("spring.datasource.url", "jdbc:h2:mem:cthdb"));
        model.addAttribute("dbUsername", environment.getProperty("spring.datasource.username", "sa"));

        return "settings";
    }

    @PostMapping("/update")
    public String updateSettings(@RequestParam("appName") String appName,
                                 @RequestParam("authProvider") String authProvider,
                                 @RequestParam(value = "smsEmail2FaEnabled", defaultValue = "false") boolean smsEmail2FaEnabled,
                                 @RequestParam("dbType") String dbType,
                                 @RequestParam("dbUrl") String dbUrl,
                                 @RequestParam("dbUsername") String dbUsername,
                                 @RequestParam(value = "dbPassword", required = false) String dbPassword,
                                 Model model) {

        // Dynamically update runtime environment properties
        Map<String, Object> newProps = new HashMap<>();
        newProps.put("cth.app.name", appName);
        newProps.put("cth.auth.provider", authProvider);
        newProps.put("cth.auth.sms-email-2fa-enabled", String.valueOf(smsEmail2FaEnabled));
        newProps.put("cth.db.type", dbType);
        newProps.put("spring.datasource.url", dbUrl);
        newProps.put("spring.datasource.username", dbUsername);
        if (dbPassword != null && !dbPassword.isEmpty()) {
            newProps.put("spring.datasource.password", dbPassword);
        }

        environment.getPropertySources().addFirst(new MapPropertySource("dynamicProps", newProps));

        // Save to file for persistence across restarts
        try {
            Properties props = new Properties();
            props.setProperty("cth.app.name", appName);
            props.setProperty("cth.auth.provider", authProvider);
            props.setProperty("cth.auth.sms-email-2fa-enabled", String.valueOf(smsEmail2FaEnabled));
            props.setProperty("cth.db.type", dbType);
            props.setProperty("spring.datasource.url", dbUrl);
            props.setProperty("spring.datasource.username", dbUsername);
            if (dbPassword != null) {
                props.setProperty("spring.datasource.password", dbPassword);
            }
            try (FileOutputStream fos = new FileOutputStream("custom-application.properties")) {
                props.store(fos, "Configured via CTH Frontend Settings");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("msg", "System Settings successfully updated and saved!");
        return settingsPage(model);
    }
}
