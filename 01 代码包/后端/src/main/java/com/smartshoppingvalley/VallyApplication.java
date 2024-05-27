package com.smartshoppingvalley;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan
@EnableTransactionManagement
public class VallyApplication {

    public static void main(String[] args) {
        SpringApplication.run(VallyApplication.class, args);
    }

}
