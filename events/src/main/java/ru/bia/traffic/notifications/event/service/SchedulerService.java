package ru.bia.traffic.notifications.event.service;

import ru.bia.traffic.notifications.event.model.NotificationMessage;

public interface SchedulerService {
    String addNewJob(NotificationMessage notificationMessage);

    void startAllJobs(String notificationType);
}
