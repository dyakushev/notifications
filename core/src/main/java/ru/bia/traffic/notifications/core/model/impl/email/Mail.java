package ru.bia.traffic.notifications.core.model.impl.email;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.bia.traffic.notifications.core.model.MessageData;

import javax.validation.constraints.Email;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper=true)
public class Mail extends MessageData {
    private String pathToAttachment;

    private List<@Email String> to;
    private String subject;
    private String content;
}
