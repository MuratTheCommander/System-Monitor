package com.example.disk;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiskPingController {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
