package org.aru.number.generator.scheduler;

import org.aru.number.generator.api.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * This class schedules task
 */
@Component
public class Scheduler {

    @Autowired
    private TaskService taskProcessor;
    @Scheduled(cron ="* * * * * ?")
    @Async
    public void processTasks() {
        taskProcessor.processTasks();
    }
}
