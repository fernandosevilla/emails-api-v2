package dev.fernandosevilla.emailsapiv2.emails.application.port.in;

import java.util.UUID;

/**
 * Puerto de entrada para el caso de uso de envío de emails.
 */
public interface SendEmailUseCase {
    /**
     * Envía un email a partir de los datos recibidos en el command.
     *
     * @param command datos para enviar el email
     * @return ID del email creado
     */
    UUID send(SendEmailCommand command);
}
