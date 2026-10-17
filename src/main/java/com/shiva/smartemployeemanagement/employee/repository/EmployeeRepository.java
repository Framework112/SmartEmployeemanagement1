package com.shiva.smartemployeemanagement.employee.repository;

import com.shiva.smartemployeemanagement.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByNameContainingIgnoreCase(
            String name);
    boolean existsByDepartmentId(Integer departmentId);

}
