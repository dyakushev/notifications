package ru.bia.traffic.notifications.core.service.impl.email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.mail.util.MimeMessageParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import ru.bia.traffic.notifications.core.model.impl.email.Mail;

import javax.mail.internet.MimeMessage;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {EmailNotificationServiceImpl.class,
        MailSenderAutoConfiguration.class})
@ActiveProfiles("integration-test")
@Log4j2
public class EmailNotificationServiceImplIntegrationTest {
    @Autowired
    private EmailNotificationServiceImpl emailService;
    @Autowired
    private JavaMailSender javaMailSender;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "secret";
    private static final String EMAIL_USERNAME = "info@memorynotfound.com";
    private static final String EMAIL_PASSWORD = "info@memorynotfound.com";

    private GreenMail greenMail = new GreenMail(new ServerSetup(2525, null, "smtp"));


    @BeforeEach
    void setUp() {
        greenMail.start();
        greenMail.setUser(USERNAME, PASSWORD);
        greenMail.setUser(EMAIL_USERNAME, EMAIL_PASSWORD);
    }

    @AfterEach
    void afterEach() {
        greenMail.stop();
    }

    @Test
    public void send_ServiceIsOk_shouldSendSingleMail() throws Exception {
        Mail mail = Mail
                .builder()
                .to(Collections.singletonList(EMAIL_USERNAME))
                .subject("Test")
                .content("We show how to write Integration Tests using Spring and GreenMail.")
                .build();

        emailService.send(mail);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);

        MimeMessage current = receivedMessages[0];
        MimeMessageParser parser = new MimeMessageParser(current);
        parser.parse();

        assertEquals(mail.getSubject(), parser.getSubject());
        assertEquals(mail.getTo().get(0), parser.getTo().get(0).toString());
        assertEquals(mail.getContent(), parser.getPlainContent());

    }
}
