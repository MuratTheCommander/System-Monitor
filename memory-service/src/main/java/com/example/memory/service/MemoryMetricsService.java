
package com.example.memory.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class MemoryMetricsService {

    private final MeterRegistry meterRegistry;
    private final AtomicReference<Double> cpuUsage = new AtomicReference<>(0.0);
    private final GlobalMemory memory;

    public MemoryMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.memory = new SystemInfo().getHardware().getMemory();
    }

    @PostConstruct
    public void init() {
        Gauge.builder("system_memory_usage_percent", cpuUsage, AtomicReference::get)
             .description("Memory Usage")
             .register(meterRegistry);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            long[] prevTicks = null;
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            double load = (1.0 - (double) memory.getAvailable() / memory.getTotal()) * 100;
            cpuUsage.set(load);
        }, 0, 2, TimeUnit.SECONDS);
    }
}
