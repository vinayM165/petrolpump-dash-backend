package com.fuel.controller;

import com.fuel.model.Employee;
import com.fuel.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // POST /api/employees
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody Employee employee) {
        Employee saved = employeeRepository.save(employee);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee created successfully");
        response.put("data", saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/employees?status=ACTIVE&stationId=00111&shift=MORNING
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String stationId,
            @RequestParam(required = false) String shift) {

        List<Employee> employees;
        if (status != null) {
            employees = employeeRepository.findByStatus(status.toUpperCase());
        } else if (stationId != null) {
            employees = employeeRepository.findByStationId(stationId);
        } else if (shift != null) {
            employees = employeeRepository.findByShift(shift.toUpperCase());
        } else {
            employees = employeeRepository.findAll();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", employees);
        response.put("count", employees.size());
        return ResponseEntity.ok(response);
    }

    // GET /api/employees/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id));
        Map<String, Object> response = new HashMap<>();
        response.put("data", employee);
        return ResponseEntity.ok(response);
    }

    // PUT /api/employees/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody Employee updated) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id));

        existing.setName(updated.getName());
        existing.setNumber(updated.getNumber());
        existing.setStatus(updated.getStatus());
        existing.setStationId(updated.getStationId());
        existing.setStation(updated.getStation());
        existing.setShift(updated.getShift());

        Employee saved = employeeRepository.save(existing);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee updated successfully");
        response.put("data", saved);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/employees/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee deleted successfully");
        return ResponseEntity.ok(response);
    }
}
