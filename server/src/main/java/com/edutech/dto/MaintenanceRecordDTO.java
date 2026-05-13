package com.edutech.dto;

import java.time.LocalDate;

public class MaintenanceRecordDTO {

     // Add the required code here!
     private Long maintenanceId;
     private LocalDate serviceDate;
     private String serviceType;
     private String serviceCenter;
     private double serviceCost;
     private LocalDate nextServiceDate;
     private String remarks;

     private Long vehicleId;
     private String vehicleNumber;

     public MaintenanceRecordDTO() {
     }

     public MaintenanceRecordDTO(Long maintenanceId, LocalDate serviceDate, String serviceType, String serviceCenter,
               double serviceCost, LocalDate nextServiceDate, String remarks, Long vehicleId, String vehicleNumber) {
          this.maintenanceId = maintenanceId;
          this.serviceDate = serviceDate;
          this.serviceType = serviceType;
          this.serviceCenter = serviceCenter;
          this.serviceCost = serviceCost;
          this.nextServiceDate = nextServiceDate;
          this.remarks = remarks;
          this.vehicleId = vehicleId;
          this.vehicleNumber = vehicleNumber;
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

}
