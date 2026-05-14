package dev.fernandosevilla.emailsapiv2.emails.infrastructure.web.response;

/**
 * Respuesta HTTP que se devuelve tras solicitar el envío de
 * un email.
 *
 * @param id ID del email creado
 */
public record SendEmailResponse(
        String id
) {
}
