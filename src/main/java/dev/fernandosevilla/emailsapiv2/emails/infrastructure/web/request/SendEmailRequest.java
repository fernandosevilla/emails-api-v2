package dev.fernandosevilla.emailsapiv2.emails.infrastructure.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Petición HTTP para enviar un email a un destinatario.
 *
 * @param to destinatario del email
 * @param subject asunto del email
 * @param content contenido (body) del email
 */
public record SendEmailRequest(
        @NotBlank(message = "El destinatario es obligatorio")
        @Email(message = "El destinatario tiene que tener un formato válido")
        String to,

        @NotBlank(message = "El asunto es obligatorio")
        @Size(max = 255, message = "El asunto no debe pasar de los 255 caracteres")
        String subject,

        @NotBlank(message = "El contenido es obligatorio")
        String content
) {
}
