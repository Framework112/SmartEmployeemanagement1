package com.shiva.smartemployeemanagement.attendance.mapper;


import com.shiva.smartemployeemanagement.attendance.api.response.AttendanceResponseDto;
import com.shiva.smartemployeemanagement.attendance.entity.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public AttendanceResponseDto toResponse(Attendance attendance) {

        AttendanceResponseDto response = new AttendanceResponseDto();

        response.setId(attendance.getId());

        if (attendance.getEmployee() != null) {
            response.setEmployeeId(attendance.getEmployee().getId());
            response.setEmployeeName(attendance.getEmployee().getName());
        }

        response.setAttendanceDate(attendance.getAttendanceDate());
        response.setCheckInTime(attendance.getCheckInTime());
        response.setCheckOutTime(attendance.getCheckOutTime());
        response.setStatus(attendance.getStatus());

        return response;
    }
}
