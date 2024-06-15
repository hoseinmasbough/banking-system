package com.azkivam.banking.exception;

import com.azkivam.banking.exception.model.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleGenericException(final ValidationException ex) {
        return handleAllExceptions(ex, BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleGenericException(final BusinessException ex) {
        return handleAllExceptions(ex, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiError> handleGenericException(final AccountNotFoundException ex) {
        return handleAllExceptions(ex, NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(final Exception ex) {
        return handleAllExceptions((GenericException) ex, INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiError> handleAllExceptions(final GenericException ex, final HttpStatus status) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(generateError(ex), status);
    }

    private ApiError generateError(final GenericException ex) {
        return new ApiError(LocalDateTime.now(), ex.getMessage());
    }
}
