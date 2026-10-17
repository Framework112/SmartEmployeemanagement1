package com.shiva.smartemployeemanagement.attendance.service;

import com.shiva.smartemployeemanagement.attendance.api.request.AttendanceRequestDto;
import com.shiva.smartemployeemanagement.attendance.api.response.AttendanceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    AttendanceResponseDto createAttendance(AttendanceRequestDto request);
    AttendanceResponseDto updateAttendance(Integer id,AttendanceRequestDto request);
    AttendanceResponseDto getAttendance(Integer id);
    AttendanceResponseDto patchAttendance(Integer id, AttendanceRequestDto request);
    boolean deleteAttendance(Integer id);

    AttendanceResponseDto checkIn(Integer employeeId);
    AttendanceResponseDto checkout(Integer employeeId);
    Page<AttendanceResponseDto> getAttendanceHistory(Integer employeeId, Pageable pageable);
//    Page<AttendanceResponseDto> getAttendanceHistoryByDateRange(
//            Integer employeeId,
//            LocalDate startDate,
//            LocalDate endDate,
//            Pageable pageable);

//    Page<AttendanceResponseDto> getAttendanceHistoryByStatus(
//            Integer employeeId,
//            String status,
//            Pageable pageable);
//    Page<AttendanceResponseDto> getAttendanceHistoryByDateRangeAndStatus(
//            Integer employeeId,
//            LocalDate startDate,
//            LocalDate endDate,
//            String status,
//            Pageable pageable);
    Page<AttendanceResponseDto> getAttendanceHistory(
            Integer employeeId,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            Pageable pageable);


}
