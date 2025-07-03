package com.example.cpu;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CpuPingController {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
