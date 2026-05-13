package com.edutech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edutech.entity.Vehicle;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
     // Find by exact vehicle number
     @Query("SELECT v FROM Vehicle v WHERE v.vehicleNumber = :vehicleNumber")
     Optional<Vehicle> findByVehicleNumber(@Param("vehicleNumber") String vehicleNumber);

     // Search by brand (case-insensitive)
     @Query("SELECT v FROM Vehicle v WHERE LOWER(v.brand) LIKE LOWER(CONCAT('%', :brand, '%'))")
     List<Vehicle> findByBrandIgnoreCase(@Param("brand") String brand);

     // Filter by status
     @Query("SELECT v FROM Vehicle v WHERE v.status = :status")
     List<Vehicle> findByStatus(@Param("status") String status);

     // Sort by manufacturing year ascending
     @Query("SELECT v FROM Vehicle v ORDER BY v.manufacturingYear ASC")
     List<Vehicle> findAllSortedByYearAsc();

     // Sort by manufacturing year descending
     @Query("SELECT v FROM Vehicle v ORDER BY v.manufacturingYear DESC")
     List<Vehicle> findAllSortedByYearDesc();

     // Sort by mileage ascending
     @Query("SELECT v FROM Vehicle v ORDER BY v.mileage ASC")
     List<Vehicle> findAllSortedByMileageAsc();

     // Sort by mileage descending
     @Query("SELECT v FROM Vehicle v ORDER BY v.mileage DESC")
     List<Vehicle> findAllSortedByMileageDesc();

     // Check if vehicle number already exists
     @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Vehicle v WHERE v.vehicleNumber = :vehicleNumber")
     boolean existsByVehicleNumber(@Param("vehicleNumber") String vehicleNumber);

}
