package com.example.etl_off.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Converts common REST parameter errors into readable HTTP responses.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Handles missing mandatory query parameters.
     *
     * @param exception Spring MVC exception
     * @return HTTP 400 response
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParameter(MissingServletRequestParameterException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Parametre obligatoire manquant: " + exception.getParameterName()));
    }

    /**
     * Handles invalid business parameters.
     *
     * @param exception validation exception
     * @return HTTP 400 response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", exception.getMessage()));
    }
}
