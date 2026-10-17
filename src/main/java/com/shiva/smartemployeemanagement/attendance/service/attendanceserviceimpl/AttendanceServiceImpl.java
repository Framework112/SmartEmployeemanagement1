package com.shiva.smartemployeemanagement.attendance.service.attendanceserviceimpl;

import com.shiva.smartemployeemanagement.attendance.api.request.AttendanceRequestDto;
import com.shiva.smartemployeemanagement.attendance.api.response.AttendanceResponseDto;
import com.shiva.smartemployeemanagement.attendance.entity.Attendance;
import com.shiva.smartemployeemanagement.attendance.mapper.AttendanceMapper;
import com.shiva.smartemployeemanagement.attendance.repository.AttendanceRepository;
import com.shiva.smartemployeemanagement.attendance.service.AttendanceService;
import com.shiva.smartemployeemanagement.employee.entity.Employee;
import com.shiva.smartemployeemanagement.employee.exception.EmployeeNotFoundException;
import com.shiva.smartemployeemanagement.employee.repository.EmployeeRepository;
import com.shiva.smartemployeemanagement.exception.AttendanceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    private AttendanceRepository repository;
    private EmployeeRepository employeeRepository;
    private AttendanceMapper mapper;

    AttendanceServiceImpl(AttendanceRepository repository,
                          EmployeeRepository employeeRepository,
                          AttendanceMapper mapper)
    {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
    }
    @Override
    public AttendanceResponseDto createAttendance(AttendanceRequestDto request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new AttendanceNotFoundException(
                                "Employee not found with id: " + request.getEmployeeId()
                        )
                );

        Attendance attendance = new Attendance();

        attendance.setEmployee(employee);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setCheckInTime(request.getCheckInTime());
        attendance.setCheckOutTime(request.getCheckOutTime());
        attendance.setStatus(request.getStatus());


        Attendance savedAttendance = repository.save(attendance);

        return mapper.toResponse(savedAttendance);
    }

    @Override
    public AttendanceResponseDto updateAttendance(Integer id, AttendanceRequestDto request) {

        Attendance attendance = repository.findById(id)
                .orElseThrow(() ->
                        new AttendanceNotFoundException(
                                "Attendance not found with id: " + id
                        )
                );

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + request.getEmployeeId()
                        )
                );

        attendance.setEmployee(employee);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setCheckInTime(request.getCheckInTime());
        attendance.setCheckOutTime(request.getCheckOutTime());
        attendance.setStatus(request.getStatus());

        Attendance updatedAttendance = repository.save(attendance);

        return mapper.toResponse(updatedAttendance);

    }

    @Override
    public AttendanceResponseDto getAttendance(Integer id) {

        Attendance attendance = repository.findById(id)
                .orElseThrow(() ->
                        new AttendanceNotFoundException(
                                "Attendance not found with id: " + id
                        )
                );

        return mapper.toResponse(attendance);
    }

    @Override
    public AttendanceResponseDto patchAttendance(Integer id, AttendanceRequestDto request) {
        Attendance attendance = repository.findById(id)
                .orElseThrow(() ->
                        new AttendanceNotFoundException(
                                "Attendance not found with id: " + id
                        )
                );

        if (request.getEmployeeId() != null) {

            Employee employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() ->
                            new EmployeeNotFoundException(
                                    "Employee not found with id: "
                                            + request.getEmployeeId()
                            )
                    );

            attendance.setEmployee(employee);
        }

        if (request.getAttendanceDate() != null) {
            attendance.setAttendanceDate(request.getAttendanceDate());
        }

        if (request.getCheckInTime() != null) {
            attendance.setCheckInTime(request.getCheckInTime());
        }

        if (request.getCheckOutTime() != null) {
            attendance.setCheckOutTime(request.getCheckOutTime());
        }

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            attendance.setStatus(request.getStatus());
        }

        Attendance updatedAttendance = repository.save(attendance);

        return mapper.toResponse(updatedAttendance);
    }

    @Override
    public boolean deleteAttendance(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);

        return true;
    }

    @Override
    public AttendanceResponseDto checkout(Integer employeeid) {

        Employee employee = employeeRepository.findById(employeeid).orElseThrow(() ->
                new EmployeeNotFoundException("Employee not found with id: " + employeeid));
//get today's date
        LocalDate today = LocalDate.now();
        Attendance attendance = repository.findByEmployeeIdAndAttendanceDate(employeeid, today)
                .orElseThrow(() -> new AttendanceNotFoundException("No check-in record found for employee id " + employeeid));

        //  Check whether employee already checked out
        if(attendance.getCheckOutTime()!=null){
            throw new IllegalStateException("Employee is already checked out today ");
        }

      //  Verify that employee checked in
if(attendance.getCheckInTime()==null){
    throw new IllegalStateException("Employee must check in before checking out");
}

//set checkout time
  attendance.setCheckOutTime(LocalTime.now());

        //  Save updated attendance
        Attendance updateAttendance = repository.save(attendance);

        return mapper.toResponse(updateAttendance);
    }

    @Override
    public Page<AttendanceResponseDto> getAttendanceHistory(Integer employeeId, Pageable pageable) {
       employeeRepository.findById(employeeId).orElseThrow(()->
                new EmployeeNotFoundException("Employee not found with id : "+employeeId));


        Page<Attendance> attendancePage = repository.findByEmployeeId(employeeId, pageable);
        return attendancePage.map(mapper::toResponse);

    }

