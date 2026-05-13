package com.edutech.dto;

public class VehicleDTO {

     // Add the required code here!
     private Long vehicleId;
     private String vehicleNumber;
     private String vehicleType;
     private String brand;
     private String model;
     private int manufacturingYear;
     private String fuelType;
     private double mileage;
     private String status;

     // Flattened driver info (optional)
     private Long driverId;
     private String driverName;

     public VehicleDTO() {
     }

     public VehicleDTO(Long vehicleId, String vehicleNumber, String vehicleType, String brand,
               String model, int manufacturingYear, String fuelType, double mileage,
               String status, Long driverId, String driverName) {
          this.vehicleId = vehicleId;
          this.vehicleNumber = vehicleNumber;
          this.vehicleType = vehicleType;
          this.brand = brand;
          this.model = model;
          this.manufacturingYear = manufacturingYear;
          this.fuelType = fuelType;
          this.mileage = mileage;
          this.status = status;
          this.driverId = driverId;
          this.driverName = driverName;
     }

     // Getters and setters
     public Long getVehicleId() {
          return vehicleId;
     }

     public void setVehicleId(Long v) {
          this.vehicleId = v;
     }

     public String getVehicleNumber() {
          return vehicleNumber;
     }

     public void setVehicleNumber(String v) {
          this.vehicleNumber = v;
     }

     public String getVehicleType() {
          return vehicleType;
     }

     public void setVehicleType(String v) {
          this.vehicleType = v;
     }

     public String getBrand() {
          return brand;
     }

     public void setBrand(String b) {
          this.brand = b;
     }

     public String getModel() {
          return model;
     }

     public void setModel(String m) {
          this.model = m;
     }

     public int getManufacturingYear() {
          return manufacturingYear;
     }

     public void setManufacturingYear(int y) {
          this.manufacturingYear = y;
     }

     public String getFuelType() {
          return fuelType;
     }

     public void setFuelType(String f) {
          this.fuelType = f;
     }

     public double getMileage() {
          return mileage;
     }

     public void setMileage(double m) {
          this.mileage = m;
     }

     public String getStatus() {
          return status;
     }

     public void setStatus(String s) {
          this.status = s;
     }

     public Long getDriverId() {
          return driverId;
     }

     public void setDriverId(Long d) {
          this.driverId = d;
     }

     public String getDriverName() {
          return driverName;
     }

     public void setDriverName(String d) {
          this.driverName = d;
     }
}
