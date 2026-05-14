package com.kory.fixflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "启动测试成功";
    }   // 测试成功
}
