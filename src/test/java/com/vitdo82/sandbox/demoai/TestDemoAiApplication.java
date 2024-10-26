package com.vitdo82.sandbox.demoai;

import org.springframework.boot.SpringApplication;

public class TestDemoAiApplication {

    public static void main(String[] args) {
        SpringApplication.from(DemoAiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
