package com.shiva.smartemployeemanagement.employee.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class EmployeeRequestDto {
        @NotBlank(message = "Employee name is required")
        @Size(min = 2, max = 100,
                message = "Employee name must be between 2 and 100 characters")
        private String name;

        @NotNull
        @Positive(message = "Salary is greater than 0")
        private Double salary;

        private  Integer departmentId;
    }

