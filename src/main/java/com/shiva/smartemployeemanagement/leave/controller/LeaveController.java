package com.shiva.smartemployeemanagement.leave.controller;

import com.shiva.smartemployeemanagement.leave.api.request.LeaveRequestDto;
import com.shiva.smartemployeemanagement.leave.api.response.LeaveBalanceResponseDto;
import com.shiva.smartemployeemanagement.leave.api.response.LeaveResponseDto;
import com.shiva.smartemployeemanagement.leave.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leaves")
public class LeaveController {
    private LeaveService leaveService;
    public LeaveController(LeaveService leaveService){
        this.leaveService = leaveService;
    }
    @PostMapping
    public ResponseEntity<LeaveResponseDto> applyLeave(
            @Valid @RequestBody LeaveRequestDto requestDto){
        LeaveResponseDto response = leaveService.applyLeave(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveResponseDto> getLeaveById(
            @PathVariable Integer id) {

        LeaveResponseDto response =
                leaveService.getLeaveById(id);

        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<Page<LeaveResponseDto>> getAllLeaves(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort sort = direction.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(leaveService.getAllLeaves(pageable));
    }
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<LeaveResponseDto>> getLeavesByEmployeeId(
            @PathVariable Integer employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                leaveService.getLeavesByEmployeeId(employeeId, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<LeaveResponseDto>> getLeavesByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                leaveService.getLeavesByStatus(status, pageable));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<LeaveResponseDto> approveLeave(
            @PathVariable Integer id) {

        LeaveResponseDto response = leaveService.approveLeave(id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<LeaveResponseDto> rejectLeave(
            @PathVariable Integer id) {

        LeaveResponseDto response = leaveService.rejectLeave(id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<LeaveResponseDto> cancelLeave(
            @PathVariable Integer id) {

        LeaveResponseDto response = leaveService.cancelLeave(id);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/employee/{employeeId}/used-count")
    public ResponseEntity<Long> getUsedLeaveCount(
            @PathVariable Integer employeeId) {

        return ResponseEntity.ok(
                leaveService.getUsedLeaveCount(employeeId));
    }
    @GetMapping("/employee/{employeeId}/used-days")
    public ResponseEntity<Long> getUsedLeaveDays(
            @PathVariable Integer employeeId) {

        return ResponseEntity.ok(
                leaveService.getUsedLeaveDays(employeeId));
    }
    @GetMapping("/employee/{employeeId}/balance")
    public ResponseEntity<LeaveBalanceResponseDto> getLeaveBalance(
            @PathVariable Integer employeeId) {

        return ResponseEntity.ok(
                leaveService.getLeaveBalance(employeeId));
    }
}
