package com.shiva.smartemployeemanagement.employee.controller;

import com.shiva.smartemployeemanagement.employee.api.request.EmployeeRequestDto;
import com.shiva.smartemployeemanagement.employee.api.response.EmployeeResponseDto;
import com.shiva.smartemployeemanagement.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

        private final EmployeeService service;

        public EmployeeController(EmployeeService service) {
            this.service = service;
        }

        @PostMapping
        public ResponseEntity<EmployeeResponseDto> saveEmployee(
                @Valid @RequestBody EmployeeRequestDto request) {

            EmployeeResponseDto response =
                    service.saveEmployee(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        }

        @GetMapping
        public Page<EmployeeResponseDto> getAllEmployees(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "id") String sortBy,
                @RequestParam(defaultValue = "asc") String direction) {

            return service.getAllEmployees(
                    page,
                    size,
                    sortBy,
                    direction);
        }

        @GetMapping("/{id}")
        public EmployeeResponseDto getEmployeeById(
                @PathVariable Integer id) {

            return service.getEmployeeById(id);
        }

        @PutMapping("/{id}")
        public EmployeeResponseDto updateEmployee(
                @PathVariable Integer id,
                @Valid @RequestBody EmployeeRequestDto request) {

            return service.updateEmployee(id, request);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteEmployee(
                @PathVariable Integer id) {

            boolean deleted =
                    service.deleteEmployee(id);

            if (deleted) {
                return ResponseEntity.ok(
                        "Employee has been deleted");
            }

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Employee not found with id: "
                                    + id);
        }
//    @GetMapping("/search")
//    public ResponseEntity<List<EmployeeResponseDto>> searchEmployees(
//            @RequestParam String name) {
//
//        return ResponseEntity.ok(
//                service.searchEmployeesByName(name));
//    }
}
