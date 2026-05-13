package com.edutech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edutech.entity.Driver;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
     // Search by name (case-insensitive)
     @Query("SELECT d FROM Driver d WHERE LOWER(d.driverName) LIKE LOWER(CONCAT('%', :name, '%'))")
     List<Driver> findByNameIgnoreCase(@Param("name") String name);

     // Filter by availability status
     @Query("SELECT d FROM Driver d WHERE d.availabilityStatus = :status")
     List<Driver> findByAvailabilityStatus(@Param("status") String status);

     // Sort by experience years ascending
     @Query("SELECT d FROM Driver d ORDER BY d.experienceYears ASC")
     List<Driver> findAllSortedByExperienceAsc();

     // Sort by experience years descending
     @Query("SELECT d FROM Driver d ORDER BY d.experienceYears DESC")
     List<Driver> findAllSortedByExperienceDesc();

     // Check if license number already exists
     @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Driver d WHERE d.licenseNumber = :licenseNumber")
     boolean existsByLicenseNumber(@Param("licenseNumber") String licenseNumber);

     // Find by license number
     @Query("SELECT d FROM Driver d WHERE d.licenseNumber = :licenseNumber")
     Optional<Driver> findByLicenseNumber(@Param("licenseNumber") String licenseNumber);

}