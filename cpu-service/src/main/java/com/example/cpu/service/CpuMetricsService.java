
package com.example.cpu.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CpuMetricsService {

    private final MeterRegistry meterRegistry;
    private final AtomicReference<Double> cpuUsage = new AtomicReference<>(0.0);
    private final CentralProcessor processor;

    public CpuMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.processor = new SystemInfo().getHardware().getProcessor();
    }

    @PostConstruct
    public void init() {
        Gauge.builder("system_cpu_usage_percent", cpuUsage, AtomicReference::get)
                .description("CPU Usage")
                .register(meterRegistry);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            long[] prevTicks = processor.getSystemCpuLoadTicks();

            try {
                TimeUnit.MILLISECONDS.sleep(300); // Reduced sampling delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            double load = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
            cpuUsage.set(load);

        }, 0, 1, TimeUnit.SECONDS); // Run every 1 second
    }

}