//    @Override
//    public Page<AttendanceResponseDto> getAttendanceHistoryByDateRange(Integer employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
//        employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new EmployeeNotFoundException(
//                        "Employee not found with id: " + employeeId));
//        // Validate date range
//        if (startDate == null || endDate == null) {
//            throw new IllegalArgumentException(
//                    "Start date and end date are required");
//        }
//        if (startDate.isAfter(endDate)) {
//            throw new IllegalArgumentException(
//                    "Start date cannot be after end date");
//        }
//        // Fetch attendance within the selected dates
//        Page<Attendance> attendancePage =
//                repository.findByEmployeeIdAndAttendanceDateBetween(
//                        employeeId, startDate, endDate, pageable);
//        // Convert entities to response DTOs
//        return attendancePage.map(mapper::toResponse);
//    }

//    @Override
//    public Page<AttendanceResponseDto> getAttendanceHistoryByStatus(
//            Integer employeeId, String status, Pageable pageable) {
//
//        // 1. Verify that the employee exists
//        employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new EmployeeNotFoundException(
//                        "Employee not found with id: " + employeeId));
//
//        // 2. Validate the status
//        if (status == null || status.isBlank()) {
//            throw new IllegalArgumentException(
//                    "Attendance status is required");
//        }
//
//        // 3. Fetch attendance records by employee ID and status
//        Page<Attendance> attendancePage =
//                repository.findByEmployeeIdAndStatusIgnoreCase(
//                        employeeId, status.trim(), pageable);
//
//        // 4. Convert entities into response DTOs
//        return attendancePage.map(mapper::toResponse);
//    }

    @Override
    public AttendanceResponseDto checkIn(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with id: " + employeeId
                        ));
        LocalDate today = LocalDate.now();

        Optional<Attendance> existingAttendance =
                repository
                        .findByEmployeeIdAndAttendanceDate(
                                employeeId,
                                today
                        );

        if (existingAttendance.isPresent()) {
            throw new IllegalStateException(
                    "Employee has already checked in today"
            );
        }

        Attendance attendance = new Attendance();

        attendance.setEmployee(employee);
        attendance.setAttendanceDate(today);
        attendance.setCheckInTime(LocalTime.now());
        attendance.setCheckOutTime(null);
        attendance.setStatus("PRESENT");

        Attendance saved = repository.save(attendance);

        return mapper.toResponse(saved);
    }
//    @Override
//    public Page<AttendanceResponseDto>
//    getAttendanceHistoryByDateRangeAndStatus(
//            Integer employeeId,
//            LocalDate startDate,
//            LocalDate endDate,
//            String status,
//            Pageable pageable) {
//
//        // 1. Verify that the employee exists
//        employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new EmployeeNotFoundException(
//                        "Employee not found with id: " + employeeId));
//
//        // 2. Validate the date range
//        if (startDate == null || endDate == null) {
//            throw new IllegalArgumentException(
//                    "Start date and end date are required");
//        }
//
//        if (startDate.isAfter(endDate)) {
//            throw new IllegalArgumentException(
//                    "Start date cannot be after end date");
//        }
//
//        // 3. Validate status
//        if (status == null || status.isBlank()) {
//            throw new IllegalArgumentException(
//                    "Attendance status is required");
//        }
//
//        // 4. Fetch matching attendance records
//        Page<Attendance> attendancePage =
//                repository.findByEmployeeIdAndAttendanceDateBetweenAndStatusIgnoreCase(
//                        employeeId,
//                        startDate,
//                        endDate,
//                        status.trim(),
//                        pageable);
//
//        // 5. Convert entities to response DTOs
//        return attendancePage.map(mapper::toResponse);
//    }

    @Override
    public Page<AttendanceResponseDto> getAttendanceHistory(
            Integer employeeId,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            Pageable pageable) {

        // 1. Verify that the employee exists
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        "Employee not found with id: " + employeeId));

        // 2. Validate the date range
        if (startDate != null && endDate != null
                && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date");
        }

        // 3. Normalize an empty status to null
        String normalizedStatus =
                (status == null || status.isBlank())
                        ? null
                        : status.trim();

        // 4. Fetch attendance using the optional filters
        Page<Attendance> attendancePage =
                repository.findAttendanceHistory(
                        employeeId,
                        startDate,
                        endDate,
                        normalizedStatus,
                        pageable);

        // 5. Convert entities to response DTOs
        return attendancePage.map(mapper::toResponse);
    }


}
