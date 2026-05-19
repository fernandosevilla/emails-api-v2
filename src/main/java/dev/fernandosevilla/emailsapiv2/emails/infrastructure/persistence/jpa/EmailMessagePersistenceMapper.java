package dev.fernandosevilla.emailsapiv2.emails.infrastructure.persistence.jpa;

import dev.fernandosevilla.emailsapiv2.emails.domain.EmailAddress;
import dev.fernandosevilla.emailsapiv2.emails.domain.EmailMessage;
import dev.fernandosevilla.emailsapiv2.emails.domain.EmailStatus;

/**
 * Mapper de persistencia para emails.
 *
 * <p>
 *     Esta clase se encarga de convertir entre el modelo del
 *     dominio {@link EmailMessage} y la entidad JPA
 *     {@link EmailMessageJpaEntity}.
 * </p>
 */
public class EmailMessagePersistenceMapper {
    /**
     * Convierte un email de dominio a una entidad JPA.
     *
     * @param emailMessage email de la capa dominio
     * @return entidad JPA preparada para ser persistida
     */
    public EmailMessageJpaEntity toEntity(EmailMessage emailMessage) {
        return new EmailMessageJpaEntity(
                emailMessage.id(),
                emailMessage.recipient().value(),
                emailMessage.subject(),
                emailMessage.body(),
                emailMessage.status().name(),
                emailMessage.createdAt(),
                emailMessage.sentAt(),
                emailMessage.failedAt(),
                emailMessage.failureReason()
        );
    }

    /**
     * Convierte una entidad JPA a un email de la capa dominio.
     *
     * @param emailMessageJpaEntity entidad JPA obtenida desde la BD
     * @return email reconstruido como modelo dominio
     */
    public EmailMessage toDomain(EmailMessageJpaEntity emailMessageJpaEntity) {
        return EmailMessage.restore(
                emailMessageJpaEntity.getId(),
                new EmailAddress(emailMessageJpaEntity.getRecipient()),
                emailMessageJpaEntity.getSubject(),
                emailMessageJpaEntity.getBody(),
                EmailStatus.valueOf(emailMessageJpaEntity.getStatus()),
                emailMessageJpaEntity.getCreatedAt(),
                emailMessageJpaEntity.getSentAt(),
                emailMessageJpaEntity.getFailedAt(),
                emailMessageJpaEntity.getFailureReason()
        );
    }
}
