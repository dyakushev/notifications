package ru.bia.traffic.notifications.event.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bia.traffic.notifications.event.model.NotificationMessage;
import ru.bia.traffic.notifications.event.service.SchedulerService;

@Slf4j
@Component
@EnableRabbit
public class RabbitMessagesListener {

    private SchedulerService schedulerService;
    @Value("${rabbit.queue}")
    private String queueName;

    public RabbitMessagesListener(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @RabbitListener(queues = "${rabbit.queue}")
    public void processNotificationQueue(NotificationMessage rabbitMessage) {
        log.info("Received from notification queue: " + rabbitMessage);
        schedulerService.addNewJob(rabbitMessage);
    }
}
