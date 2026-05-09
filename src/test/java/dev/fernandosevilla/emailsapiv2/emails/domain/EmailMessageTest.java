package dev.fernandosevilla.emailsapiv2.emails.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailMessageTest {
    @Test
    void shouldCreatePendingEmailMessage() {
        EmailAddress recipient = new EmailAddress("cliente@cliente.com");

        EmailMessage emailMessage = EmailMessage.create(
                recipient,
                " Bienvenido ",
                " Hola Fernando "
        );

        assertThat(emailMessage.id()).isNotNull();
        assertThat(emailMessage.recipient()).isEqualTo(recipient);
        assertThat(emailMessage.subject()).isEqualTo("Bienvenido");
        assertThat(emailMessage.body()).isEqualTo("Hola Fernando");
        assertThat(emailMessage.status()).isEqualTo(EmailStatus.PENDING);
        assertThat(emailMessage.createdAt()).isNotNull();
        assertThat(emailMessage.sentAt()).isNull();
        assertThat(emailMessage.failedAt()).isNull();
        assertThat(emailMessage.failureReason()).isNull();
    }

    @Test
    void shouldMarkEmailMessageAsSent() {
        EmailMessage emailMessage = EmailMessage.create(
                new EmailAddress("cliente@cliente.com"),
                "Bienvenido",
                "Hola Fernando"
        );

        emailMessage.markAsSent();

        assertThat(emailMessage.status()).isEqualTo(EmailStatus.SENT);
        assertThat(emailMessage.sentAt()).isNotNull();
        assertThat(emailMessage.failedAt()).isNull();
        assertThat(emailMessage.failureReason()).isNull();
    }

    @Test
    void shouldMarkEmailMessageAsFailed() {
        EmailMessage emailMessage = EmailMessage.create(
                new EmailAddress("cliente@cliente.com"),
                "Bienvenido",
                "Hola Fernando"
        );

        emailMessage.markAsFailed("SMTP error");

        assertThat(emailMessage.status()).isEqualTo(EmailStatus.FAILED);
        assertThat(emailMessage.failedAt()).isNotNull();
        assertThat(emailMessage.failureReason()).isEqualTo("SMTP error");
    }

    @Test
    void shouldRestoreExistingEmailMessage() {
        UUID id = UUID.randomUUID();
        EmailAddress recipient = new EmailAddress("cliente@cliente.com");
        Instant createdAt = Instant.parse("2026-05-09T20:57:00Z");
        Instant sentAt = Instant.parse("2026-05-09T20:58:00Z");

        EmailMessage emailMessage = EmailMessage.restore(
                id,
                recipient,
                "Bienvenido",
                "Hola Fernando",
                EmailStatus.SENT,
                createdAt,
                sentAt,
                null,
                null
        );

        assertThat(emailMessage.id()).isEqualTo(id);
        assertThat(emailMessage.recipient()).isEqualTo(recipient);
        assertThat(emailMessage.subject()).isEqualTo("Bienvenido");
        assertThat(emailMessage.body()).isEqualTo("Hola Fernando");
        assertThat(emailMessage.status()).isEqualTo(EmailStatus.SENT);
        assertThat(emailMessage.createdAt()).isEqualTo(createdAt);
        assertThat(emailMessage.sentAt()).isEqualTo(sentAt);
        assertThat(emailMessage.failedAt()).isNull();
        assertThat(emailMessage.failureReason()).isNull();
    }

    @Test
    void shouldRejectBlankSubject() {
        assertThatThrownBy(() -> EmailMessage.create(
                new EmailAddress("cliente@cliente.com"),
                "  ",
                "Hola Fernando"
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email subject cannot be blank");
    }

    @Test
    void shouldRejectBlankBody() {
        assertThatThrownBy(() -> EmailMessage.create(
                new EmailAddress("cliente@cliente.com"),
                "Bienvenido",
                "  "
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email body cannot be blank");
    }

    @Test
    void shouldRejectBlankFailureReason() {
        EmailMessage emailMessage = EmailMessage.create(
                new EmailAddress("cliente@cliente.com"),
                "Bienvenido",
                "Hola Fernando"
        );

        assertThatThrownBy(() -> emailMessage.markAsFailed("  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Failure reason cannot be blank");
    }
}
