package dev.fernandosevilla.emailsapiv2.emails.application.port.in;

/**
 * Comando de entrada para solicitar el envío de un email.
 *
 * <p>Representa los datos que necesita el caso de uso para crear
 * y enviar un mensaje de email.</p>
 *
 * @param recipient email del destinatario
 * @param subject asunto del email
 * @param body body del email
 */
public record SendEmailCommand(
        String recipient,
        String subject,
        String body
) {
}
