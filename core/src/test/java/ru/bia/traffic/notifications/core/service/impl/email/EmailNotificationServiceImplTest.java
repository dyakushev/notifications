package ru.bia.traffic.notifications.core.service.impl.email;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import ru.bia.traffic.notifications.core.model.impl.email.Mail;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailNotificationServiceImpl emailNotificationService;
    private MimeMessage mimeMessage;
    private Mail mail, mail2;

    @BeforeEach
    void setUp() {
        mimeMessage = new MimeMessage((Session) null);

        mail = (Mail) Mail.builder().to(Collections.singletonList("to")).content("text").build();
        mail2 = (Mail) Mail.builder().build();
    }


    @Test
    void send_MailContainsToAndContent_EmailSent() {
        //when
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        //then
        emailNotificationService.send(mail);
        //assert
        verify(javaMailSender, times(1)).send(mimeMessage);

    }

    @Test
    void send_MailDoesNotContainToAndContent_ThrowsException() {
        //when
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> emailNotificationService.send(mail2));
    }

    @Test
    void send_MailIsNull_ThrowsException() {
        //when
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        //then
        Assertions.assertThrows(NullPointerException.class, () -> emailNotificationService.send(null));

    }
}