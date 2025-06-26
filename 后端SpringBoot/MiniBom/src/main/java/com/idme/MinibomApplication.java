package com.idme;

/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2024-2024. All rights reserved.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类，需要扫描客户端SDK包和用户项目路径包
 *
 * @since 2024-04-10
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.huawei.innovation", "com.idme"})
public class MinibomApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinibomApplication.class, args);
    }
}
