package dev.fernandosevilla.emailsapiv2.emails.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repositorio Spring Data JPA para la entidad
 * {@link EmailMessageJpaEntity}.
 */
public interface SpringDataEmailMessageRepository extends JpaRepository<EmailMessageJpaEntity, UUID> {
}
