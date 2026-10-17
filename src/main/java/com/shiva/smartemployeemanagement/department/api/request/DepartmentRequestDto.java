package com.shiva.smartemployeemanagement.department.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequestDto {
    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100,
            message = "Department name must be between 2 and 100 characters")
    private String name;
}
