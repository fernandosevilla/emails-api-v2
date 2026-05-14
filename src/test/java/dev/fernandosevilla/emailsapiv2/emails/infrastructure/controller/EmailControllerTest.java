package dev.fernandosevilla.emailsapiv2.emails.infrastructure.controller;

import dev.fernandosevilla.emailsapiv2.emails.application.port.in.SendEmailUseCase;
import dev.fernandosevilla.emailsapiv2.emails.infrastructure.web.controller.EmailController;
import dev.fernandosevilla.emailsapiv2.emails.infrastructure.web.exception.WebExceptionHandler;
import dev.fernandosevilla.emailsapiv2.emails.infrastructure.web.request.SendEmailRequest;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmailController.class)
@Import(WebExceptionHandler.class)
class EmailControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SendEmailUseCase sendEmailUseCase;

    @Test
    void shouldSendEmailWhenRequestIsValid() throws Exception {
        UUID emailId = UUID.fromString("f609776c-5148-43ef-93c1-4984aa081b7c");

        SendEmailRequest request = new SendEmailRequest(
                "cliente@cliente.com",
                "Bienvenido",
                "Gracias por registrarte"
        );

        when(sendEmailUseCase.send(any()))
                .thenReturn(emailId);

        mockMvc.perform(post("/api/v1/emails")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/emails/" + emailId))
                .andExpect(jsonPath("$.id").value(emailId.toString()));

        verify(sendEmailUseCase).send(any());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        SendEmailRequest request = new SendEmailRequest(
                "correo-mal",
                "Bienvenido",
                "Gracias por registrarte."
        );

        mockMvc.perform(post("/api/v1/emails")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/v1/emails"))
                .andExpect(jsonPath("$.errors[*].field", hasItem("to")))
                .andExpect(jsonPath("$.errors[*].message", hasItem("El destinatario tiene que tener un formato válido")));

        verifyNoInteractions(sendEmailUseCase);
    }

    @Test
    void shouldReturnBadRequestWhenRequiredFieldAreBlank() throws Exception {
        SendEmailRequest request = new SendEmailRequest(
                "",
                "",
                ""
        );

        mockMvc.perform(post("/api/v1/emails")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/v1/emails"))
                .andExpect(jsonPath("$.errors[*].field", hasItem("to")))
                .andExpect(jsonPath("$.errors[*].field", hasItem("subject")))
                .andExpect(jsonPath("$.errors[*].field", hasItem("content")))
                .andExpect(jsonPath("$.errors[*].message", hasItem("El destinatario es obligatorio")))
                .andExpect(jsonPath("$.errors[*].message", hasItem("El asunto es obligatorio")))
                .andExpect(jsonPath("$.errors[*].message", hasItem("El contenido es obligatorio")));

        verifyNoInteractions(sendEmailUseCase);
    }
}
