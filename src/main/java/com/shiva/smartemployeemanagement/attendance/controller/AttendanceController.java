package com.shiva.smartemployeemanagement.attendance.controller;

import com.shiva.smartemployeemanagement.attendance.api.request.AttendanceRequestDto;
import com.shiva.smartemployeemanagement.attendance.api.response.AttendanceResponseDto;
import com.shiva.smartemployeemanagement.attendance.service.AttendanceService;
import com.shiva.smartemployeemanagement.common.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<AttendanceResponseDto> createAttendance(
            @Valid @RequestBody AttendanceRequestDto request) {

        AttendanceResponseDto response =
                attendanceService.createAttendance(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PostMapping("/check-in/{employeeId}")
    public ResponseEntity<AttendanceResponseDto> checkIn(
            @PathVariable Integer employeeId) {

        AttendanceResponseDto response =
                attendanceService.checkIn(employeeId);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/check-out/{employeeId}")
    public ResponseEntity<AttendanceResponseDto> checkout(@PathVariable Integer employeeId){
      AttendanceResponseDto response = attendanceService.checkout(employeeId);
      return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{employeeId}/history")
    public ResponseEntity<PagedResponse<AttendanceResponseDto>> getAttendanceHistory(
            @PathVariable Integer employeeId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam(required = false) String status,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "attendanceDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        // Validate pagination
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        // Validate date range
        if (startDate != null && endDate != null
                && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date");
        }

        // Validate sorting field
        if (!"attendanceDate".equals(sortBy)
                && !"checkInTime".equals(sortBy)
                && !"checkOutTime".equals(sortBy)
                && !"status".equals(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy);
        }

        // Validate sort direction
        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Direction must be ASC or DESC");
        }

        Pageable pageable = PageRequest.of(
                page, size, Sort.by(sortDirection, sortBy));

        Page<AttendanceResponseDto> history =
                attendanceService.getAttendanceHistory(
                        employeeId, startDate, endDate, status, pageable);

        PagedResponse<AttendanceResponseDto> response =
                new PagedResponse<>(
                        history.getContent(),
                        history.getNumber(),
                        history.getSize(),
                        history.getTotalElements(),
                        history.getTotalPages(),
                        history.isFirst(),
                        history.isLast()
                );

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/employee/{employeeId}/history/date-range")
//    public ResponseEntity<Page<AttendanceResponseDto>>
//    getAttendanceHistoryByDateRange(
//            @PathVariable Integer employeeId,
//            @RequestParam
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//            LocalDate startDate,
//            @RequestParam
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//            LocalDate endDate,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "attendanceDate") String sortBy,
//            @RequestParam(defaultValue = "DESC") String direction) {
//
//        if (page < 0) {
//            throw new IllegalArgumentException(
//                    "Page number cannot be negative");
//        }
//
//        if (size < 1 || size > 100) {
//            throw new IllegalArgumentException(
//                    "Page size must be between 1 and 100");
//        }
//
//        if (startDate.isAfter(endDate)) {
//            throw new IllegalArgumentException(
//                    "Start date cannot be after end date");
//        }
//
//        if (!"attendanceDate".equals(sortBy)
//                && !"checkInTime".equals(sortBy)
//                && !"checkOutTime".equals(sortBy)
//                && !"status".equals(sortBy)) {
//            throw new IllegalArgumentException(
//                    "Invalid sort field: " + sortBy);
//        }
//
//        Sort.Direction sortDirection =
//                Sort.Direction.fromString(direction);
//
//        Pageable pageable = PageRequest.of(
//                page, size, Sort.by(sortDirection, sortBy));
//
//        Page<AttendanceResponseDto> history =
//                attendanceService.getAttendanceHistoryByDateRange(
//                        employeeId, startDate, endDate, pageable);
//
//        return ResponseEntity.ok(history);
//    }
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceResponseDto> getAttendanceById(
            @PathVariable Integer id) {

        AttendanceResponseDto response =
                attendanceService.getAttendance(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceResponseDto> updateAttendance(
            @PathVariable Integer id,
            @Valid @RequestBody AttendanceRequestDto request) {

        AttendanceResponseDto response =
                attendanceService.updateAttendance(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AttendanceResponseDto> patchAttendance(
            @PathVariable Integer id,
            @RequestBody AttendanceRequestDto request) {

        AttendanceResponseDto response =
                attendanceService.patchAttendance(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttendance(
            @PathVariable Integer id) {

        boolean deleted = attendanceService.deleteAttendance(id);

        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Attendance not found with id: " + id);
        }

        return ResponseEntity.ok(
                "Attendance has been deleted successfully"
        );
    }

//    @GetMapping("/employee/{employeeId}/history/status")
//    public ResponseEntity<Page<AttendanceResponseDto>>
//    getAttendanceHistoryByStatus(
//            @PathVariable Integer employeeId,
//            @RequestParam String status,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "attendanceDate") String sortBy,
//            @RequestParam(defaultValue = "DESC") String direction) {
//
//        if (page < 0) {
//            throw new IllegalArgumentException(
//                    "Page number cannot be negative");
//        }
//
//        if (size < 1 || size > 100) {
//            throw new IllegalArgumentException(
//                    "Page size must be between 1 and 100");
//        }
//
//        if (status.isBlank()) {
//            throw new IllegalArgumentException(
//                    "Attendance status is required");
//        }
//
//        if (!"attendanceDate".equals(sortBy)
//                && !"checkInTime".equals(sortBy)
//                && !"checkOutTime".equals(sortBy)
//                && !"status".equals(sortBy)) {
//            throw new IllegalArgumentException(
//                    "Invalid sort field: " + sortBy);
//        }
//
//        Sort.Direction sortDirection =
//                Sort.Direction.fromString(direction);
//
//        Pageable pageable = PageRequest.of(
//                page,
//                size,
//                Sort.by(sortDirection, sortBy));
//
//        Page<AttendanceResponseDto> history =
//                attendanceService.getAttendanceHistoryByStatus(
//                        employeeId, status.trim(), pageable);
//
//        return ResponseEntity.ok(history);
//    }
//    @GetMapping("/employee/{employeeId}/history/filter")
//    public ResponseEntity<Page<AttendanceResponseDto>>
//    getAttendanceHistoryByDateRangeAndStatus(
//            @PathVariable Integer employeeId,
//
//            @RequestParam
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//            LocalDate startDate,
//
//            @RequestParam
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//            LocalDate endDate,
//
//            @RequestParam String status,
//
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "attendanceDate") String sortBy,
//            @RequestParam(defaultValue = "DESC") String direction) {
//
//        if (page < 0) {
//            throw new IllegalArgumentException(
//                    "Page number cannot be negative");
//        }
//
//        if (size < 1 || size > 100) {
//            throw new IllegalArgumentException(
//                    "Page size must be between 1 and 100");
//        }
//
//        if (startDate.isAfter(endDate)) {
//            throw new IllegalArgumentException(
//                    "Start date cannot be after end date");
//        }
//
//        if (status.isBlank()) {
//            throw new IllegalArgumentException(
//                    "Attendance status is required");
//        }
//
//        if (!"attendanceDate".equals(sortBy)
//                && !"checkInTime".equals(sortBy)
//                && !"checkOutTime".equals(sortBy)
//                && !"status".equals(sortBy)) {
//            throw new IllegalArgumentException(
//                    "Invalid sort field: " + sortBy);
//        }
//
//        Sort.Direction sortDirection =
//                Sort.Direction.fromString(direction);
//
//        Pageable pageable = PageRequest.of(
//                page,
//                size,
//                Sort.by(sortDirection, sortBy));
//
//        Page<AttendanceResponseDto> history =
//                attendanceService.getAttendanceHistoryByDateRangeAndStatus(
//                        employeeId,
//                        startDate,
//                        endDate,
//                        status.trim(),
//                        pageable);
//
//        return ResponseEntity.ok(history);
//    }

}
