package dev.fernandosevilla.emailsapiv2.emails.infrastructure.web.exception;

/**
 * Representa un error de validación asociado a un campo concreto.
 *
 * @param field campo que ha fallado en la validacion
 * @param message mensaje del error
 */
public record FieldValidationErrorResponse(
        String field,
        String message
) {
}
