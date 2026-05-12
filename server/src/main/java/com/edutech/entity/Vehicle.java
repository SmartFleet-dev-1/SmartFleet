package com.edutech.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Vehicle {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long vehicleId;
     @NotBlank
     @Column(unique = true)
     String vehicleNumber;
     @NotBlank
     String vehicleType;
     @NotBlank
     String brand;
     @NotBlank
     String model;
     @Min(value = 1900)
     @Max(value = 2100)
     int manufacturingYear;
     @NotBlank
     String fuelType;
     @Min(value = 0)
     double mileage;
     @NotBlank
     String status;
     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "driver_id")
     Driver driver;
     @OneToMany(mappedBy = "vehicle" , cascade = CascadeType.ALL, orphanRemoval = true)
     List <MaintenanceRecord> maintenanceRecords=new ArrayList<>();
     @OneToOne(mappedBy = "vehicle",cascade = CascadeType.ALL)
     Insurance insurance;
     public Vehicle() {
     }
     public Vehicle(Long vehicleId, @NotBlank String vehicleNumber, @NotBlank String vehicleType,
               @NotBlank String brand, @NotBlank String model, @Min(1900) @Max(2100) int manufacturingYear,
               @NotBlank String fuelType, @Min(0) double mileage, @NotBlank String status, Driver driver,
               List<MaintenanceRecord> maintenanceRecords, Insurance insurance) {
          this.vehicleId = vehicleId;
          this.vehicleNumber = vehicleNumber;
          this.vehicleType = vehicleType;
          this.brand = brand;
          this.model = model;
          this.manufacturingYear = manufacturingYear;
          this.fuelType = fuelType;
          this.mileage = mileage;
          this.status = status;
          this.driver = driver;
          this.maintenanceRecords = maintenanceRecords;
          this.insurance = insurance;
     }
     public Vehicle(@NotBlank String vehicleNumber, @NotBlank String vehicleType, @NotBlank String brand,
               @NotBlank String model, @Min(1900) @Max(2100) int manufacturingYear, @NotBlank String fuelType,
               @Min(0) double mileage, @NotBlank String status, Driver driver,
               List<MaintenanceRecord> maintenanceRecords, Insurance insurance) {
          this.vehicleNumber = vehicleNumber;
          this.vehicleType = vehicleType;
          this.brand = brand;
          this.model = model;
          this.manufacturingYear = manufacturingYear;
          this.fuelType = fuelType;
          this.mileage = mileage;
          this.status = status;
          this.driver = driver;
          this.maintenanceRecords = maintenanceRecords;
          this.insurance = insurance;
     }
     public Long getVehicleId() {
         return vehicleId;
     }
     public void setVehicleId(Long vehicleId) {
         this.vehicleId = vehicleId;
     }
     public String getVehicleNumber() {
         return vehicleNumber;
     }
     public void setVehicleNumber(String vehicleNumber) {
         this.vehicleNumber = vehicleNumber;
     }
     public String getVehicleType() {
         return vehicleType;
     }
     public void setVehicleType(String vehicleType) {
         this.vehicleType = vehicleType;
     }
     public String getBrand() {
         return brand;
     }
     public void setBrand(String brand) {
         this.brand = brand;
     }
     public String getModel() {
         return model;
     }
     public void setModel(String model) {
         this.model = model;
     }
     public int getManufacturingYear() {
         return manufacturingYear;
     }
     public void setManufacturingYear(int manufacturingYear) {
         this.manufacturingYear = manufacturingYear;
     }
     public String getFuelType() {
         return fuelType;
     }
     public void setFuelType(String fuelType) {
         this.fuelType = fuelType;
     }
     public double getMileage() {
         return mileage;
     }
     public void setMileage(double mileage) {
         this.mileage = mileage;
     }
     public String getStatus() {
         return status;
     }
     public void setStatus(String status) {
         this.status = status;
     }
     public Driver getDriver() {
         return driver;
     }
     public void setDriver(Driver driver) {
         this.driver = driver;
     }
     public List<MaintenanceRecord> getMaintenanceRecords() {
         return maintenanceRecords;
     }
     public void setMaintenanceRecords(List<MaintenanceRecord> maintenanceRecords) {
         this.maintenanceRecords = maintenanceRecords;
     }
     public Insurance getInsurance() {
         return insurance;
     }
     public void setInsurance(Insurance insurance) {
         this.insurance = insurance;
     }
     @Override
     public String toString() {
          return "Vehicle [vehicleId=" + vehicleId + ", vehicleNumber=" + vehicleNumber + ", vehicleType=" + vehicleType
                    + ", brand=" + brand + ", model=" + model + ", manufacturingYear=" + manufacturingYear
                    + ", fuelType=" + fuelType + ", mileage=" + mileage + ", status=" + status + ", driver=" + driver
                    + ", maintenanceRecords=" + maintenanceRecords + ", insurance=" + insurance + "]";
     }

     
}
