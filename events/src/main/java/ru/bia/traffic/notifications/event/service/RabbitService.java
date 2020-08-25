package ru.bia.traffic.notifications.event.service;

import ru.bia.traffic.notifications.event.model.NotificationMessage;

import java.util.List;

public interface RabbitService {
    NotificationMessage receiveMessage();

    List<NotificationMessage> receiveAllMessages();
}
