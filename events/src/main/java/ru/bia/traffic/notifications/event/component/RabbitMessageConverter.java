package ru.bia.traffic.notifications.event.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.bia.traffic.notifications.core.model.impl.email.Mail;
import ru.bia.traffic.notifications.event.model.NotificationMessage;

@Slf4j
@Component
public class RabbitMessageConverter {

    public Mail convertToMail(NotificationMessage message) {

        return Mail.builder().to(message.getRecipients()).content(message.getContent()).subject(message.getSubject()).build();
    }
}
