package com.edutech.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;

    @NotBlank
    private String driverName;

    @NotBlank
    @Column(unique = true)
    private String licenseNumber;

    @NotBlank
    @Pattern(regexp = "\\d{10}")
    private String phoneNumber;

    @Min(0)
    private int experienceYears;

    @NotBlank
    private String address;

    @NotBlank
    private String availabilityStatus;

    // NEW — for document expiry alerts
    private LocalDate licenseExpiryDate;

    // NEW — link driver to their user account
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Driver() {
    }

    public Driver(Long driverId, String driverName, String licenseNumber,
                  String phoneNumber, int experienceYears, String address,
                  String availabilityStatus, LocalDate licenseExpiryDate, User user) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.licenseNumber = licenseNumber;
        this.phoneNumber = phoneNumber;
        this.experienceYears = experienceYears;
        this.address = address;
        this.availabilityStatus = availabilityStatus;
        this.licenseExpiryDate = licenseExpiryDate;
        this.user = user;
    }

    public Driver(String driverName, String licenseNumber, String phoneNumber,
                  int experienceYears, String address, String availabilityStatus) {
        this.driverName = driverName;
        this.licenseNumber = licenseNumber;
        this.phoneNumber = phoneNumber;
        this.experienceYears = experienceYears;
        this.address = address;
        this.availabilityStatus = availabilityStatus;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public LocalDate getLicenseExpiryDate() {
        return licenseExpiryDate;
    }

    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) {
        this.licenseExpiryDate = licenseExpiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Driver [driverId=" + driverId + ", driverName=" + driverName
                + ", licenseNumber=" + licenseNumber + ", phoneNumber=" + phoneNumber
                + ", experienceYears=" + experienceYears + ", address=" + address
                + ", availabilityStatus=" + availabilityStatus
                + ", licenseExpiryDate=" + licenseExpiryDate + "]";
    }
}