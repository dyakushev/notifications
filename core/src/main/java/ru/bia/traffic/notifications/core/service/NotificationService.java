package ru.bia.traffic.notifications.core.service;

import ru.bia.traffic.notifications.core.model.MessageData;

public interface NotificationService<T extends MessageData> {
    void send(T t);
}
