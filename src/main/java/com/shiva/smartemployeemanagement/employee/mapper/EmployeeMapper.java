package com.shiva.smartemployeemanagement.employee.mapper;

import com.shiva.smartemployeemanagement.employee.api.response.EmployeeResponseDto;
import com.shiva.smartemployeemanagement.employee.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeResponseDto toResponse(Employee employee) {

        EmployeeResponseDto response = new EmployeeResponseDto();

        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setSalary(employee.getSalary());

        if (employee.getDepartment() != null) {

            response.setDepartment_id(
                    employee.getDepartment().getId()
            );

            response.setDepartment_name(
                    employee.getDepartment().getName()
            );
        }

        return response;
    }
}
