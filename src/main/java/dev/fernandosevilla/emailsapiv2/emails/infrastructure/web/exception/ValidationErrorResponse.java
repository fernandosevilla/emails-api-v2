package dev.fernandosevilla.emailsapiv2.emails.infrastructure.web.exception;

import java.time.Instant;
import java.util.List;

/**
 * Respuesta HTTP devuelta cuando una petición no supera
 * las validaciones.
 *
 * @param timestamp fecha que se produjo el error
 * @param status código de error HTTP
 * @param error descripción general del error
 * @param path ruta de la petición
 * @param errors lista de errores de validación por cada campo
 */
public record ValidationErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String path,
        List<FieldValidationErrorResponse> errors
) {
}
