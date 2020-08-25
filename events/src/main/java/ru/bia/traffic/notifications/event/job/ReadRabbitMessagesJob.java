package ru.bia.traffic.notifications.event.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bia.traffic.notifications.event.model.NotificationMessage;
import ru.bia.traffic.notifications.event.service.RabbitService;
import ru.bia.traffic.notifications.event.service.SchedulerService;

import java.util.List;

@Component
@Slf4j
public class ReadRabbitMessagesJob implements Job {
    private RabbitService rabbitService;

    private SchedulerService schedulerService;


    @Autowired
    public void setRabbitService(RabbitService rabbitService) {
        this.rabbitService = rabbitService;
    }

    @Autowired
    public void setSchedulerService(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        List<NotificationMessage> messageList = rabbitService.receiveAllMessages();
        messageList
                .stream()
                .forEach(schedulerService::addNewJob);
    }
}
