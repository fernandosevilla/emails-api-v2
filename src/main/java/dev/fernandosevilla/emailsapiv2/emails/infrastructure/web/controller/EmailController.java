package dev.fernandosevilla.emailsapiv2.emails.infrastructure.web.controller;

import dev.fernandosevilla.emailsapiv2.emails.application.port.in.SendEmailCommand;
import dev.fernandosevilla.emailsapiv2.emails.application.port.in.SendEmailUseCase;
import dev.fernandosevilla.emailsapiv2.emails.infrastructure.web.request.SendEmailRequest;
import dev.fernandosevilla.emailsapiv2.emails.infrastructure.web.response.SendEmailResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Controller REST para la gestión de emails.
 */
@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {
    private final SendEmailUseCase sendEmailUseCase;

    /**
     * Constructor del controller.
     *
     * @param sendEmailUseCase caso de uso encargado de coordinar
     *                         el envío de emails
     */
    public EmailController(SendEmailUseCase sendEmailUseCase) {
        this.sendEmailUseCase = sendEmailUseCase;
    }

    /**
     * Recibe una petición HTTP para enviar UN email.
     *
     * <p>
     *     Convierte la petición HTTP en un {@link SendEmailCommand},
     *     delega la operación en el caso de uso {@link SendEmailUseCase}
     *     y devuelve una respuesta HTTP con el ID del email creado.
     * </p>
     *
     * @param request datos recibidos en la petición HTTP
     * @return respuesta HTTP 201 Created con el ID del email
     */
    @PostMapping
    public ResponseEntity<SendEmailResponse> sendEmail(
            @Valid @RequestBody SendEmailRequest request
    ) {
        SendEmailCommand command = new SendEmailCommand(
                request.to(),
                request.subject(),
                request.content()
        );

        var emailId = sendEmailUseCase.send(command);

        SendEmailResponse response = new SendEmailResponse(emailId.toString());

        URI location = URI.create("/api/v1/emails/" + response.id());

        return ResponseEntity
                .created(location)
                .body(response);
    }
}
