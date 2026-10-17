package com.shiva.smartemployeemanagement.department.service.departmentimpl;

import com.shiva.smartemployeemanagement.department.api.request.DepartmentRequestDto;
import com.shiva.smartemployeemanagement.department.api.response.DepartmentResponseDto;
import com.shiva.smartemployeemanagement.department.entity.Department;
import com.shiva.smartemployeemanagement.department.mapper.DepartmentMapper;
import com.shiva.smartemployeemanagement.department.repository.DepartmentRepository;
import com.shiva.smartemployeemanagement.department.service.DepartmentService;
import com.shiva.smartemployeemanagement.employee.repository.EmployeeRepository;
import com.shiva.smartemployeemanagement.exception.DepartmentNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;
    private final EmployeeRepository employeeRepository;

public DepartmentServiceImpl(DepartmentRepository repository, DepartmentMapper mapper, EmployeeRepository employeeRepository)
{
    this.repository = repository;
    this.mapper = mapper;
    this.employeeRepository = employeeRepository;
}
    @Override
    @Transactional
    public DepartmentResponseDto createDepartment(
            DepartmentRequestDto requestDto) {

        String name = requestDto.getName().trim();

        if (repository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException(
                    "Department already exists with name: " + name);
        }

        Department department = new Department();
        department.setName(name);

        Department savedDepartment = repository.save(department);

        return mapper.toResponse(savedDepartment);
    }
    @Override
    public DepartmentResponseDto getDepartment(Integer id) {
     Department department = repository.findById( id)
             .orElseThrow(()->new DepartmentNotFoundException(
             "Department not found with id "+id));
        return mapper.toResponse(department);
    }

    @Override
    public DepartmentResponseDto updateDepartment(
            Integer id, DepartmentRequestDto request) {
        Department department = repository.findById(id).orElseThrow(
                ()-> new DepartmentNotFoundException("Department not found with id "+ id)
        );
        department.setName(request.getName());
        Department updatedDepartment  = repository.save(department);

    return mapper.toResponse(updatedDepartment);
    }


    @Override
    public DepartmentResponseDto patchDepartment(Integer id, DepartmentRequestDto request) {
        Department  department= repository.findById(id).orElseThrow(() ->
                new DepartmentNotFoundException("Department not found with id "+ id));
        if(request.getName()!=null &&
                !request.getName().isBlank()){
            department.setName(request.getName());
        }
        Department updateDepartments = repository.save(department);
        return mapper.toResponse(updateDepartments);

    }
    @Override
    @Transactional
    public boolean deleteDepartment(Integer id) {

        if (!repository.existsById(id)) {
            return false;
        }

        if (employeeRepository.existsByDepartmentId(id)) {
            throw new IllegalStateException(
                    "Cannot delete department because employees are assigned to it.");
        }

        repository.deleteById(id);
        return true;
    }
}
