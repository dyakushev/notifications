package ru.bia.traffic.notifications.event.component;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import ru.bia.traffic.notifications.event.job.TriggerAllJobsJob;

@Slf4j
@Component
public class QuartzStartupListener implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${rabbit.jobRepeatIntervalSeconds}")
    private int repeatInterval;

    private SchedulerFactoryBean schedulerFactoryBean;


    @Autowired
    public void setSchedulerFactoryBean(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDetail jobDetail = JobBuilder
                .newJob(TriggerAllJobsJob.class)
                .withIdentity("triggerAllJobs", "bulk")
                .usingJobData("notificationType", "email")
                .build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .startNow()
                .withIdentity("triggerAllJobs", "bulk")
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .repeatForever()
                        .withIntervalInSeconds(repeatInterval)
                )
                .build();
        try {
            if (!scheduler.checkExists(jobDetail.getKey()) && !scheduler.checkExists(trigger.getKey()))
                scheduler.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            log.error(e.toString());
        }

    }
}
