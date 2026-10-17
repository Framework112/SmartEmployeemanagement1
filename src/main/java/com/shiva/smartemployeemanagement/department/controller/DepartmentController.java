package com.shiva.smartemployeemanagement.department.controller;

import com.shiva.smartemployeemanagement.department.api.request.DepartmentRequestDto;
import com.shiva.smartemployeemanagement.department.api.response.DepartmentResponseDto;
import com.shiva.smartemployeemanagement.department.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentservice;

    public DepartmentController(DepartmentService departmentservice) {
        this.departmentservice = departmentservice;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(
            @Valid @RequestBody DepartmentRequestDto request) {
        DepartmentResponseDto response = departmentservice.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/{id}")
    public  ResponseEntity<?> getDepartment(@PathVariable Integer id) {

        DepartmentResponseDto response = departmentservice.getDepartment(id);

        if (response == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Department not found with the id " + id);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(@PathVariable Integer id,
                                                                  @Valid @RequestBody DepartmentRequestDto request) {
DepartmentResponseDto response = departmentservice.updateDepartment(id, request);

        return ResponseEntity.ok(response);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> patchDepartment(
            @PathVariable Integer id,
            @RequestBody DepartmentRequestDto request) {

        DepartmentResponseDto response =
                departmentservice.patchDepartment(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String > deleteDepartment(@PathVariable  Integer id)
    {
        boolean deleted = departmentservice.deleteDepartment(id);
        if(!deleted)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Department not found wit Id "+id);

        }

        return ResponseEntity.ok("Department has been deleted");
    }
}