package com.shiva.smartemployeemanagement.attendance.entity;

import com.shiva.smartemployeemanagement.employee.entity.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
        @NotNull(message = "Employee ID is required")
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne
        @JoinColumn(name = "employee_id", nullable = false)
        private Employee employee;
        @NotNull(message = "Attendance date is required")
        private LocalDate attendanceDate;

        private LocalTime checkInTime;

        private LocalTime checkOutTime;

        @NotBlank(message = "Attendance status is required")
        private String status;

        public void setCheckIn(LocalDateTime now) {
        }
}
