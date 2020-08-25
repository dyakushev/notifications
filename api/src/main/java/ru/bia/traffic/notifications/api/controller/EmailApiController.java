package ru.bia.traffic.notifications.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bia.traffic.notifications.api.model.NotificationResponseEntity;
import ru.bia.traffic.notifications.event.model.NotificationMessage;
import ru.bia.traffic.notifications.event.service.SchedulerService;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping(path = "/notifications/api/", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailApiController {

    private SchedulerService schedulerService;

    /*   @Autowired
       public void setSchedulerService(SchedulerService schedulerService) {
           this.schedulerService = schedulerService;
       }*/
    public EmailApiController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<NotificationResponseEntity> addNotification(@RequestBody @Valid NotificationMessage notificationMessage) {
        String jobName = schedulerService.addNewJob(notificationMessage);
        NotificationResponseEntity notificationResponseEntity = null;
        if (jobName == null) {
            notificationResponseEntity = NotificationResponseEntity.builder().success(false).message("Error scheduling email. Please try later!").build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(notificationResponseEntity);
        }
        notificationResponseEntity = NotificationResponseEntity.builder().success(true).jobId(jobName).message("Email Scheduled Successfully!").build();
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(notificationResponseEntity);
    }
}
