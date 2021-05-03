package org.camptocamp.watchconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WatchConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WatchConnectApplication.class, args);
    }

}
