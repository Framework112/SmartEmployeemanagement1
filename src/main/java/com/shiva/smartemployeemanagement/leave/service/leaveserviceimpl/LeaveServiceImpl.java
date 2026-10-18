package com.shiva.smartemployeemanagement.leave.service.leaveserviceimpl;

import com.shiva.smartemployeemanagement.employee.entity.Employee;
import com.shiva.smartemployeemanagement.employee.exception.EmployeeNotFoundException;
import com.shiva.smartemployeemanagement.employee.repository.EmployeeRepository;
import com.shiva.smartemployeemanagement.leave.api.request.LeaveRequestDto;
import com.shiva.smartemployeemanagement.leave.api.response.LeaveBalanceResponseDto;
import com.shiva.smartemployeemanagement.leave.api.response.LeaveResponseDto;
import com.shiva.smartemployeemanagement.leave.entity.LeaveRequest;
import com.shiva.smartemployeemanagement.leave.repository.LeaveRepository;
import com.shiva.smartemployeemanagement.leave.service.LeaveService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {
    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveServiceImpl(LeaveRepository leaveRepository,
                            EmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;

    }


    @Override
    @Transactional
    public LeaveResponseDto applyLeave(LeaveRequestDto requestDto) {
        if (requestDto.getStartDate().isAfter(requestDto.getEndDate())) {
            throw new IllegalArgumentException("Leave start date cannot be after end date");
        }
        if (requestDto.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Leave start date cannot be in the past");
        }

        Employee employee = employeeRepository.findById(requestDto.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(
                        "Employee now found with id" + requestDto.getEmployeeId()));

        boolean overlappingLeave =
                leaveRepository
                        .existsByEmployeeIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                                requestDto.getEmployeeId(),
                                List.of("PENDING", "APPROVED"),
                                requestDto.getEndDate(),
                                requestDto.getStartDate()
                        );

        if (overlappingLeave) {
            throw new IllegalArgumentException(
                    "You already have a pending or approved leave request for these dates."
            );
        }
        long requestedDays = java.time.temporal.ChronoUnit.DAYS.between(
                requestDto.getStartDate(),
                requestDto.getEndDate()) + 1;

        LeaveBalanceResponseDto balance =
                getLeaveBalance(requestDto.getEmployeeId());

        if (requestedDays > balance.getRemainingDays()) {
            throw new IllegalArgumentException(
                    "Insufficient leave balance. Remaining days: "
                            + balance.getRemainingDays()
                            + ", requested days: " + requestedDays);
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setStartDate(requestDto.getStartDate());
        leaveRequest.setEndDate(requestDto.getEndDate());
        leaveRequest.setReason(requestDto.getReason().trim());
        leaveRequest.setStatus("PENDING");
        LeaveRequest savedLeave = leaveRepository.save(leaveRequest);
        return toResponse(savedLeave);
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveResponseDto getLeaveById(Integer id) {

        LeaveRequest leave = leaveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Leave request not found with id: " + id));

        return toResponse(leave);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveResponseDto> getAllLeaves(Pageable pageable) {

        return leaveRepository.findAll(pageable)
                .map(this::toResponse);
    }

    private LeaveResponseDto toResponse(LeaveRequest leave) {

        return new LeaveResponseDto(
                leave.getId(),
                leave.getEmployee().getId(),
                leave.getEmployee().getName(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getReason(),
                leave.getStatus()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public Page<LeaveResponseDto> getLeavesByEmployeeId(
            Integer employeeId,
            Pageable pageable) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException(
                    "Employee not found with id: " + employeeId);
        }

        return leaveRepository.findByEmployeeId(employeeId, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveResponseDto> getLeavesByStatus(
            String status,
            Pageable pageable) {

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException(
                    "Leave status must not be blank");
        }

        return leaveRepository.findByStatusIgnoreCase(
                        status.trim(), pageable)
                .map(this::toResponse);
    }
    @Override
    @Transactional
    public LeaveResponseDto approveLeave(Integer id) {

        LeaveRequest leave = leaveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Leave request not found with id: " + id));

        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            throw new IllegalStateException(
                    "Only pending leave requests can be approved.");
        }

        leave.setStatus("APPROVED");

        LeaveRequest updatedLeave = leaveRepository.save(leave);

        return toResponse(updatedLeave);
    }

    @Override
    @Transactional
    public LeaveResponseDto rejectLeave(Integer id) {

        LeaveRequest leave = leaveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Leave request not found with id: " + id));

        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            throw new IllegalStateException(
                    "Only pending leave requests can be rejected.");
        }

        leave.setStatus("REJECTED");

        LeaveRequest updatedLeave = leaveRepository.save(leave);

        return toResponse(updatedLeave);
    }

    @Override
    @Transactional
    public LeaveResponseDto cancelLeave(Integer id) {

        LeaveRequest leave = leaveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Leave request not found with id: " + id));

        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            throw new IllegalStateException(
                    "Only pending leave requests can be cancelled.");
        }

        leave.setStatus("CANCELLED");

        LeaveRequest updatedLeave = leaveRepository.save(leave);

        return toResponse(updatedLeave);
    }
    @Override
    @Transactional(readOnly = true)
    public long getUsedLeaveCount(Integer employeeId) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException(
                    "Employee not found with id: " + employeeId);
        }

        return leaveRepository.countByEmployeeIdAndStatusIn(
                employeeId,
                java.util.List.of("APPROVED")
        );
    }

    @Override
    @Transactional(readOnly = true)
    public long getUsedLeaveDays(Integer employeeId) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException(
                    "Employee not found with id: " + employeeId);
        }

        List<LeaveRequest> approvedLeaves =
                leaveRepository.findByEmployeeIdAndStatusIgnoreCase(
                        employeeId, "APPROVED");

        return approvedLeaves.stream()
                .mapToLong(leave ->
                        java.time.temporal.ChronoUnit.DAYS.between(
                                leave.getStartDate(),
                                leave.getEndDate()) + 1)
                .sum();
    }
    @Override
    @Transactional(readOnly = true)
    public LeaveBalanceResponseDto getLeaveBalance(Integer employeeId) {

        if (!employeeRepository.existsById(employeeId)) {
            throw new EmployeeNotFoundException(
                    "Employee not found with id: " + employeeId);
        }

        int allocatedDays = 12;

        long usedDays = getUsedLeaveDays(employeeId);

        long remainingDays = Math.max(0, allocatedDays - usedDays);

        return new LeaveBalanceResponseDto(
                employeeId,
                allocatedDays,
                usedDays,
                remainingDays
        );
    }

}
