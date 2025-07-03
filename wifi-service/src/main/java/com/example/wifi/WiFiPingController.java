package com.example.wifi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WiFiPingController {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
