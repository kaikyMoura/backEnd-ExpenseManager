package com.lab.expenseManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExpenseManagerBackEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExpenseManagerBackEndApplication.class, args);
    }
}
