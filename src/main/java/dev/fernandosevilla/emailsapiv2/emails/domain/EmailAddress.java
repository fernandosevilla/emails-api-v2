package dev.fernandosevilla.emailsapiv2.emails.domain;

import java.util.Objects;

/**
 * Representa una dirección de email válida dentro del dominio.
 *
 * Se ha hecho como object porque una dirección de email no es
 * símplemente un {@link String}, si no que debe cumplir algunas reglas
 * mínimas para que esté dentro del sistema.
 * @param value dirección de email.
 */
public record EmailAddress(String value) {

    /**
     * Crea una dirección de email validada y normalizada.
     *
     * @throws NullPointerException si el email es nulo
     * @throws IllegalArgumentException si el email está vacío o no
     * contiene {@code @}.
     */
    public EmailAddress {
        Objects.requireNonNull(value, "Email address cannot be null");

        String normalizedEmail = value.trim().toLowerCase();

        if (normalizedEmail.isBlank()) {
            throw new IllegalArgumentException("Email address cannot be blank");
        }

        if (!normalizedEmail.contains("@")) {
            throw new IllegalArgumentException("Email address must be valid");
        }

        value = normalizedEmail;
    }
}
