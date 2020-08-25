package ru.bia.traffic.notifications.event.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.bia.traffic.notifications.event.service.SchedulerService;

@Slf4j
public class TriggerAllJobsJob implements Job {
    private SchedulerService schedulerService;

    @Autowired
    public void setSchedulerService(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String notificationType = context
                .getJobDetail()
                .getJobDataMap()
                .getString("notificationType");
        schedulerService.startAllJobs(notificationType);
        log.info("All jobs started");
    }
}
