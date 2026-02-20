package com.fuel.controller;

import com.fuel.model.DailyReading;
import com.fuel.repository.DailyReadingRepository;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/daily-readings")
@CrossOrigin(origins = "*")
public class DailyReadingController {

    private final DailyReadingRepository repository;

    public DailyReadingController(DailyReadingRepository repository) {
        this.repository = repository;
    }

    /**
     * POST /api/daily-readings
     * Save a new daily opening reading.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createReading(@Valid @RequestBody DailyReading reading) {
        DailyReading saved = repository.save(reading);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Daily reading saved successfully");
        response.put("data", saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/daily-readings
     * Retrieve all daily readings.
     * Optional filters: ?date=2024-01-15&shift=MORNING
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllReadings(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String shift) {

        List<DailyReading> readings;

        if (date != null && shift != null) {
            readings = repository.findByReadingDateAndShift(date, shift.toUpperCase());
        } else if (date != null) {
            readings = repository.findByReadingDate(date);
        } else if (shift != null) {
            readings = repository.findByShift(shift.toUpperCase());
        } else {
            readings = repository.findAll();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("count", readings.size());
        response.put("data", readings);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/daily-readings/{id}
     * Retrieve a single daily reading by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getReadingById(@PathVariable Long id) {
        return repository.findById(id)
                .map(reading -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("data", reading);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Daily reading not found with id: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
}
