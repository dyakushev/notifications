package ru.bia.traffic.notifications.event.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.bia.traffic.notifications.core.model.impl.email.Mail;
import ru.bia.traffic.notifications.core.service.NotificationService;


@Slf4j
public class SendEmailJob implements Job {
    private NotificationService<Mail> notificationService;

    @Autowired
    public void setNotificationService(@Qualifier("emailNotificationServiceImpl") NotificationService<Mail> notificationService) {
        this.notificationService = notificationService;
    }


    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Mail mail = (Mail) dataMap.get("messageData");
        notificationService.send(mail);
    }
}
