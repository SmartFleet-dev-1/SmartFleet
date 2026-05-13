package com.edutech.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

     // Add the required code here!

     private Map<String, Object> buildResponse(HttpStatus status, String error,
               String message, String details) {
          Map<String, Object> res = new HashMap<>();
          res.put("timestamp", LocalDateTime.now());
          res.put("status", status.value());
          res.put("error", error);
          res.put("message", message);
          res.put("details", details);
          return res;
     }

     // 404 - Resource Not Found
     @ExceptionHandler(ResourceNotFoundException.class)
     public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex, WebRequest req) {
          return new ResponseEntity<>(
                    buildResponse(HttpStatus.NOT_FOUND, "Not Found",
                              ex.getMessage(), req.getDescription(false)),
                    HttpStatus.NOT_FOUND);
     }

     // 409 - Duplicate Data
     @ExceptionHandler(DuplicateResourceException.class)
     public ResponseEntity<?> handleDuplicate(DuplicateResourceException ex, WebRequest req) {
          return new ResponseEntity<>(
                    buildResponse(HttpStatus.CONFLICT, "Conflict",
                              ex.getMessage(), req.getDescription(false)),
                    HttpStatus.CONFLICT);
     }

     // 400 - Invalid Data
     @ExceptionHandler(InvalidDataException.class)
     public ResponseEntity<?> handleInvalid(InvalidDataException ex, WebRequest req) {
          return new ResponseEntity<>(
                    buildResponse(HttpStatus.BAD_REQUEST, "Bad Request",
                              ex.getMessage(), req.getDescription(false)),
                    HttpStatus.BAD_REQUEST);
     }

     // 400 - Validation Errors (@Valid)
     @ExceptionHandler(MethodArgumentNotValidException.class)
     public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, WebRequest req) {

          // Collect all field errors
          String errorMsg = ex.getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(e -> ((FieldError) e).getField() + ": " + e.getDefaultMessage())
                    .collect(Collectors.joining(", "));

          return new ResponseEntity<>(
                    buildResponse(HttpStatus.BAD_REQUEST, "Validation Failed",
                              errorMsg, req.getDescription(false)),
                    HttpStatus.BAD_REQUEST);
     }

     // 401 - Wrong credentials
     @ExceptionHandler(BadCredentialsException.class)
     public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex, WebRequest req) {
          return new ResponseEntity<>(
                    buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized",
                              "Invalid username or password", req.getDescription(false)),
                    HttpStatus.UNAUTHORIZED);
     }

     // 403 - Access denied
     @ExceptionHandler(AccessDeniedException.class)
     public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex, WebRequest req) {
          return new ResponseEntity<>(
                    buildResponse(HttpStatus.FORBIDDEN, "Forbidden",
                              "You are not authorized to access this resource",
                              req.getDescription(false)),
                    HttpStatus.FORBIDDEN);
     }

     // 500 - General Exception
     @ExceptionHandler(Exception.class)
     public ResponseEntity<?> handleGeneral(Exception ex, WebRequest req) {
          return new ResponseEntity<>(
                    buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                              "Internal Server Error", ex.getMessage(),
                              req.getDescription(false)),
                    HttpStatus.INTERNAL_SERVER_ERROR);
     }
}
