package com.shiva.smartemployeemanagement.department.mapper;

import com.shiva.smartemployeemanagement.department.api.response.DepartmentResponseDto;
import com.shiva.smartemployeemanagement.department.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public DepartmentResponseDto toResponse(Department department) {

        DepartmentResponseDto response =
                new DepartmentResponseDto();

        response.setId(department.getId());
        response.setName(department.getName());

        return response;
    }
}
