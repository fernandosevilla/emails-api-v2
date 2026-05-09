package dev.fernandosevilla.emailsapiv2.emails.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa un mensaje de email dentro del dominio de la aplicación.
 *
 * Un email se crea inicialmente en estado {@link EmailStatus#PENDING} y
 * despues se puede marcar como enviado {@link #markAsSent()} o 
 * fallido {@link #markAsFailed(String)}.
 */
public class EmailMessage {
    private final UUID id;
    private final EmailAddress recipient;
    private final String subject;
    private final String body;
    private EmailStatus status;
    private final Instant createdAt;
    private Instant sentAt;
    private Instant failedAt;
    private String failureReason;

    /**
     * Instancia de {@code EmailMessage}.
     * 
     * Hacemos el constructor privado para obligar a crear mensajes
     * nuevos mediante {@link #create(EmailAddress, String, String)} o
     * reconstruir mensajes que ya existen mediante
     * {@link #restore(UUID, EmailAddress, String, String, EmailStatus, Instant, Instant, Instant, String)}.
     * 
     * @param id ID único
     * @param recipient destinatario del email
     * @param subject asunto del email
     * @param body body del email
     * @param status estado del email
     * @param createdAt fecha de creación del email
     * @param sentAt fecha de envío del email
     * @param failedAt fecha en la que el email ha fallado
     * @param failureReason motivo del fallo
     */
    private EmailMessage(
            UUID id,
            EmailAddress recipient,
            String subject,
            String body,
            EmailStatus status,
            Instant createdAt,
            Instant sentAt,
            Instant failedAt,
            String failureReason
    ) {
        this.id = Objects.requireNonNull(id, "Email id cannot be null");
        this.recipient = Objects.requireNonNull(recipient, "Email recipient cannot be null");
        this.subject = validateRequiredText(subject, "Email subject");
        this.body = validateRequiredText(body, "Email body");
        this.status = Objects.requireNonNull(status, "Email status cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Email createdAt cannot be null");
        this.sentAt = sentAt;
        this.failedAt = failedAt;
        this.failureReason = failureReason;
    }

    /**
     * Crea un nuevo mensaje en estado pendiente.
     *
     * @param recipient destinatario del email
     * @param subject asunto del email
     * @param body body del email
     * @return nuevo mensaje de email en estado
     * {@link EmailStatus#PENDING}
     */
    public static EmailMessage create(
            EmailAddress recipient,
            String subject,
            String body
    ) {
        return new EmailMessage(
                UUID.randomUUID(),
                recipient,
                subject,
                body,
                EmailStatus.PENDING,
                Instant.now(),
                null,
                null,
                null
        );
    }

    /**
     * Reconstruye un mensaje de email que ya existe.
     *
     * @param id ID único
     * @param recipient destinatario del email
     * @param subject asunto del email
     * @param body body del email
     * @param status estado del email
     * @param createdAt fecha de creación del email
     * @param sentAt fecha de envío del email
     * @param failedAt fecha en la que el email ha fallado
     * @param failureReason motivo del fallo
     * @return mensaje de email reconstruido.
     */
    public static EmailMessage restore(
            UUID id,
            EmailAddress recipient,
            String subject,
            String body,
            EmailStatus status,
            Instant createdAt,
            Instant sentAt,
            Instant failedAt,
            String failureReason
    ) {
        return new EmailMessage(
                id,
                recipient,
                subject,
                body,
                status,
                createdAt,
                sentAt,
                failedAt,
                failureReason
        );
    }

    /**
     * Marca el email como que se ha enviado correctamente.
     *
     * Al marcarlo como enviado,se registra la fecha de envío
     * y se limpian los datos relacionados con posibles fallos anteriores.
     */
    public void markAsSent() {
        this.status = EmailStatus.SENT;
        this.sentAt = Instant.now();
        this.failedAt = null;
        this.failureReason = null;
    }

    /**
     * Marca el email como fallido.
     *
     * @param reason motivo por el que ha fallado el envío
     */
    public void markAsFailed(String reason) {
        this.status = EmailStatus.FAILED;
        this.failedAt = Instant.now();
        this.failureReason = reason;
    }

    /**
     * Devuelve el ID del email
     * @return ID del email
     */
    public UUID id() {
        return id;
    }

    /**
     * Devuelve el destinatario del email.
     * @return destinatario del email.
     */
    public EmailAddress recipient() {
        return recipient;
    }

    /**
     * Devuelve el asunto del email.
     * @return asunto del email
     */
    public String subject() {
        return subject;
    }

    /**
     * Devuelve el body del email.
     * @return body del email.
     */
    public String body() {
        return body;
    }

    /**
     * Devuelve el estado del email.
     * @return estado del email
     */
    public EmailStatus status() {
        return status;
    }

    /**
     * Devuelve la fecha de creación del email.
     * @return fecha de creación del email
     */
    public Instant createdAt() {
        return createdAt;
    }

    /**
     * Devuelve la fecha de envío del email.
     * @return fecha de envío del email
     */
    public Instant sentAt() {
        return sentAt;
    }

    /**
     * Devuelve la fecha en la que falló el envío del email.
     * @return fecha de fallo del envio del email.
     */
    public Instant failedAt() {
        return failedAt;
    }

    /**
     * Devuelve la razón del fallo del envío del email.
     * @return razón del fallo del envío del email
     */
    public String failureReason() {
        return failureReason;
    }

    /**
     * Valida que un texto sea obligatorio, no sea nulo
     * ni esté vacío.
     *
     * @param value valor a validar
     * @param fieldName nombre del atributo usado en el mensaje de error
     *
     * @return texto validado y trimeado.
     *
     * @throws NullPointerException si el valor es nulo.
     * @throws IllegalArgumentException si el valor está vacío.
     */
    private static String validateRequiredText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");

        String trimmedValue = value.trim();

        if (trimmedValue.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }

        return trimmedValue;
    }
}
