package com.shiva.smartemployeemanagement.exception;

import com.shiva.smartemployeemanagement.employee.exception.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

   // @ExceptionHandler(EmployeeNotFoundException.class)
   // @ResponseStatus(HttpStatus.NOT_FOUND)

  // public String  handleExceptionNotFound(EmployeeNotFoundException ex) {
  //      return ex.getMessage();
   // }

    @ExceptionHandler(AttendanceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAttendanceNotFound(
            AttendanceNotFoundException exception) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", 404);
        response.put("error", "Not Found");
        response.put("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeNotFound(
            EmployeeNotFoundException exception) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", 404);
        response.put("error", "Not Found");
        response.put("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(
            IllegalStateException exception) {

        Map<String, Object> response = Map.of(
                "status", 409,
                "error", "Conflict",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException exception) {

        Map<String, Object> response = Map.of(
                "status", 400,
                "error", "Bad Request",
                "message", exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
//            IllegalArgumentException exception) {
//
//        Map<String, Object> response = new LinkedHashMap<>();
//        response.put("status", 400);
//        response.put("error", "Bad Request");
//        response.put("message", exception.getMessage());
//
//        return ResponseEntity.badRequest().body(response);
//    }

//    @ExceptionHandler(IllegalStateException.class)
//    public ResponseEntity<Map<String, Object>> handleIllegalState(
//            IllegalStateException exception) {
//
//        Map<String, Object> response = new LinkedHashMap<>();
//        response.put("status", 409);
//        response.put("error", "Conflict");
//        response.put("message", exception.getMessage());
//
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
//    }
@ExceptionHandler(DepartmentNotFoundException.class)
public ResponseEntity<Map<String, Object>> handleDepartmentNotFound(
        DepartmentNotFoundException exception) {

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("status", 404);
    response.put("error", "Not Found");
    response.put("message", exception.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
}
}
