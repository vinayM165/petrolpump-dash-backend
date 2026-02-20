package com.fuel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_readings")
public class DailyReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Shift Selection ---
    @NotBlank(message = "Shift is required (MORNING, AFTERNOON, NIGHT)")
    @Column(nullable = false)
    private String shift; // MORNING, AFTERNOON, NIGHT

    // --- Tank Levels ---
    @NotNull(message = "Petrol tank level is required")
    @PositiveOrZero(message = "Petrol tank level must be >= 0")
    @Column(name = "petrol_tank_liters", nullable = false)
    private Double petrolTankLiters;

    @NotNull(message = "Diesel tank level is required")
    @PositiveOrZero(message = "Diesel tank level must be >= 0")
    @Column(name = "diesel_tank_liters", nullable = false)
    private Double dieselTankLiters;

    // --- Cash On Hand ---
    @NotNull(message = "Cash on hand is required")
    @PositiveOrZero(message = "Cash must be >= 0")
    @Column(name = "cash_on_hand", nullable = false)
    private BigDecimal cashOnHand;

    // --- Meter Readings ---
    @NotNull(message = "Pump 1 Nozzle 1A reading is required")
    @PositiveOrZero
    @Column(name = "pump1_nozzle1a", nullable = false)
    private Double pump1Nozzle1A;

    @NotNull(message = "Pump 1 Nozzle 1B reading is required")
    @PositiveOrZero
    @Column(name = "pump1_nozzle1b", nullable = false)
    private Double pump1Nozzle1B;

    @NotNull(message = "Pump 2 Nozzle 2A reading is required")
    @PositiveOrZero
    @Column(name = "pump2_nozzle2a", nullable = false)
    private Double pump2Nozzle2A;

    @NotNull(message = "Pump 2 Nozzle 2B reading is required")
    @PositiveOrZero
    @Column(name = "pump2_nozzle2b", nullable = false)
    private Double pump2Nozzle2B;

    // --- Metadata ---
    @Column(name = "reading_date")
    private LocalDate readingDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.readingDate == null) {
            this.readingDate = LocalDate.now();
        }
    }

    // --- Constructors ---
    public DailyReading() {}

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public Double getPetrolTankLiters() { return petrolTankLiters; }
    public void setPetrolTankLiters(Double petrolTankLiters) { this.petrolTankLiters = petrolTankLiters; }

    public Double getDieselTankLiters() { return dieselTankLiters; }
    public void setDieselTankLiters(Double dieselTankLiters) { this.dieselTankLiters = dieselTankLiters; }

    public BigDecimal getCashOnHand() { return cashOnHand; }
    public void setCashOnHand(BigDecimal cashOnHand) { this.cashOnHand = cashOnHand; }

    public Double getPump1Nozzle1A() { return pump1Nozzle1A; }
    public void setPump1Nozzle1A(Double pump1Nozzle1A) { this.pump1Nozzle1A = pump1Nozzle1A; }

    public Double getPump1Nozzle1B() { return pump1Nozzle1B; }
    public void setPump1Nozzle1B(Double pump1Nozzle1B) { this.pump1Nozzle1B = pump1Nozzle1B; }

    public Double getPump2Nozzle2A() { return pump2Nozzle2A; }
    public void setPump2Nozzle2A(Double pump2Nozzle2A) { this.pump2Nozzle2A = pump2Nozzle2A; }

    public Double getPump2Nozzle2B() { return pump2Nozzle2B; }
    public void setPump2Nozzle2B(Double pump2Nozzle2B) { this.pump2Nozzle2B = pump2Nozzle2B; }

    public LocalDate getReadingDate() { return readingDate; }
    public void setReadingDate(LocalDate readingDate) { this.readingDate = readingDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
