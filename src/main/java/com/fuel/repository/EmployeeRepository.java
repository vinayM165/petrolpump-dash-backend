package com.fuel.repository;

import com.fuel.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByStatus(String status);
    List<Employee> findByStationId(String stationId);
    List<Employee> findByShift(String shift);
}
