package ru.bia.traffic.notifications.event.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import ru.bia.traffic.notifications.event.component.RabbitMessageConverter;
import ru.bia.traffic.notifications.event.exception.JobAlreadyExistsException;
import ru.bia.traffic.notifications.event.job.SendEmailJob;
import ru.bia.traffic.notifications.event.model.NotificationMessage;
import ru.bia.traffic.notifications.event.service.SchedulerService;

import java.util.UUID;

@Service
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {

    private SchedulerFactoryBean schedulerFactoryBean;
    private RabbitMessageConverter rabbitMessageConverter;

    public SchedulerServiceImpl(SchedulerFactoryBean schedulerFactoryBean,
                                RabbitMessageConverter rabbitMessageConverter
    ) {
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.rabbitMessageConverter = rabbitMessageConverter;
    }

    @Override
    public String addNewJob(NotificationMessage notificationMessage) {
        String type = notificationMessage.getType();
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = null;
        if (type.equals("email")) {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("messageData", rabbitMessageConverter.convertToMail(notificationMessage));
            JobDetail jobDetail = JobBuilder
                    .newJob(SendEmailJob.class)
                    .withIdentity(UUID
                            .randomUUID()
                            .toString(), "email")
                    .setJobData(jobDataMap)
                    .storeDurably()
                    .build();
            try {
                if (!scheduler.checkExists(jobDetail.getKey())) {
                    scheduler.addJob(jobDetail, false);
                    jobKey = jobDetail.getKey();
                    log.info("job {} scheduled", jobDetail.getKey().toString());
                } else {
                    log.warn("job {} already exists", jobDetail.getKey().toString());
                    throw new JobAlreadyExistsException("job already exists " + jobDetail.getKey().toString());
                }
            } catch (SchedulerException e) {
                log.error(e.toString());
            }
        }
        return jobKey == null ? null : jobKey.getName();
    }

    @Override
    public void startAllJobs(String notificationType) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        GroupMatcher<JobKey> matcher;
        if (notificationType.equals("any"))
            matcher = GroupMatcher.anyGroup();
        else
            matcher = GroupMatcher.groupEquals(notificationType);
        try {
            scheduler
                    .getJobKeys(matcher)
                    .stream()
                    .forEach(
                            jobKey -> {
                                try {
                                    scheduler.triggerJob(jobKey);
                                    scheduler.deleteJob(jobKey);
                                } catch (SchedulerException e) {
                                    log.error(e.toString());
                                }

                            });
        } catch (SchedulerException e) {
            log.error(e.toString());
        }
    }
}
