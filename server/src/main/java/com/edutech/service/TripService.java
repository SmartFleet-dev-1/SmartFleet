package com.edutech.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.dto.TripDTO;
import com.edutech.entity.*;
import com.edutech.exception.ResourceNotFoundException;
import com.edutech.repository.DriverRepository;
import com.edutech.repository.TripRepository;
import com.edutech.repository.VehicleRepository;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverRepository driverRepository;

    // Entity → DTO
    private TripDTO mapToDTO(Trip t) {
        TripDTO dto = new TripDTO();
        dto.setTripId(t.getTripId());
        dto.setStartLocation(t.getStartLocation());
        dto.setEndLocation(t.getEndLocation());
        dto.setStartTime(t.getStartTime());
        dto.setEndTime(t.getEndTime());
        dto.setStatus(t.getStatus());
        dto.setCreatedAt(t.getCreatedAt());

        if (t.getVehicle() != null) {
            dto.setVehicleId(t.getVehicle().getVehicleId());
            dto.setVehicleNumber(t.getVehicle().getVehicleNumber());
        }

        if (t.getDriver() != null) {
            dto.setDriverId(t.getDriver().getDriverId());
            dto.setDriverName(t.getDriver().getDriverName());
        }

        if (t.getCreatedBy() != null) {
            dto.setCreatedByUsername(t.getCreatedBy().getUsername());
        }

        return dto;
    }

    // CREATE trip (admin only)
    public TripDTO createTrip(Long vehicleId, Long driverId, Trip trip, User createdBy) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        trip.setVehicle(vehicle);
        trip.setDriver(driver);
        trip.setCreatedBy(createdBy);
        trip.setStatus(TripStatus.ASSIGNED);

        return mapToDTO(tripRepository.save(trip));
    }

    // GET all trips
    public List<TripDTO> getAllTrips() {
        return tripRepository.findAllOrderByCreatedAtDesc()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // GET trip by ID
    public TripDTO getTripById(Long id) {
        Trip t = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        return mapToDTO(t);
    }

    // GET trips by driver
    public List<TripDTO> getTripsByDriver(Long driverId) {
        return tripRepository.findByDriverId(driverId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // GET trips by status
    public List<TripDTO> getTripsByStatus(TripStatus status) {
        return tripRepository.findByStatus(status)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // START trip (driver only — ASSIGNED → IN_PROGRESS)
    public TripDTO startTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        if (trip.getStatus() != TripStatus.ASSIGNED) {
            throw new RuntimeException("Trip can only be started from ASSIGNED status");
        }

        trip.setStatus(TripStatus.IN_PROGRESS);
        trip.setStartTime(LocalDateTime.now());

        return mapToDTO(tripRepository.save(trip));
    }

    // COMPLETE trip (driver only — IN_PROGRESS → COMPLETED)
    public TripDTO completeTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        if (trip.getStatus() != TripStatus.IN_PROGRESS) {
            throw new RuntimeException("Trip can only be completed from IN_PROGRESS status");
        }

        trip.setStatus(TripStatus.COMPLETED);
        trip.setEndTime(LocalDateTime.now());

        return mapToDTO(tripRepository.save(trip));
    }

    // CANCEL trip
    // Admin can cancel anytime, Driver can cancel only when ASSIGNED
    public TripDTO cancelTrip(Long tripId, boolean isAdmin) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        if (!isAdmin && trip.getStatus() != TripStatus.ASSIGNED) {
            throw new RuntimeException("Driver can only cancel trips that are ASSIGNED");
        }

        if (trip.getStatus() == TripStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel a completed trip");
        }

        trip.setStatus(TripStatus.CANCELLED);

        return mapToDTO(tripRepository.save(trip));
    }

    // DELETE trip (admin only)
    public void deleteTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        tripRepository.delete(trip);
    }

    // COUNT methods for dashboard
    public long countAll() {
        return tripRepository.countAll();
    }

    public long countByStatus(TripStatus status) {
        return tripRepository.countByStatus(status);
    }
}