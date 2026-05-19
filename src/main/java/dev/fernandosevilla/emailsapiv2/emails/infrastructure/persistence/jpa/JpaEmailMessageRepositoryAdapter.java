package dev.fernandosevilla.emailsapiv2.emails.infrastructure.persistence.jpa;

import dev.fernandosevilla.emailsapiv2.emails.application.port.out.EmailMessageRepositoryPort;
import dev.fernandosevilla.emailsapiv2.emails.domain.EmailMessage;
import org.springframework.stereotype.Repository;

/**
 * Adaptador JPA del puerto de persistencia de emails.
 *
 * <p>
 *     Esta clase implementa el puerto {@link EmailMessageRepositoryPort}
 *     utilizando Spring Data JPA como tecnología de persistencia.
 * </p>
 */
@Repository
public class JpaEmailMessageRepositoryAdapter implements EmailMessageRepositoryPort {
    private final SpringDataEmailMessageRepository repository;
    private final EmailMessagePersistenceMapper mapper;

    /**
     * Constructor del adaptador JPA de persistencia de emails.
     *
     * @param repository repositorio Spring Data encargado de persistir
     *                   entidades JPA
     */
    public JpaEmailMessageRepositoryAdapter(
            SpringDataEmailMessageRepository repository
    ) {
        this.repository = repository;
        this.mapper = new EmailMessagePersistenceMapper();
    }

    /**
     * Guarda un mensaje de email en BBDD.
     *
     * @param emailMessage mensaje de email para guardar
     */
    @Override
    public void save(EmailMessage emailMessage) {
        EmailMessageJpaEntity entity = mapper.toEntity(emailMessage);
        repository.save(entity);
    }
}
