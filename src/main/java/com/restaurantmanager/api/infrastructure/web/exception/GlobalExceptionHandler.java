package com.restaurantmanager.api.infrastructure.web.exception;

import com.restaurantmanager.api.domain.exception.ConflictException;
import com.restaurantmanager.api.domain.exception.DomainException;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String PROBLEM_BASE_URL = "https://api.restaurantmanager.com/problems/";


    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        logger.warn("Entity not found: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            ex.getMessage()
        );
        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "entity-not-found"));
        problemDetail.setTitle("Entity Not Found");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problemDetail.setProperty("timestamp", Instant.now());

        if (ex.getEntityType() != null) {
            problemDetail.setProperty("entityType", ex.getEntityType());
        }
        if (ex.getEntityId() != null) {
            problemDetail.setProperty("entityId", ex.getEntityId());
        }

        return problemDetail;
    }


    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidationException(ValidationException ex, WebRequest request) {
        logger.warn("Validation error: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
        );
        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "validation-error"));
        problemDetail.setTitle("Validation Error");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problemDetail.setProperty("timestamp", Instant.now());

        if (ex.getField() != null) {
            problemDetail.setProperty("field", ex.getField());
        }
        if (ex.getRejectedValue() != null) {
            problemDetail.setProperty("rejectedValue", ex.getRejectedValue());
        }

        return problemDetail;
    }


    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException ex, WebRequest request) {
        logger.warn("Conflict: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT,
            ex.getMessage()
        );
        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "conflict"));
        problemDetail.setTitle("Conflict");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problemDetail.setProperty("timestamp", Instant.now());

        if (ex.getResource() != null) {
            problemDetail.setProperty("resource", ex.getResource());
        }
        if (ex.getReason() != null) {
            problemDetail.setProperty("reason", ex.getReason());
        }

        return problemDetail;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        WebRequest request
    ) {
        logger.warn("Method argument validation failed: {}", ex.getMessage());

        String errorMessage = "Validation failed for request body";
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            errorMessage
        );
        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "invalid-request"));
        problemDetail.setTitle("Invalid Request");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problemDetail.setProperty("timestamp", Instant.now());


        var fieldErrors = ex.getBindingResult().getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            problemDetail.setProperty("errors", fieldErrors.stream()
                .map(error -> String.format(
                    "%s: %s (rejected value: '%s')",
                    error.getField(),
                    error.getDefaultMessage(),
                    error.getRejectedValue()
                ))
                .toList()
            );
        }

        return problemDetail;
    }


    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomainException(DomainException ex, WebRequest request) {
        logger.error("Domain exception occurred: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
        );
        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "domain-error"));
        problemDetail.setTitle("Domain Error");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred", ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred. Please contact support if the problem persists."
        );
        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "internal-error"));
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}

