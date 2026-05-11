package dev.fernandosevilla.emailsapiv2.emails.infrastructure.persistence.inmemory;

import dev.fernandosevilla.emailsapiv2.emails.application.port.out.EmailMessageRepositoryPort;
import dev.fernandosevilla.emailsapiv2.emails.domain.EmailMessage;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adaptador de persistencia en memoria para menasjes de email.
 *
 * <p>
 *     Esta implementación guarda los emails en una estructura
 *     de datos ({@code ConcurrentHashMap<UUID, EmailMessage>},
 *     por lo que la información se pierde al reiniciar.
 * </p>
 */
public class InMemoryEmailMessageRepositoryAdapter implements EmailMessageRepositoryPort {
    private final Map<UUID, EmailMessage> emails = new ConcurrentHashMap<>();

    /**
     * Guarda un mensaje de email en memoria.
     *
     * @param emailMessage mensaje de email para guardar
     */
    @Override
    public void save(EmailMessage emailMessage) {
        emails.put(emailMessage.id(), emailMessage);
    }
}
