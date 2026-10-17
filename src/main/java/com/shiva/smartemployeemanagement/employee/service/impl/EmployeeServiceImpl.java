package com.shiva.smartemployeemanagement.employee.service.impl;


import com.shiva.smartemployeemanagement.department.entity.Department;
import com.shiva.smartemployeemanagement.department.repository.DepartmentRepository;
import com.shiva.smartemployeemanagement.employee.api.request.EmployeeRequestDto;
import com.shiva.smartemployeemanagement.employee.api.response.EmployeeResponseDto;
import com.shiva.smartemployeemanagement.employee.entity.Employee;
import com.shiva.smartemployeemanagement.employee.exception.EmployeeNotFoundException;
import com.shiva.smartemployeemanagement.employee.mapper.EmployeeMapper;
import com.shiva.smartemployeemanagement.employee.repository.EmployeeRepository;
import com.shiva.smartemployeemanagement.employee.service.EmployeeService;
import com.shiva.smartemployeemanagement.exception.DepartmentNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;
    private final DepartmentRepository departmentRepository;


    public EmployeeServiceImpl(
            EmployeeRepository repository,
            EmployeeMapper mapper, DepartmentRepository departmentRepository) {

        this.repository = repository;
        this.mapper = mapper;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public EmployeeResponseDto saveEmployee(EmployeeRequestDto request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found"+ request.getDepartmentId()));

        Employee employee = new Employee();

        employee.setName(request.getName());
        employee.setSalary(request.getSalary());
        employee.setDepartment(department);

        Employee savedEmployee = repository.save(employee);


        return mapper.toResponse(savedEmployee);
    }

    @Override
    public Page<EmployeeResponseDto> getAllEmployees(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort;

        if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }
//    @Override
//    public List<EmployeeResponseDto> searchEmployeesByName(String name) {
//
//        if (name == null || name.isBlank()) {
//            throw new IllegalArgumentException(
//                    "Search name cannot be empty");
//        }
//
//        return repository
//                .findByNameContainingIgnoreCase(name.trim())
//                .stream()
//                .map(mapper::toResponse)
//                .toList();
//    }

    @Override
    public EmployeeResponseDto getEmployeeById(Integer id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + id
                        )
                );

        return mapper.toResponse(employee);
    }

    @Override
    public EmployeeResponseDto updateEmployee(
            Integer id,
            EmployeeRequestDto request) {

        Employee employee = repository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + id
                        )
                );

        employee.setName(request.getName());
        employee.setSalary(request.getSalary());

        Employee updatedEmployee =
                repository.save(employee);

        return mapper.toResponse(updatedEmployee);
    }

    @Override
    public boolean deleteEmployee(Integer id) {

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);

        return true;
    }
}
