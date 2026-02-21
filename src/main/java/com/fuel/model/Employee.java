package com.fuel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false)
    private String number;

    @Column
    private String email;

    // e.g. ACTIVE, INACTIVE
    @Column(nullable = false)
    private String status = "ACTIVE";

    @NotBlank(message = "Station ID is required")
    @Column(name = "station_id", nullable = false)
    private String stationId;

    @NotBlank(message = "Station name is required")
    @Column(nullable = false)
    private String station;

    // e.g. MORNING, AFTERNOON, NIGHT
    @NotBlank(message = "Shift is required")
    @Column(nullable = false)
    private String shift;

    // One-to-One link to the User login account
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore   // prevent circular serialization
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null || this.status.isBlank()) {
            this.status = "ACTIVE";
        }
    }

    // --- Constructors ---
    public Employee() {}

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStationId() { return stationId; }
    public void setStationId(String stationId) { this.stationId = stationId; }

    public String getStation() { return station; }
    public void setStation(String station) { this.station = station; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Convenience getter — expose loginId in JSON without circular ref
    public String getLoginId() { return user != null ? user.getUsername() : null; }
}
