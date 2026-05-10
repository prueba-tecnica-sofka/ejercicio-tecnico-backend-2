// GlobalExceptionHandler.java
package com.banco.api.exception;  
  
import org.springframework.http.HttpStatus;  
import org.springframework.http.ResponseEntity;  
import org.springframework.validation.FieldError;  
import org.springframework.web.bind.MethodArgumentNotValidException;  
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;  
import org.springframework.web.bind.annotation.RestControllerAdvice;  
  
import java.time.LocalDateTime;  
import java.util.Arrays;
import java.util.HashMap;  
import java.util.Map;  
  
@RestControllerAdvice  
public class GlobalExceptionHandler {  

    private static ErrorResponse buildErrorResponse(
            HttpStatus status,
            String error,
            String message,
            Map<String, String> details) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .details(details)
                .build();
    }
  
    @ExceptionHandler(ResourceNotFoundException.class)  
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {  
        ErrorResponse error = buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage(),
                null);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);  
    }  
  
    @ExceptionHandler(SaldoInsuficienteException.class)  
    public ResponseEntity<ErrorResponse> handleSaldoInsuficiente(SaldoInsuficienteException ex) {  
        ErrorResponse error = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Saldo no disponible",
                ex.getMessage(),
                null);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);  
    }  

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex) {
        ErrorResponse error = buildErrorResponse(
                HttpStatus.CONFLICT,
                "Conflict",
                ex.getMessage(),
                null);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
  
    @ExceptionHandler(MethodArgumentNotValidException.class)  
    public ResponseEntity<ErrorResponse> handleValidationExceptions(  
            MethodArgumentNotValidException ex) {  
        Map<String, String> errors = new HashMap<>();  
        ex.getBindingResult().getAllErrors().forEach((error) -> {  
            String fieldName = ((FieldError) error).getField();  
            String errorMessage = error.getDefaultMessage();  
            errors.put(fieldName, errorMessage);  
        });  

        ErrorResponse error = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                "La solicitud contiene campos invalidos",
                errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);  
    }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex) {
        String allowedMethods = ex.getSupportedMethods() == null
            ? ""
            : String.join(", ", Arrays.asList(ex.getSupportedMethods()));

        String message = allowedMethods.isBlank()
            ? "Metodo HTTP no permitido para este endpoint"
            : "Metodo HTTP no permitido. Metodos soportados: " + allowedMethods;

        ErrorResponse error = buildErrorResponse(
            HttpStatus.METHOD_NOT_ALLOWED,
            "Method Not Allowed",
            message,
            null);
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
        }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        ErrorResponse error = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "Ocurrio un error interno en el servidor",
                null);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }  
}