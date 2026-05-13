package com.edutech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edutech.entity.Insurance;

import java.util.List;
import java.util.Optional;
@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

     // Get all insurance records for a specific vehicle
     @Query("SELECT i FROM Insurance i WHERE i.vehicle.vehicleId = :vehicleId")
     List<Insurance> findByVehicleId(@Param("vehicleId") Long vehicleId);

     // Search by provider name (case-insensitive)
     @Query("SELECT i FROM Insurance i WHERE LOWER(i.providerName) LIKE LOWER(CONCAT('%', :providerName, '%'))")
     List<Insurance> findByProviderNameIgnoreCase(@Param("providerName") String providerName);

     // Filter by coverage type
     @Query("SELECT i FROM Insurance i WHERE i.coverageType = :coverageType")
     List<Insurance> findByCoverageType(@Param("coverageType") String coverageType);

     // Sort by premium amount ascending
     @Query("SELECT i FROM Insurance i ORDER BY i.premiumAmount ASC")
     List<Insurance> findAllSortedByPremiumAsc();

     // Sort by premium amount descending
     @Query("SELECT i FROM Insurance i ORDER BY i.premiumAmount DESC")
     List<Insurance> findAllSortedByPremiumDesc();

     // Check if policy number already exists
     @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Insurance i WHERE i.policyNumber = :policyNumber")
     boolean existsByPolicyNumber(@Param("policyNumber") String policyNumber);

     // Validate: end date must not be before start date
     @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Insurance i WHERE i.insuranceId = :id AND i.endDate < i.startDate")
     boolean isEndDateBeforeStartDate(@Param("id") Long id);

}