package com.example.study01.schedule;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Configuration
public class SpringbootScheduleTask {
    /**
     * 每隔5s 添加定时任务
     * 或直接指定时间间隔，例如：5秒 @Scheduled(fixedRate=5000)
     */
    @Scheduled(cron = "0/5 * * * * ?")
    private void configureTasks() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }
}
