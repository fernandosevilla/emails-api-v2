package dev.fernandosevilla.emailsapiv2.emails.application.port.out;

import dev.fernandosevilla.emailsapiv2.emails.domain.EmailMessage;

/**
 * Puerto de salida para enviar emails sin saber aun mediante que,
 * respetando la arquitectura hexagonal.
 */
public interface EmailSenderPort {
    /**
     * Envía un email.
     *
     * @param emailMessage email a enviar
     */
    void send(EmailMessage emailMessage);
}
