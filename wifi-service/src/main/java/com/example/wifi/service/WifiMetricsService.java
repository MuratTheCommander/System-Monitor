package com.example.wifi.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import oshi.SystemInfo;
import oshi.hardware.NetworkIF;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class WifiMetricsService {

    private final MeterRegistry meterRegistry;
    private final AtomicReference<Double> wifiUpload = new AtomicReference<>(0.0);
    private final AtomicReference<Double> wifiDownload = new AtomicReference<>(0.0);
    private final SystemInfo systemInfo = new SystemInfo();

    // Match Docker-visible interface like "eth0"
    private static final String TARGET_INTERFACE_NAME = "eth0";

    public WifiMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        System.out.println("=== One-Time Startup Interface Dump ===");
        List<NetworkIF> interfaces = systemInfo.getHardware().getNetworkIFs();
        for (NetworkIF net : interfaces) {
            System.out.println("DisplayName: " + net.getDisplayName());
            for (String addr : net.getIPv4addr()) {
                System.out.println(" - IPv4: " + addr);
            }
        }

        Gauge.builder("wifi_upload_bytes_per_sec", wifiUpload, AtomicReference::get)
                .description("WiFi Upload Speed (Bytes/sec)")
                .register(meterRegistry);

        Gauge.builder("wifi_download_bytes_per_sec", wifiDownload, AtomicReference::get)
                .description("WiFi Download Speed (Bytes/sec)")
                .register(meterRegistry);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            NetworkIF target = null;

            for (NetworkIF net : systemInfo.getHardware().getNetworkIFs()) {
                net.updateAttributes();
                if (net.getDisplayName().contains(TARGET_INTERFACE_NAME)) {
                    target = net;
                    break;
                }
            }

            if (target != null) {
                target.updateAttributes();
                long tx1 = target.getBytesSent();
                long rx1 = target.getBytesRecv();

                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                target.updateAttributes();
                long tx2 = target.getBytesSent();
                long rx2 = target.getBytesRecv();

                double uploadRate = (tx2 - tx1) / 0.5;
                double downloadRate = (rx2 - rx1) / 0.5;

                wifiUpload.set(uploadRate);
                wifiDownload.set(downloadRate);
            }

        }, 0, 2, TimeUnit.SECONDS);
    }
}
