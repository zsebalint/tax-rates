package com.dk.tax_rates.web;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
@Slf4j
public class BasicExceptionHandler implements ProblemHandling {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Problem> handleConstraintViolation(
      DataIntegrityViolationException ex, NativeWebRequest request) {
    Problem problem =
        Problem.builder()
            .withTitle("Validation Error")
            .withStatus(Status.BAD_REQUEST)
            .withDetail("Tax record with the given municipality and dates already exist")
            .build();

    return ResponseEntity.badRequest().body(problem);
  }

  @Override
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Problem> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, @NonNull NativeWebRequest request) {
    List<Violation> violations =
        ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> new Violation(fieldError.getField(), fieldError.getDefaultMessage()))
            .toList();
    log.error("Error handler 2nd method");
    Problem problem =
        Problem.builder()
            .withTitle("Validation Error")
            .withStatus(Status.BAD_REQUEST)
            .withDetail("Input validation failed")
            .with("violations", violations)
            .build();

    return ResponseEntity.badRequest().body(problem);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Problem> handleConstraintViolation(
      ConstraintViolationException ex, NativeWebRequest request) {
    List<Violation> violations =
        ex.getConstraintViolations().stream()
            .map(
                violation ->
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage()))
            .toList();

    Problem problem =
        Problem.builder()
            .withTitle("Validation Error")
            .withStatus(Status.BAD_REQUEST)
            .withDetail("Input validation failed")
            .with("violations", violations)
            .build();

    return ResponseEntity.badRequest().body(problem);
  }

  record Violation(String fieldName, String message) {}
}
