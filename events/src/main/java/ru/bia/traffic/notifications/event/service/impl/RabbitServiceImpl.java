package ru.bia.traffic.notifications.event.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.bia.traffic.notifications.event.model.NotificationMessage;
import ru.bia.traffic.notifications.event.service.RabbitService;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class RabbitServiceImpl implements RabbitService {
    private RabbitTemplate rabbit;
    private MessageConverter messageConverter;


    @Value("${rabbit.queue}")
    private String queueName;

    public RabbitServiceImpl(RabbitTemplate rabbit,
                             MessageConverter messageConverter
                             ) {
        this.messageConverter = messageConverter;
        this.rabbit = rabbit;
    }

    @Override
    public NotificationMessage receiveMessage() {

        return null;
    }

    @Override
    public List<NotificationMessage> receiveAllMessages() {
        List<NotificationMessage> rabbitMessageList = new ArrayList<>();
        Message message = rabbit.receive(queueName);
        while (message != null) {
            rabbitMessageList.add((NotificationMessage) messageConverter.fromMessage(message));
            message = rabbit.receive(queueName);
        }
        return rabbitMessageList;
    }


}
