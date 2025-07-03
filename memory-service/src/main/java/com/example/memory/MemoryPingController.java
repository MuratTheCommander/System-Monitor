package com.example.memory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemoryPingController {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
