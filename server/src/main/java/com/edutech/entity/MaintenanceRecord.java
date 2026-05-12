package com.edutech.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class MaintenanceRecord {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long maintenanceId;
     @NotNull
     @PastOrPresent
     LocalDate serviceDate;
     @NotBlank
     String serviceType;
     @NotBlank
     String serviceCenter;
     @DecimalMin(value = "0.0",inclusive = false)
     double serviceCost;
     LocalDate nextServiceDate;
     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "vehicle_id")
     @NotNull
     Vehicle vehicle;
}
