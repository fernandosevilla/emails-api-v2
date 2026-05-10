package dev.fernandosevilla.emailsapiv2.emails.application.service;

import dev.fernandosevilla.emailsapiv2.emails.application.port.in.SendEmailCommand;
import dev.fernandosevilla.emailsapiv2.emails.application.port.in.SendEmailUseCase;
import dev.fernandosevilla.emailsapiv2.emails.application.port.out.EmailMessageRepositoryPort;
import dev.fernandosevilla.emailsapiv2.emails.application.port.out.EmailSenderPort;
import dev.fernandosevilla.emailsapiv2.emails.domain.EmailAddress;
import dev.fernandosevilla.emailsapiv2.emails.domain.EmailMessage;

import java.util.Objects;
import java.util.UUID;

/**
 * Servicio de aplicación que implementa el caso de uso de envío
 * de emails.
 *
 * <p>
 *     Este coordina el flujo de envío usando el dominio y los puertos
 *     de salida definidos por la aplicación.
 * </p>
 */
public class SendEmailService implements SendEmailUseCase {
    private final EmailMessageRepositoryPort emailMessageRepositoryPort;
    private final EmailSenderPort emailSenderPort;

    /**
     * Constructor.
     *
     * @param emailMessageRepositoryPort puerto de salida para persistir
     *                                   emails
     * @param emailSenderPort puerto de salida para enviar emails
     */
    public SendEmailService(
            EmailMessageRepositoryPort emailMessageRepositoryPort,
            EmailSenderPort emailSenderPort
    ) {
        this.emailMessageRepositoryPort = Objects.requireNonNull(
                emailMessageRepositoryPort,
                "Email message repository cannot be null"
        );

        this.emailSenderPort = Objects.requireNonNull(
                emailSenderPort,
                "Email sender port cannot be null"
        );
    }

    /**
     * Ejecuta el caso de uso de envío de un email.
     *
     * @param command datos para enviar el email
     * @return ID del email creado
     */
    @Override
    public UUID send(SendEmailCommand command) {
        Objects.requireNonNull(command, "Send email command cannot be null");

        EmailMessage emailMessage = EmailMessage.create(
                new EmailAddress(command.recipient()),
                command.subject(),
                command.body()
        );

        emailMessageRepositoryPort.save(emailMessage);

        try {
            emailSenderPort.send(emailMessage);
            emailMessage.markAsSent();
        } catch (Exception e) {
            emailMessage.markAsFailed(resolveFailureReason(e));
        }

        emailMessageRepositoryPort.save(emailMessage);

        return emailMessage.id();
    }

    /**
     * Obtiene un motivo de fallo seguro a partir de una excepción.
     *
     * @param exception excepción producida durante el envío
     * @return mensaje de error usable dentro del dominio
     */
    private String resolveFailureReason(Exception exception) {
        String message = exception.getMessage();

        if (message == null || message.isBlank())
            return exception.getClass().getSimpleName();

        return message;
    }
}
