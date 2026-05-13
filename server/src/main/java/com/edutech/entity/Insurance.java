package com.edutech.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class Insurance {

     // Add the required code here!
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long insuranceId;

     @NotBlank
     private String providerName;

     @NotBlank
     @Column(unique = true)
     private String policyNumber;

     @NotNull
     private LocalDate startDate;

     @NotNull
     private LocalDate endDate;

     @DecimalMin(value = "0.0", inclusive = false)
     private double premiumAmount;

     @NotBlank
     private String coverageType;

     @OneToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "vehicle_id", nullable = false)
     private Vehicle vehicle;

     public Insurance() {
     }

     public Insurance(Long insuranceId, @NotBlank String providerName, @NotBlank String policyNumber,
               @NotNull LocalDate startDate, @NotNull LocalDate endDate,
               @DecimalMin(value = "0.0", inclusive = false) double premiumAmount, @NotBlank String coverageType,
               Vehicle vehicle) {
          this.insuranceId = insuranceId;
          this.providerName = providerName;
          this.policyNumber = policyNumber;
          this.startDate = startDate;
          this.endDate = endDate;
          this.premiumAmount = premiumAmount;
          this.coverageType = coverageType;
          this.vehicle = vehicle;
     }

     public String getProviderName() {
          return providerName;
     }

     public void setProviderName(String providerName) {
          this.providerName = providerName;
     }

     public String getPolicyNumber() {
          return policyNumber;
     }

     public void setPolicyNumber(String policyNumber) {
          this.policyNumber = policyNumber;
     }

     public LocalDate getStartDate() {
          return startDate;
     }

     public void setStartDate(LocalDate startDate) {
          this.startDate = startDate;
     }

     public LocalDate getEndDate() {
          return endDate;
     }

     public void setEndDate(LocalDate endDate) {
          this.endDate = endDate;
     }

     public double getPremiumAmount() {
          return premiumAmount;
     }

     public void setPremiumAmount(double premiumAmount) {
          this.premiumAmount = premiumAmount;
     }

     public String getCoverageType() {
          return coverageType;
     }

     public void setCoverageType(String coverageType) {
          this.coverageType = coverageType;
     }

     public Vehicle getVehicle() {
          return vehicle;
     }

     public void setVehicle(Vehicle vehicle) {
          this.vehicle = vehicle;
     }

     @Override
     public String toString() {
          return "Insurance [insuranceId=" + insuranceId + ", providerName=" + providerName + ", policyNumber="
                    + policyNumber + ", startDate=" + startDate + ", endDate=" + endDate + ", premiumAmount="
                    + premiumAmount + ", coverageType=" + coverageType + ", vehicle=" + vehicle + "]";
     }

     public Long getInsuranceId() {
          return insuranceId;
     }

     public void setInsuranceId(Long insuranceId) {
          this.insuranceId = insuranceId;
     }

}
