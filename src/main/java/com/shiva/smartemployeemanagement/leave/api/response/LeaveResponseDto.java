package com.shiva.smartemployeemanagement.leave.api.response;

import lombok.Data;

import java.time.LocalDate;
@Data
public class LeaveResponseDto {
    private Integer id;
    private Integer employeeId;
    private String employeeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;

    public LeaveResponseDto() {
    }

    public LeaveResponseDto(
            Integer id,
            Integer employeeId,
            String employeeName,
            LocalDate startDate,
            LocalDate endDate,
            String reason,
            String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
    }

}
