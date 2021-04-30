package com.epam.esm.controller;

import com.epam.esm.dao.exception.DataException;
import com.epam.esm.dao.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionController {

    private final ResourceBundleMessageSource messageSource;

    @Autowired
    public GlobalExceptionController(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception, Locale locale) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("errorMessage", messageSource.getMessage(defineTypeMessage(exception.getErrorCode()), null, locale));
        parameters.put("errorCode", exception.getErrorCode());
        return new ResponseEntity<>(parameters, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({DataException.class})
    public ResponseEntity<Object> handleDataException(DataException exception, Locale locale) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("errorMessage", messageSource.getMessage(defineTypeMessage(exception.getErrorCode()), null, locale));
        parameters.put("errorCode", exception.getErrorCode());
        return new ResponseEntity<>(parameters, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(Locale locale) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("errorMessage", messageSource.getMessage("exception.message.50001", null, locale));
        parameters.put("errorCode", 50001);
        return new ResponseEntity<>(parameters, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String defineTypeMessage(int code) {
        return "exception.message." + code;
    }
}
