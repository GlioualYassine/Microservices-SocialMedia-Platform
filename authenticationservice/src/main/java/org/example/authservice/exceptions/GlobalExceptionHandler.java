package org.example.authservice.exceptions;

import jakarta.mail.MessagingException;
import org.example.authservice.exceptions.responses.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exp){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .buisnessErrorCode(BusinessErrorCodes.ACCOUNT_LOCKED.getCode())
                                .buisnessExceptionDescription(BusinessErrorCodes.ACCOUNT_LOCKED.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exp){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .buisnessErrorCode(BusinessErrorCodes.ACCOUNT_DISABLED.getCode())
                                .buisnessExceptionDescription(BusinessErrorCodes.ACCOUNT_DISABLED.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exp){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .error(BusinessErrorCodes.BAD_CREDENTIALS.getDescription())
                                .buisnessErrorCode(BusinessErrorCodes.BAD_CREDENTIALS.getCode())
                                .buisnessExceptionDescription(BusinessErrorCodes.BAD_CREDENTIALS.getDescription())
                                .build()
                );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exp){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exp){

        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getFieldErrors().forEach(
                fieldError -> {
                    var errorMessage = fieldError.getDefaultMessage();
                    errors.add(errorMessage);}
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp){
        //log the exception
        exp.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .buisnessExceptionDescription("Internal Server Error, please contact the administrator")
                                .error(exp.getMessage())
                                .build()
                );
    }

}
