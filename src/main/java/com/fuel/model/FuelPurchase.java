package com.fuel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fuel_purchases")
public class FuelPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Fuel type is required")
    @Column(name = "fuel_type", nullable = false)
    private String fuelType;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(name = "quantity_liters", nullable = false)
    private Double quantityLiters;

    @NotNull(message = "Purchase rate is required")
    @Positive(message = "Purchase rate must be positive")
    @Column(name = "purchase_rate", nullable = false)
    private BigDecimal purchaseRate;

    @NotBlank(message = "Invoice number is required")
    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Auto-calculate total amount before persisting
    @PrePersist
    public void prePersist() {
        this.totalAmount = purchaseRate.multiply(BigDecimal.valueOf(quantityLiters));
        this.createdAt = LocalDateTime.now();
    }

    // Constructors
    public FuelPurchase() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public Double getQuantityLiters() { return quantityLiters; }
    public void setQuantityLiters(Double quantityLiters) { this.quantityLiters = quantityLiters; }

    public BigDecimal getPurchaseRate() { return purchaseRate; }
    public void setPurchaseRate(BigDecimal purchaseRate) { this.purchaseRate = purchaseRate; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
