package ru.kazimir.bortnik.online_market.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "ru.kazimir.bortnik.online_market")
@EntityScan(basePackages = "ru.kazimir.bortnik.online_market")
public class SpringBootModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootModuleApplication.class, args);
    }
}
