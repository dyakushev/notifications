package ru.bia.traffic.notifications.api.controller;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.bia.traffic.notifications.event.model.NotificationMessage;
import ru.bia.traffic.notifications.event.service.SchedulerService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(EmailApiController.class)
@ActiveProfiles("integration-test")
public class EmailApiControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmailApiController emailApiController;
    @MockBean
    private SchedulerService schedulerService;


    private static String JSON = "{\n" +
            "  \"type\" :\"email\",\n" +
            "  \"recipients\" : [\n" +
            "    \"yakushev.den@gmail.com\"\n" +
            "  ],\n" +
            "  \"subject\":\"testsubject\",\n" +
            "  \"content\":\"testcontent\"\n" +
            "}";
    private static String JSON2 = "{\n" +
            "  \"type\" :\"\",\n" +
            "  \"recipients\" : [\n" +
            "    \"yakushev.den@gmail.com\"\n" +
            "  ],\n" +
            "  \"subject\":\"testsubject\",\n" +
            "  \"content\":\"testcontent\"\n" +
            "}";
    private static String UID = UUID.randomUUID().toString();

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(emailApiController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    public void addNotification_RequestBodyIsValid_Returns200OK() throws Exception {
        //when
        Mockito.when(schedulerService.addNewJob(any(NotificationMessage.class))).thenReturn(UID);
        //
        this.mockMvc.perform(post("/notifications/api/add").contentType("application/json").content(JSON).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.jobId").value(UID))
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    public void addNotification_RequestBodyIsInvalid_Returns400BadRequest() throws Exception {
        //when
        Mockito.when(schedulerService.addNewJob(any(NotificationMessage.class))).thenReturn(null);
        //
        this.mockMvc.perform(post("/notifications/api/add").contentType("application/json").content(JSON2).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errors[0]").value("type must not be empty"));

    }

    @Test
    public void addNotification_RequestBodyIsValid_SchedulerReturnsNull_Returns500InternalServerError() throws Exception {
        //when
        Mockito.when(schedulerService.addNewJob(any(NotificationMessage.class))).thenReturn(null);
        //
        this.mockMvc.perform(post("/notifications/api/add").contentType("application/json").content(JSON).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.success").value(false));
    }
}

