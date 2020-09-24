package org.activiti.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "org.activiti.app")
@SpringBootApplication
@EnableAutoConfiguration
public class ActivitiAppApplication {

    public static void main(String args[]){
        SpringApplication.run(ActivitiAppApplication.class, args);
    }

}
