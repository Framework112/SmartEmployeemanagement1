package com.shiva.smartemployeemanagement.department.service;

import com.shiva.smartemployeemanagement.department.api.request.DepartmentRequestDto;
import com.shiva.smartemployeemanagement.department.api.response.DepartmentResponseDto;

public interface DepartmentService {
    DepartmentResponseDto createDepartment(DepartmentRequestDto requestDto);
    DepartmentResponseDto getDepartment(Integer id);
    DepartmentResponseDto updateDepartment(Integer id, DepartmentRequestDto request);

    DepartmentResponseDto patchDepartment(Integer id, DepartmentRequestDto request);

    boolean deleteDepartment(Integer id);
}
