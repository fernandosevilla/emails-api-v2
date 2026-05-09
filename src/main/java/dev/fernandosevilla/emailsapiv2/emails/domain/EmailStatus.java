package dev.fernandosevilla.emailsapiv2.emails.domain;

/**
 * Representa los posibles estados de un mensaje de email.
 */
public enum EmailStatus {
    /**
     * El email ha sido creado, pero no se ha enviado.
     */
    PENDING,

    /**
     * El email se ha enviado.
     */
    SENT,

    /**
     * El envío del email ha fallado.
     */
    FAILED

    // TODO: pondremos más estados según evolucionemos la aplicación.
}
