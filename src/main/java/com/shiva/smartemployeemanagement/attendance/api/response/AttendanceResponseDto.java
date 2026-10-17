package com.shiva.smartemployeemanagement.attendance.api.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponseDto {

        private Integer id;

        private Integer employeeId;

        private String employeeName;

        private LocalDate attendanceDate;

        private LocalTime checkInTime;

        private LocalTime checkOutTime;

        private String status;
    }

