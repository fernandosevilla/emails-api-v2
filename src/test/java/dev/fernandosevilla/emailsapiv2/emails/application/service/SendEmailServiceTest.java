package dev.fernandosevilla.emailsapiv2.emails.application.service;

import dev.fernandosevilla.emailsapiv2.emails.application.port.in.SendEmailCommand;
import dev.fernandosevilla.emailsapiv2.emails.application.port.out.EmailMessageRepositoryPort;
import dev.fernandosevilla.emailsapiv2.emails.application.port.out.EmailSenderPort;
import dev.fernandosevilla.emailsapiv2.emails.domain.EmailMessage;
import dev.fernandosevilla.emailsapiv2.emails.domain.EmailStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendEmailServiceTest {
    @Test
    void shouldSendEmailSuccessfully() {
        FakeEmailMessageRepositoryPort repositoryPort = new FakeEmailMessageRepositoryPort();
        FakeEmailSenderPort senderPort = new FakeEmailSenderPort();

        SendEmailService sendEmailService = new SendEmailService(repositoryPort, senderPort);

        UUID emailId = sendEmailService.send(new SendEmailCommand(
                "cliente@cliente.com",
                "Bienvenido",
                "Hola Fernando"
        ));

        assertThat(emailId).isNotNull();

        assertThat(repositoryPort.savedStatuses())
                .containsExactly(EmailStatus.PENDING, EmailStatus.SENT);

        assertThat(senderPort.sentStatuses())
                .containsExactly(EmailStatus.PENDING);

        assertThat(repositoryPort.lastSavedEmail().id())
                .isEqualTo(emailId);

        assertThat(repositoryPort.lastSavedEmail().status())
                .isEqualTo(EmailStatus.SENT);

        assertThat(repositoryPort.lastSavedEmail().sentAt())
                .isNotNull();

        assertThat(repositoryPort.lastSavedEmail().failedAt())
                .isNull();

        assertThat(repositoryPort.lastSavedEmail().failureReason())
                .isNull();
    }

    @Test
    void shouldMarkEmailAsFailedWhenSenderFails() {
        FakeEmailMessageRepositoryPort repositoryPort = new FakeEmailMessageRepositoryPort();
        FakeEmailSenderPort senderPort = new FakeEmailSenderPort();
        senderPort.failWith(new RuntimeException("SMTP error"));

        SendEmailService sendEmailService = new SendEmailService(repositoryPort, senderPort);

        UUID emailId = sendEmailService.send(new SendEmailCommand(
                "cliente@cliente.com",
                "Bienvenido",
                "Hola Fernando"
        ));

        assertThat(emailId).isNotNull();

        assertThat(repositoryPort.savedStatuses())
                .containsExactly(EmailStatus.PENDING, EmailStatus.FAILED);

        assertThat(senderPort.sentStatuses())
                .containsExactly(EmailStatus.PENDING);

        assertThat(repositoryPort.lastSavedEmail().id())
                .isEqualTo(emailId);

        assertThat(repositoryPort.lastSavedEmail().status())
                .isEqualTo(EmailStatus.FAILED);

        assertThat(repositoryPort.lastSavedEmail().sentAt())
                .isNull();

        assertThat(repositoryPort.lastSavedEmail().failedAt())
                .isNotNull();

        assertThat(repositoryPort.lastSavedEmail().failureReason())
                .isEqualTo("SMTP error");
    }

    @Test
    void shouldRejectNullCommand() {
        FakeEmailMessageRepositoryPort repositoryPort = new FakeEmailMessageRepositoryPort();
        FakeEmailSenderPort senderPort = new FakeEmailSenderPort();

        SendEmailService sendEmailService = new SendEmailService(repositoryPort, senderPort);

        assertThatThrownBy(() -> sendEmailService.send(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Send email command cannot be null");

        assertThat(repositoryPort.savedStatuses()).isEmpty();
        assertThat(senderPort.sentStatuses()).isEmpty();
    }

    @Test
    void shouldRejectInvalidRecipient() {
        FakeEmailMessageRepositoryPort repositoryPort = new FakeEmailMessageRepositoryPort();
        FakeEmailSenderPort senderPort = new FakeEmailSenderPort();

        SendEmailService sendEmailService = new SendEmailService(repositoryPort, senderPort);

        assertThatThrownBy(() -> sendEmailService.send(new SendEmailCommand(
                "clientecliente.com",
                "Bienvenido",
                "Hola Fernando"
        )))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email address must be valid");

        assertThat(repositoryPort.savedStatuses()).isEmpty();
        assertThat(senderPort.sentStatuses()).isEmpty();
    }

    private static class FakeEmailMessageRepositoryPort implements EmailMessageRepositoryPort {
        private final List<EmailMessage> savedEmails = new ArrayList<>();
        private final List<EmailStatus> savedStatuses = new ArrayList<>();

        @Override
        public void save(EmailMessage emailMessage) {
            savedEmails.add(emailMessage);
            savedStatuses.add(emailMessage.status());
        }

        List<EmailStatus> savedStatuses() {
            return savedStatuses;
        }

        EmailMessage lastSavedEmail() {
            return savedEmails.getLast();
        }
    }

    private static class FakeEmailSenderPort implements EmailSenderPort {
        private final List<EmailStatus> sentStatuses = new ArrayList<>();
        private RuntimeException exception;

        @Override
        public void send(EmailMessage emailMessage) {
            sentStatuses.add(emailMessage.status());

            if (exception != null) throw exception;
        }

        void failWith(RuntimeException exception) {
            this.exception = exception;
        }

        List<EmailStatus> sentStatuses() {
            return sentStatuses;
        }
    }
}
