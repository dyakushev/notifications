package ru.bia.traffic.notifications.core.service.impl.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.bia.traffic.notifications.core.model.impl.email.Mail;
import ru.bia.traffic.notifications.core.service.NotificationService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;


@Slf4j
@Service
public class EmailNotificationServiceImpl implements NotificationService<Mail> {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void send(Mail mail) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            //to
            List<String> toList = mail.getTo();
            if (toList == null)
                throw new IllegalArgumentException();
            if (toList.isEmpty())
                throw new IllegalArgumentException();
            String[] to = new String[toList.size()];
            mail.getTo().toArray(to);
            helper.setTo(to);
            //content
            if (mail.getContent() != null)
                helper.setText(mail.getContent());
            //subject
            if (mail.getSubject() != null)
                helper.setSubject(mail.getSubject());
            //attachment
            if (mail.getPathToAttachment() != null && !mail.getPathToAttachment().isEmpty()) {
                FileSystemResource file
                        = new FileSystemResource(new File(mail.getPathToAttachment()));
                helper.addAttachment(file.getFilename(), file);
            }

            javaMailSender.send(message);

            log.info("email to {} with the message {} was sent", mail.getTo(), mail.getContent());
        } catch (MailException | MessagingException e) {
            log.info("email to {} with the message {} was sent because of exception {}", mail.getTo(), mail.getContent(), e.toString());
        }
    }
}
