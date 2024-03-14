package com.briandidthat.econserver.controller;

import com.briandidthat.econserver.domain.exception.RetrievalException;
import com.briandidthat.econserver.domain.exception.BadRequestException;
import com.briandidthat.econserver.domain.exception.ExceptionDetails;
import com.briandidthat.econserver.domain.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<Error> handleResourceNotFoundException(Exception e, WebRequest request) {
        ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    private ResponseEntity<Error> handleMissingParamException(Exception e, WebRequest request) {
        ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    private ResponseEntity<Error> handleBadRequestException(Exception e, WebRequest request) {
        ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<Error> handleConstrainViolationException(Exception e, WebRequest request) {
        ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity(details, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        List<ExceptionDetails> violations = new ArrayList<>();
        LocalDateTime date = LocalDateTime.now();

        for (FieldError violation : e.getFieldErrors()) {
            String message = violation.getField() + ": " + violation.getDefaultMessage();
            ExceptionDetails details = new ExceptionDetails(date, message, request.getDescription(false));
            violations.add(details);
        }

        return new ResponseEntity(violations, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(RetrievalException.class)
    private ResponseEntity<Error> handleRetrievalException(Exception e, WebRequest request) {
        ExceptionDetails details = new ExceptionDetails(LocalDateTime.now(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}