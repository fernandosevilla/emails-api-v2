package dev.fernandosevilla.emailsapiv2.emails.infrastructure.mail.fake;

import dev.fernandosevilla.emailsapiv2.emails.application.port.out.EmailSenderPort;
import dev.fernandosevilla.emailsapiv2.emails.domain.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adaptador fake para el envío de emails.
 *
 * <p>
 *     Esta implementación NO envía emails reales. Su objetivo
 *     es simular el comportamiento de un proveedor de envío
 *     mediante logs.
 * </p>
 */
public class FakeEmailSenderAdapter implements EmailSenderPort {
    private static final Logger log = LoggerFactory.getLogger(FakeEmailSenderAdapter.class);

    /**
     * Simula el envío de un email registrando sus datos
     * principales en logs.
     *
     * @param emailMessage email a enviar
     */
    @Override
    public void send(EmailMessage emailMessage) {
        log.info(
                "Fake email sent. id = {}, recipient = {}, subject = {}",
                emailMessage.id(),
                emailMessage.recipient().value(),
                emailMessage.subject()
        );
    }
}
