package dev.fernandosevilla.emailsapiv2.emails.infrastructure.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

/**
 * Manejador global de excepciones del adaptador web.
 *
 * <p>
 *     Centraliza la forma en la que la API REST devuelve los
 *     errores HTTP.
 * </p>
 *
 * <p>
 *     De esta manera evitamos duplicar la lógica de gestión de
 *     errores en los controllers y mantenemos una respuesta consistente
 *     para los clientes.
 * </p>
 */
@RestControllerAdvice
public class WebExceptionHandler {
    /**
     * Maneja los errores producidos por la validación de los DTOs
     * de entrada.
     *
     * <p>
     *     Este metodo captura las excepciones lanzadas cuando una
     *     petición anotada con {@code @Valid} no cumple las restricciones
     *     definidas ({@code @NotBlank}, {@code @Email}, {@code @Size}...).
     * </p>
     *
     * <p>
     *     A partir de los errores de validación, construye una respuesta
     *     HTTP personalizada con el estado {@code 400 Bad Request}, la
     *     ruta de la petición y el listado de campos que no han superado
     *     la validación.
     * </p>
     *
     * @param exception excepción generada por Spring cuando falla la validación
     * @param request petición HTTP original
     * @return respueta HTTP con el detalle de los errores de validación
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<FieldValidationErrorResponse> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldValidationErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ValidationErrorResponse response = new ValidationErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                request.getRequestURI(),
                errors
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }
}
