package ru.bia.traffic.notifications.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class NotificationResponseEntity {
    private Boolean success;
    private String jobId;
    private String message;
}
