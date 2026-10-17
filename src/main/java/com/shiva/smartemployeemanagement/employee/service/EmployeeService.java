package com.shiva.smartemployeemanagement.employee.service;

import com.shiva.smartemployeemanagement.employee.api.request.EmployeeRequestDto;
import com.shiva.smartemployeemanagement.employee.api.response.EmployeeResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;


public interface EmployeeService {

    EmployeeResponseDto saveEmployee(
            EmployeeRequestDto request);

    Page<EmployeeResponseDto> getAllEmployees(
            int page,
            int size,
            String sortBy,
            String direction);

    EmployeeResponseDto getEmployeeById(
            Integer id);

    EmployeeResponseDto updateEmployee(
            Integer id,
            EmployeeRequestDto request);

    boolean deleteEmployee(Integer id);
   // List<EmployeeResponseDto> searchEmployeesByName(String name);
}
