package com.thrifleganger.alexa.scene.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping(path = "/")
@RestController
public class RootController {

    @GetMapping
    public String hello() {
        return "Hello from What's the scene: " + LocalDateTime.now();
    }
}
