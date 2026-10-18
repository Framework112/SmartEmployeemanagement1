package com.shiva.smartemployeemanagement.leave.api.response;

import lombok.Data;

@Data
public class LeaveBalanceResponseDto {
    private Integer employeeId;
    private int allocatedDays;
    private long usedDays;
    private long remainingDays;

    public LeaveBalanceResponseDto() {
    }

    public LeaveBalanceResponseDto(
            Integer employeeId,
            int allocatedDays,
            long usedDays,
            long remainingDays) {
        this.employeeId = employeeId;
        this.allocatedDays = allocatedDays;
        this.usedDays = usedDays;
        this.remainingDays = remainingDays;
    }
}
