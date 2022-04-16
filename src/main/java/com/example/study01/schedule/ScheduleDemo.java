package com.example.study01.schedule;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务例子
 */
public class ScheduleDemo {


    static long count = 0;

    public static void main(String[] args) {
//        threadRunnableMethod();
//        timerTaskMethod();
//        threadPoolMethod();
        quartzJob();
    }

    private static void threadRunnableMethod() {
        Runnable runnable = () -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    count++;
                    System.out.println(count);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    private static void timerTaskMethod(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                count++;
                System.out.println(count);
            }
        };
        //创建timer对象设置间隔时间
        Timer timer = new Timer();
        // 间隔天数
        long delay = 0;
        // 间隔毫秒数
        long period = 1000;
        timer.scheduleAtFixedRate(timerTask, delay, period);
    }


    private static void threadPoolMethod(){
        Runnable runnable = () -> {
            count++;
            System.out.println(count);
        };
        ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        threadPool.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.SECONDS);
        // 第三个参数为一次执行终止和下一次执行开始之间的延迟
        threadPool.scheduleWithFixedDelay(runnable,0,5,TimeUnit.SECONDS);
    }


    private static void quartzJob(){
        //1.创建Scheduler的工厂
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        //2.从工厂中获取调度器实例
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            //3.创建JobDetail，
            JobDetail jb = JobBuilder.newJob(MyJob.class)
                    //job的描述
                    .withDescription("this is a quartz job")
                    //job的name和group
                    .withIdentity("quartzJobName", "quartzGroup")
                    .build();
            //任务运行的时间，SimpleSchedle类型触发器有效，3秒后启动任务
            long time=  System.currentTimeMillis() + 3*1000L;
            Date statTime = new Date(time);
            //4.创建Trigger
            //使用SimpleScheduleBuilder或者CronScheduleBuilder
            Trigger t = TriggerBuilder.newTrigger()
                    .withDescription("")
                    .withIdentity("ramTrigger", "ramTriggerGroup")
                    //.withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    //默认当前时间启动
                    .startAt(statTime)
                    //两秒执行一次，Quartz表达式，支持各种牛逼表达式
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
                    .build();
            //5.注册任务和定时器
            scheduler.scheduleJob(jb, t);
            //6.启动 调度器
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


}
