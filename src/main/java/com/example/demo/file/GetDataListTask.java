package com.example.demo.file;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import com.example.demo.dao.CronDao;
import com.example.demo.domain.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;


/**
 * 定时任务调度，从数据库中获取配置进行开始与暂停
 */
@Component
@EnableScheduling
public class GetDataListTask {

    private Set<Integer> cronIdCache = new HashSet<>();	// 缓存已经加入到队列中的cron

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    private Map<Integer, ScheduledFuture<?>> futureMap = new HashMap<>();

    @Autowired
    private CronDao cronDao;

    // 0 要执行  1  执行中  2 要停止  3 已停止
    @Scheduled(cron = "0/10 * * * * ?")
    public void getData() {
        System.out.println("get ALl data...");
        List<Cron> listNew = new ArrayList<>();	// 新的
        List<Cron> listStop = new ArrayList<>();

        List<Cron> list = cronDao.findAll();
        for(Cron entity : list) {
            if (!cronIdCache.contains(entity.getId()) && entity.getStatus() == 0) {
                //更改数据库中的任务状态
                cronDao.updateStatus(entity.getId(), 1);
                cronIdCache.add(entity.getId());
                listNew.add(entity);
            }
            if (cronIdCache.contains(entity.getId()) && entity.getStatus() == 2) {
                //更改数据库中的任务状态
                cronDao.updateStatus(entity.getId(), 3);
                listStop.add(entity);
            }
        }
        start(listNew);
        stop(listStop);
    }

    private void start(List<Cron> listNew) {
        for (Cron entity : listNew) {
            ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(
                    () -> {
                        //具体的任务执行
                        System.out.println(entity.getName() + " " + LocalTime.now().toString());
                        },
                    new CronTrigger(entity.getCron()));
            futureMap.put(entity.getId(), future);
            System.out.println(entity.getName() + " 开始..");
        }
    }

    private void stop(List<Cron> listStop) {
        for (Cron entity : listStop) {
            int id = entity.getId();
            if (futureMap.containsKey(id)) {
                ScheduledFuture<?> future = futureMap.get(id);
                if (future != null) {
                    future.cancel(true);
                }
                futureMap.remove(id);
                cronIdCache.remove(id);
                System.out.println(entity.getName() + " 结束..");
            }
        }
    }
}