package in.goldi.creatorstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==========================================
    // RESOURCE NOT FOUND - 404
    // ==========================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(
            ResourceNotFoundException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        errorResponse(
                                404,
                                ex.getMessage()
                        )
                );
    }


    // ==========================================
    // INSUFFICIENT STOCK - 400
    // ==========================================

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<?> handleStock(
            InsufficientStockException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        errorResponse(
                                400,
                                ex.getMessage()
                        )
                );
    }


    // ==========================================
    // VALIDATION ERROR - 400
    // ==========================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }


    // ==========================================
    // ILLEGAL ARGUMENT - 400
    // ==========================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(
            IllegalArgumentException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        errorResponse(
                                400,
                                ex.getMessage()
                        )
                );
    }


    // ==========================================
    // ILLEGAL STATE - 400
    // ==========================================

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalState(
            IllegalStateException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        errorResponse(
                                400,
                                ex.getMessage()
                        )
                );
    }


    // ==========================================
    // GENERAL EXCEPTION - 500
    // ==========================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(
            Exception ex
    ) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        errorResponse(
                                500,
                                "An unexpected error occurred"
                        )
                );
    }


    // ==========================================
    // COMMON ERROR RESPONSE
    // ==========================================

    private Map<String, Object> errorResponse(
            int status,
            String message
    ) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        response.put(
                "status",
                status
        );

        response.put(
                "message",
                message
        );

        return response;
    }
}