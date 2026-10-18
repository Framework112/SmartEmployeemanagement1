package com.shiva.smartemployeemanagement.leave.repository;

import com.shiva.smartemployeemanagement.leave.entity.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface LeaveRepository extends JpaRepository<LeaveRequest, Integer> {
    boolean existsByEmployeeIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Integer employeeId,
            Collection<String> statuses,
            LocalDate endDate,
            LocalDate startDate
    );
    Page<LeaveRequest> findAll(Pageable pageable);
    Page<LeaveRequest> findByEmployeeId(
            Integer employeeId,
            Pageable pageable
    );

    Page<LeaveRequest> findByStatusIgnoreCase(
            String status,
            Pageable pageable
    );
    long countByEmployeeIdAndStatusIn(
            Integer employeeId,
            Collection<String> statuses
    );
    List<LeaveRequest> findByEmployeeIdAndStatusIgnoreCase(
            Integer employeeId,
            String status
    );
}
