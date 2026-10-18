
package com.shiva.smartemployeemanagement.leave.service;

import com.shiva.smartemployeemanagement.leave.api.request.LeaveRequestDto;
import com.shiva.smartemployeemanagement.leave.api.response.LeaveBalanceResponseDto;
import com.shiva.smartemployeemanagement.leave.api.response.LeaveResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaveService {

    LeaveResponseDto applyLeave(LeaveRequestDto requestDto);

    LeaveResponseDto getLeaveById(Integer id);
    Page<LeaveResponseDto> getAllLeaves(Pageable pageable);

    Page<LeaveResponseDto> getLeavesByEmployeeId(
            Integer employeeId,
            Pageable pageable
    );

    Page<LeaveResponseDto> getLeavesByStatus(
            String status,
            Pageable pageable
    );
    LeaveResponseDto approveLeave(Integer id);

    LeaveResponseDto rejectLeave(Integer id);
    LeaveResponseDto cancelLeave(Integer id);
    long getUsedLeaveCount(Integer employeeId);
    long getUsedLeaveDays(Integer employeeId);
    LeaveBalanceResponseDto getLeaveBalance(Integer employeeId);
}
