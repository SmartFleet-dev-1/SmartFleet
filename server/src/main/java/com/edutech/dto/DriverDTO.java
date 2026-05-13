package com.edutech.dto;

public class DriverDTO {

     // Add the required code here!
     private Long driverId;
     private String driverName;
     private String licenseNumber;
     private String phoneNumber;
     private int experienceYears;
     private String address;
     private String availabilityStatus;

     public DriverDTO() {
     }

     public DriverDTO(Long driverId, String driverName, String licenseNumber, String phoneNumber,
               int experienceYears, String address, String availabilityStatus) {
          this.driverId = driverId;
          this.driverName = driverName;
          this.licenseNumber = licenseNumber;
          this.phoneNumber = phoneNumber;
          this.experienceYears = experienceYears;
          this.address = address;
          this.availabilityStatus = availabilityStatus;
     }

     // Getters and setters
     public Long getDriverId() {
          return driverId;
     }

     public void setDriverId(Long d) {
          this.driverId = d;
     }

     public String getDriverName() {
          return driverName;
     }

     public void setDriverName(String n) {
          this.driverName = n;
     }

     public String getLicenseNumber() {
          return licenseNumber;
     }

     public void setLicenseNumber(String l) {
          this.licenseNumber = l;
     }

     public String getPhoneNumber() {
          return phoneNumber;
     }

     public void setPhoneNumber(String p) {
          this.phoneNumber = p;
     }

     public int getExperienceYears() {
          return experienceYears;
     }

     public void setExperienceYears(int e) {
          this.experienceYears = e;
     }

     public String getAddress() {
          return address;
     }

     public void setAddress(String a) {
          this.address = a;
     }

     public String getAvailabilityStatus() {
          return availabilityStatus;
     }

     public void setAvailabilityStatus(String s) {
          this.availabilityStatus = s;
     }
}
