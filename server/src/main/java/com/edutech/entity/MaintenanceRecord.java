package com.edutech.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class MaintenanceRecord {

     // Add the required code here!
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long maintenanceId;

     @NotNull
     @PastOrPresent
     private LocalDate serviceDate;

     @NotBlank
     private String serviceType;

     @NotBlank
          private String serviceCenter;

     @DecimalMin(value = "0.0", inclusive = false)
     private double serviceCost;

     private LocalDate nextServiceDate; // optional

     private String remarks;

     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "vehicle_id", nullable = false)
     private Vehicle vehicle;

     public MaintenanceRecord() {
     }

     public MaintenanceRecord(Long maintenanceId, @NotNull @PastOrPresent LocalDate serviceDate,
               @NotBlank String serviceType, @NotBlank String serviceCenter,
               @DecimalMin(value = "0.0", inclusive = false) double serviceCost, LocalDate nextServiceDate,
               String remarks,
               Vehicle vehicle) {
          this.maintenanceId = maintenanceId;
          this.serviceDate = serviceDate;
          this.serviceType = serviceType;
          this.serviceCenter = serviceCenter;
          this.serviceCost = serviceCost;
          this.nextServiceDate = nextServiceDate;
          this.remarks = remarks;
          this.vehicle = vehicle;
     }

     public MaintenanceRecord(@NotNull @PastOrPresent LocalDate serviceDate, @NotBlank String serviceType,
               @NotBlank String serviceCenter, @DecimalMin(value = "0.0", inclusive = false) double serviceCost,
               LocalDate nextServiceDate, String remarks, Vehicle vehicle) {
          this.serviceDate = serviceDate;
          this.serviceType = serviceType;
          this.serviceCenter = serviceCenter;
          this.serviceCost = serviceCost;
          this.nextServiceDate = nextServiceDate;
          this.remarks = remarks;
          this.vehicle = vehicle;
     }

     public Long getMaintenanceId() {
          return maintenanceId;
     }

     public void setMaintenanceId(Long maintenanceId) {
          this.maintenanceId = maintenanceId;
     }

     public LocalDate getServiceDate() {
          return serviceDate;
     }

     public void setServiceDate(LocalDate serviceDate) {
          this.serviceDate = serviceDate;
     }

     public String getServiceType() {
          return serviceType;
     }

     public void setServiceType(String serviceType) {
          this.serviceType = serviceType;
     }

     public String getServiceCenter() {
          return serviceCenter;
     }

     public void setServiceCenter(String serviceCenter) {
          this.serviceCenter = serviceCenter;
     }

     public double getServiceCost() {
          return serviceCost;
     }

     public void setServiceCost(double serviceCost) {
          this.serviceCost = serviceCost;
     }

     public LocalDate getNextServiceDate() {
          return nextServiceDate;
     }

     public void setNextServiceDate(LocalDate nextServiceDate) {
          this.nextServiceDate = nextServiceDate;
     }

     public String getRemarks() {
          return remarks;
     }

     public void setRemarks(String remarks) {
          this.remarks = remarks;
     }

     public Vehicle getVehicle() {
          return vehicle;
     }

     public void setVehicle(Vehicle vehicle) {
          this.vehicle = vehicle;
     }

     @Override
     public String toString() {
          return "MaintenanceRecord [maintenanceId=" + maintenanceId + ", serviceDate=" + serviceDate + ", serviceType="
                    + serviceType + ", serviceCenter=" + serviceCenter + ", serviceCost=" + serviceCost
                    + ", nextServiceDate=" + nextServiceDate + ", remarks=" + remarks + ", vehicle=" + vehicle + "]";
     }

}
