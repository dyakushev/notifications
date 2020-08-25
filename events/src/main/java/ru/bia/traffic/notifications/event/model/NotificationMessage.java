package ru.bia.traffic.notifications.event.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class NotificationMessage {
    @NotEmpty
    @NotNull
    private String type;
    @NotEmpty
    @NotNull
    private List<String> recipients;
    private String subject;
    private String content;

}
