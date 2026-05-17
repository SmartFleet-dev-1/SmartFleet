package com.edutech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edutech.entity.Trip;
import com.edutech.entity.TripStatus;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    // Find trips by driver id
    @Query("SELECT t FROM Trip t WHERE t.driver.driverId = :driverId")
    List<Trip> findByDriverId(@Param("driverId") Long driverId);

    // Find trips by vehicle id
    @Query("SELECT t FROM Trip t WHERE t.vehicle.vehicleId = :vehicleId")
    List<Trip> findByVehicleId(@Param("vehicleId") Long vehicleId);

    // Find trips by status
    @Query("SELECT t FROM Trip t WHERE t.status = :status")
    List<Trip> findByStatus(@Param("status") TripStatus status);

    // Count by status
    @Query("SELECT COUNT(t) FROM Trip t WHERE t.status = :status")
    long countByStatus(@Param("status") TripStatus status);

    // Count all trips
    @Query("SELECT COUNT(t) FROM Trip t")
    long countAll();

    // Find all trips ordered by created date (latest first)
    @Query("SELECT t FROM Trip t ORDER BY t.createdAt DESC")
    List<Trip> findAllOrderByCreatedAtDesc();
}