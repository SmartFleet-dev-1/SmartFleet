package com.edutech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edutech.entity.MaintenanceRecord;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {
     // Get all maintenance records for a specific vehicle
     @Query("SELECT m FROM MaintenanceRecord m WHERE m.vehicle.vehicleId = :vehicleId")
     List<MaintenanceRecord> findByVehicleId(@Param("vehicleId") Long vehicleId);

     // Search by service type
     @Query("SELECT m FROM MaintenanceRecord m WHERE LOWER(m.serviceType) LIKE LOWER(CONCAT('%', :serviceType, '%'))")
     List<MaintenanceRecord> findByServiceType(@Param("serviceType") String serviceType);

     // Filter by service date
     @Query("SELECT m FROM MaintenanceRecord m WHERE m.serviceDate = :date")
     List<MaintenanceRecord> findByServiceDate(@Param("date") LocalDate date);

     // Sort by cost ascending
     @Query("SELECT m FROM MaintenanceRecord m ORDER BY m.serviceCost ASC")
     List<MaintenanceRecord> findAllSortedByCostAsc();

     // Sort by cost descending
     @Query("SELECT m FROM MaintenanceRecord m ORDER BY m.serviceCost DESC")
     List<MaintenanceRecord> findAllSortedByCostDesc();

     // Validate: service date must not be in the future
     @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MaintenanceRecord m WHERE m.maintenanceId = :id AND m.serviceDate > CURRENT_DATE")
     boolean isServiceDateInFuture(@Param("id") Long id);
}
