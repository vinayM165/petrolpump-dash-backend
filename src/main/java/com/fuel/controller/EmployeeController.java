package com.fuel.controller;

import com.fuel.dto.EmployeeRegistrationRequest;
import com.fuel.model.Employee;
import com.fuel.model.User;
import com.fuel.repository.EmployeeRepository;
import com.fuel.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(EmployeeRepository employeeRepository,
                              UserRepository userRepository,
                              PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // POST /api/employees  — admin creates employee + login account in one call
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody EmployeeRegistrationRequest req) {

        // Check for duplicate loginId
        if (userRepository.existsByUsername(req.getLoginId())) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Login ID '" + req.getLoginId() + "' is already taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        // 1. Create the User (login account)
        User user = new User();
        user.setUsername(req.getLoginId());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEmail(req.getEmail());
        user.setRole("EMPLOYEE");

        // 2. Create the Employee profile linked to User
        Employee employee = new Employee();
        employee.setName(req.getName());
        employee.setNumber(req.getNumber());
        employee.setEmail(req.getEmail());
        employee.setStatus(req.getStatus() != null ? req.getStatus() : "ACTIVE");
        employee.setStationId(req.getStationId());
        employee.setStation(req.getStation());
        employee.setShift(req.getShift());
        employee.setUser(user); // CascadeType.ALL saves user automatically

        Employee saved = employeeRepository.save(employee);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee registered successfully");
        response.put("data", buildResponse(saved));
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

        List<Map<String, Object>> list = employees.stream().map(this::buildResponse).toList();
        Map<String, Object> response = new HashMap<>();
        response.put("data", list);
        response.put("count", list.size());
        return ResponseEntity.ok(response);
    }

    // GET /api/employees/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        Employee employee = findOrThrow(id);
        Map<String, Object> response = new HashMap<>();
        response.put("data", buildResponse(employee));
        return ResponseEntity.ok(response);
    }

    // PUT /api/employees/{id}  — update profile fields only (not login credentials)
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
                                                      @RequestBody EmployeeRegistrationRequest req) {
        Employee existing = findOrThrow(id);

        existing.setName(req.getName());
        existing.setNumber(req.getNumber());
        existing.setEmail(req.getEmail());
        if (req.getStatus() != null) existing.setStatus(req.getStatus());
        existing.setStationId(req.getStationId());
        existing.setStation(req.getStation());
        existing.setShift(req.getShift());

        // Update password if provided
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            existing.getUser().setPassword(passwordEncoder.encode(req.getPassword()));
        }

        Employee saved = employeeRepository.save(existing);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee updated successfully");
        response.put("data", buildResponse(saved));
        return ResponseEntity.ok(response);
    }

    // DELETE /api/employees/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);  // CascadeType.ALL also deletes the linked User
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee deleted successfully");
        return ResponseEntity.ok(response);
    }

    // --- Helper ---
    private Employee findOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id));
    }

    private Map<String, Object> buildResponse(Employee e) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", e.getId());
        m.put("name", e.getName());
        m.put("number", e.getNumber());
        m.put("email", e.getEmail());
        m.put("status", e.getStatus());
        m.put("stationId", e.getStationId());
        m.put("station", e.getStation());
        m.put("shift", e.getShift());
        m.put("loginId", e.getLoginId());
        m.put("createdAt", e.getCreatedAt());
        return m;
    }
}
