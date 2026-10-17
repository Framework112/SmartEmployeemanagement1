package com.shiva.smartemployeemanagement.attendance.repository;

import com.shiva.smartemployeemanagement.attendance.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    Optional<Attendance> findByEmployeeIdAndAttendanceDate(
            Integer employeeId,
            LocalDate attendanceDate
    );

    Page<Attendance> findByEmployeeId(Integer employeeId, Pageable pageable);

    Page<Attendance> findByEmployeeIdAndAttendanceDateBetween(
            Integer employeeId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable);
    Page<Attendance> findByEmployeeIdAndStatusIgnoreCase(Integer employeeId, String status, Pageable pageable);
    Page<Attendance> findByEmployeeIdAndAttendanceDateBetweenAndStatusIgnoreCase(
            Integer employeeId,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            Pageable pageable);
    @Query("""
    SELECT a
    FROM Attendance a
    WHERE a.employee.id = :employeeId
      AND (:startDate IS NULL OR a.attendanceDate >= :startDate)
      AND (:endDate IS NULL OR a.attendanceDate <= :endDate)
      AND (:status IS NULL OR LOWER(a.status) = LOWER(:status))
    """)
    Page<Attendance> findAttendanceHistory(
            @Param("employeeId") Integer employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") String status,
            Pageable pageable);

}
