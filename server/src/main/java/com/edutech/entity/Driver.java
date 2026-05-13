package com.edutech.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class Driver {

     // Add the required code here!
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

     public Driver() {
     }

     public Driver(Long driverId, @NotBlank String driverName, @NotBlank String licenseNumber,
               @NotBlank @Pattern(regexp = "\\d{10}") String phoneNumber, @Min(0) int experienceYears,
               @NotBlank String address, @NotBlank String availabilityStatus) {
          this.driverId = driverId;
          this.driverName = driverName;
          this.licenseNumber = licenseNumber;
          this.phoneNumber = phoneNumber;
          this.experienceYears = experienceYears;
          this.address = address;
          this.availabilityStatus = availabilityStatus;
     }

     public Driver(@NotBlank String driverName, @NotBlank String licenseNumber,
               @NotBlank @Pattern(regexp = "\\d{10}") String phoneNumber, @Min(0) int experienceYears,
               @NotBlank String address, @NotBlank String availabilityStatus) {
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

     @Override
     public String toString() {
          return "Driver [driverId=" + driverId + ", driverName=" + driverName + ", licenseNumber=" + licenseNumber
                    + ", phoneNumber=" + phoneNumber + ", experienceYears=" + experienceYears + ", address=" + address
                    + ", availabilityStatus=" + availabilityStatus + "]";
     }

}
