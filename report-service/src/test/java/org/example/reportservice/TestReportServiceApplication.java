package org.example.reportservice;

import org.springframework.boot.SpringApplication;

public class TestReportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(ReportServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
