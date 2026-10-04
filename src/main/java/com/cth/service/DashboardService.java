package com.cth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.MemoryMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        long heapUsed = memoryBean.getHeapMemoryUsage().getUsed() / (1024 * 1024);
        long heapMax = memoryBean.getHeapMemoryUsage().getMax() / (1024 * 1024);

        metrics.put("systemCpuLoad", Math.round(osBean.getSystemLoadAverage() * 10.0) / 10.0);
        metrics.put("availableProcessors", osBean.getAvailableProcessors());
        metrics.put("heapUsedMb", heapUsed);
        metrics.put("heapMaxMb", heapMax);
        metrics.put("activeSessions", 12); // Sample realtime metric
        metrics.put("reportsGeneratedToday", 145);
        metrics.put("activeWorkflows", 8);
        metrics.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        return metrics;
    }

    @Scheduled(fixedRate = 3000)
    public void pushRealtimeMetrics() {
        try {
            Map<String, Object> metrics = getMetrics();
            messagingTemplate.convertAndSend("/topic/metrics", metrics);
        } catch (Exception e) {
            // Ignore if messaging broker not ready
        }
    }
}
