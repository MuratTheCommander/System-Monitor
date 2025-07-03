package com.example.disk.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.software.os.OSFileStore;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DiskMetricsService {

    private final MeterRegistry meterRegistry;
    private final AtomicReference<Double> diskUsage = new AtomicReference<>(0.0);
    private final SystemInfo systemInfo = new SystemInfo();

    public DiskMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        // Register the disk usage metric
        Gauge.builder("system_disk_usage_percent", diskUsage, AtomicReference::get)
                .description("Disk Usage Percent")
                .register(meterRegistry);

        // Log available file stores once
        List<OSFileStore> fileStores = systemInfo.getOperatingSystem().getFileSystem().getFileStores();
        System.out.println("=== Detected File Stores ===");
        for (OSFileStore fs : fileStores) {
            System.out.printf("Mount: %s, Total: %.2f GB, Usable: %.2f GB, Free: %.2f GB%n",
                    fs.getMount(),
                    fs.getTotalSpace() / 1e9,
                    fs.getUsableSpace() / 1e9,
                    fs.getFreeSpace() / 1e9);
        }

        // Scheduled updater
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true); // Ensure JVM can exit cleanly
            return t;
        }).scheduleAtFixedRate(() -> {
            double total = 0, used = 0;
            for (OSFileStore fs : fileStores) {
                total += fs.getTotalSpace();
                used += fs.getTotalSpace() - fs.getUsableSpace();
            }
            double usagePercent = (total > 0) ? (used / total) * 100 : 0;
            diskUsage.set(usagePercent);

            // Optional: log periodically for debug
            System.out.printf("Updated Disk Usage: %.2f%%%n", usagePercent);

        }, 0, 2, TimeUnit.SECONDS);
    }
}
