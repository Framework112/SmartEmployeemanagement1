package com.shiva.smartemployeemanagement.employee.entity;

import com.shiva.smartemployeemanagement.department.entity.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Employee {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String name;

        private double salary;

        @ManyToOne
        @JoinColumn(name = "department_id")
        private Department department;
    }

