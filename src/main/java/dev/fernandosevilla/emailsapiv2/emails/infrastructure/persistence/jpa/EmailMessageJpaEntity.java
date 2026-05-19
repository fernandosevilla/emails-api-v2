package dev.fernandosevilla.emailsapiv2.emails.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Entidad JPA que representa la tabla {@code email_messages}.
 */
@Entity
@Table(name = "email_messages")
public class EmailMessageJpaEntity {
    @Id
    private UUID id;

    @Column(nullable = false, length = 320)
    private String recipient;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "failed_at")
    private Instant failedAt;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    protected EmailMessageJpaEntity() {
    }

    public EmailMessageJpaEntity(
            UUID id,
            String recipient,
            String subject,
            String body,
            String status,
            Instant createdAt,
            Instant sentAt,
            Instant failedAt,
            String failureReason
    ) {
        this.id = id;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.status = status;
        this.createdAt = createdAt;
        this.sentAt = sentAt;
        this.failedAt = failedAt;
        this.failureReason = failureReason;
    }

    public UUID getId() {
        return id;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public Instant getFailedAt() {
        return failedAt;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
