package dev.fernandosevilla.emailsapiv2.emails.application.port.out;

import dev.fernandosevilla.emailsapiv2.emails.domain.EmailMessage;

/**
 * Puerto de salida para persistir mensajes de email.
 */
public interface EmailMessageRepositoryPort {
    /**
     * Guarda un mensaje de email.
     *
     * @param emailMessage mensaje de email para guardar
     */
    void save(EmailMessage emailMessage);
}
