package com.fuel.controller;

import com.fuel.model.FuelPurchase;
import com.fuel.repository.FuelPurchaseRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fuel-purchases")
@CrossOrigin(origins = "*")
public class FuelPurchaseController {

    private final FuelPurchaseRepository repository;

    public FuelPurchaseController(FuelPurchaseRepository repository) {
        this.repository = repository;
    }

    /**
     * POST /api/fuel-purchases
     * Create a new fuel purchase record.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createFuelPurchase(@Valid @RequestBody FuelPurchase fuelPurchase) {
        FuelPurchase saved = repository.save(fuelPurchase);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Fuel purchase recorded successfully");
        response.put("data", saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/fuel-purchases
     * Retrieve all fuel purchase records.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllFuelPurchases() {
        List<FuelPurchase> purchases = repository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("count", purchases.size());
        response.put("data", purchases);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/fuel-purchases/{id}
     * Retrieve a single fuel purchase record by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getFuelPurchaseById(@PathVariable Long id) {
        return repository.findById(id)
                .map(purchase -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("data", purchase);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Fuel purchase not found with id: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
}
