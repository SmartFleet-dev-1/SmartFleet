package com.edutech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.dto.VehicleDTO;
import com.edutech.entity.Driver;
import com.edutech.entity.Vehicle;
import com.edutech.exception.DuplicateResourceException;
import com.edutech.exception.ResourceNotFoundException;
import com.edutech.repository.DriverRepository;
import com.edutech.repository.VehicleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverRepository driverRepository;

    private VehicleDTO mapToDTO(Vehicle v) {
        VehicleDTO dto = new VehicleDTO();
        dto.setVehicleId(v.getVehicleId());
        dto.setVehicleNumber(v.getVehicleNumber());
        dto.setVehicleType(v.getVehicleType());
        dto.setBrand(v.getBrand());
        dto.setModel(v.getModel());
        dto.setManufacturingYear(v.getManufacturingYear());
        dto.setFuelType(v.getFuelType());
        dto.setMileage(v.getMileage());
        dto.setStatus(v.getStatus());

        if (v.getDriver() != null) {
            dto.setDriverId(v.getDriver().getDriverId());
            dto.setDriverName(v.getDriver().getDriverName());
        }
        return dto;
    }

    public VehicleDTO addVehicle(Vehicle vehicle) {
        if (vehicleRepository.findByVehicleNumber(vehicle.getVehicleNumber()).isPresent()) {
            throw new DuplicateResourceException("vehicleNumber already exists");
        }
        return mapToDTO(vehicleRepository.save(vehicle));
    }

    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public VehicleDTO getVehicleById(Long id) {
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        return mapToDTO(v);
    }

    public VehicleDTO updateVehicle(Long id, Vehicle updated) {
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        if (!v.getVehicleNumber().equals(updated.getVehicleNumber()) &&
                vehicleRepository.findByVehicleNumber(updated.getVehicleNumber()).isPresent()) {
            throw new DuplicateResourceException("vehicleNumber already exists");
        }

        v.setVehicleNumber(updated.getVehicleNumber());
        v.setVehicleType(updated.getVehicleType());
        v.setBrand(updated.getBrand());
        v.setModel(updated.getModel());
        v.setManufacturingYear(updated.getManufacturingYear());
        v.setFuelType(updated.getFuelType());
        v.setMileage(updated.getMileage());
        v.setStatus(updated.getStatus());

        return mapToDTO(vehicleRepository.save(v));
    }

    public void deleteVehicle(Long id) {
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        vehicleRepository.delete(v);
    }

    public VehicleDTO searchByVehicleNumber(String num) {
        Vehicle v = vehicleRepository.findByVehicleNumber(num)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        return mapToDTO(v);
    }

    public List<VehicleDTO> searchByBrand(String brand) {
        return vehicleRepository.findByBrandIgnoreCase(brand)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<VehicleDTO> filterByStatus(String status) {
        return vehicleRepository.findByStatus(status)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<VehicleDTO> sortByManufacturingYear(String order) {
        List<Vehicle> list = "desc".equalsIgnoreCase(order)
                ? vehicleRepository.findAllSortedByYearDesc()
                : vehicleRepository.findAllSortedByYearAsc();

        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<VehicleDTO> sortByMileage(String order) {
        List<Vehicle> list = "desc".equalsIgnoreCase(order)
                ? vehicleRepository.findAllSortedByMileageDesc()
                : vehicleRepository.findAllSortedByMileageAsc();

        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public VehicleDTO assignDriver(Long vehicleId, Long driverId) {
        Vehicle v = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        Driver d = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        v.setDriver(d);
        d.setAvailabilityStatus("Assigned");

        driverRepository.save(d);

        return mapToDTO(vehicleRepository.save(v));
    }

    // ✅ NEW — UNASSIGN DRIVER
    public VehicleDTO unassignDriver(Long vehicleId) {
        Vehicle v = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        if (v.getDriver() == null) {
            throw new RuntimeException("No driver assigned to this vehicle");
        }

        Driver d = v.getDriver();
        d.setAvailabilityStatus("Active");
        driverRepository.save(d);

        v.setDriver(null);
        return mapToDTO(vehicleRepository.save(v));
    }
}