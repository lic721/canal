package com.monitor.client;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动入口
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@EnableScheduling
@SpringBootApplication
public class MonitorClientApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MonitorClientApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}
