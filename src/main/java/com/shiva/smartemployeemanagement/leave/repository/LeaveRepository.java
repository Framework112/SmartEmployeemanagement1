package com.shiva.smartemployeemanagement.leave.repository;

import com.shiva.smartemployeemanagement.leave.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepository extends JpaRepository<LeaveRequest, Integer> {
}
