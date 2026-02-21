package com.fuel.dto;

import jakarta.validation.constraints.NotBlank;

public class EmployeeRegistrationRequest {

    // --- Employee Profile ---
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    private String number;

    private String email;

    private String status = "ACTIVE";

    @NotBlank(message = "Station ID is required")
    private String stationId;

    @NotBlank(message = "Station name is required")
    private String station;

    @NotBlank(message = "Shift is required")
    private String shift;

    // --- Login Credentials ---
    @NotBlank(message = "Login ID is required")
    private String loginId;

    @NotBlank(message = "Password is required")
    private String password;

    // --- Getters & Setters ---
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

    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
