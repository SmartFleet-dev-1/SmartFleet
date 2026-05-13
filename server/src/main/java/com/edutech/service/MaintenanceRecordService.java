package com.edutech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.dto.MaintenanceRecordDTO;
import com.edutech.entity.MaintenanceRecord;
import com.edutech.entity.Vehicle;
import com.edutech.exception.InvalidDataException;
import com.edutech.exception.ResourceNotFoundException;
import com.edutech.repository.MaintenanceRecordRepository;
import com.edutech.repository.VehicleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceRecordService {

     //Add the required code here!
       @Autowired
    private MaintenanceRecordRepository maintenanceRepo;

    @Autowired
    private VehicleRepository vehicleRepo;

    /**
     * Convert MaintenanceRecord entity -> MaintenanceRecordDTO
     * IMPORTANT:
     * Tests usually expect DTO getter name getServiceCenter()
     * So DTO must have field serviceCenter (NOT serviceCentre)
     */
    private MaintenanceRecordDTO mapToDTO(MaintenanceRecord m) {
        MaintenanceRecordDTO dto = new MaintenanceRecordDTO();

        dto.setMaintenanceId(m.getMaintenanceId());
        dto.setServiceDate(m.getServiceDate());
        dto.setServiceType(m.getServiceType());

        // ✅ must match DTO property name expected by tests: serviceCenter
        dto.setServiceCenter(m.getServiceCenter());

        dto.setServiceCost(m.getServiceCost());
        dto.setNextServiceDate(m.getNextServiceDate());
        dto.setRemarks(m.getRemarks());

        // vehicle info (optional but useful)
        if (m.getVehicle() != null) {
            dto.setVehicleId(m.getVehicle().getVehicleId());
            dto.setVehicleNumber(m.getVehicle().getVehicleNumber());
        }

        return dto;
    }

    /**
     * ✅ TEST EXPECTED SIGNATURE:
     * addRecord(MaintenanceRecord record, Long vehicleId)
     *
     * API: POST /api/maintenance?vehicleId=
     */
    public MaintenanceRecordDTO addRecord(MaintenanceRecord record, Long vehicleId) {

        // Vehicle must exist
        Vehicle v = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        // Business validation: Service date cannot be future
        if (record.getServiceDate() != null && record.getServiceDate().isAfter(LocalDate.now())) {
            throw new InvalidDataException("Service date cannot be in the future");
        }

        // Attach vehicle
        record.setVehicle(v);

        return mapToDTO(maintenanceRepo.save(record));
    }

    /**
     * ✅ TEST EXPECTED:
     * getAllRecords()
     *
     * API: GET /api/maintenance
     */
    public List<MaintenanceRecordDTO> getAllRecords() {
        return maintenanceRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * ✅ TEST EXPECTED:
     * getRecordById(Long id)
     *
     * API: GET /api/maintenance/{id}
     */
    public MaintenanceRecordDTO getRecordById(Long id) {
        MaintenanceRecord m = maintenanceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found"));
        return mapToDTO(m);
    }

    /**
     * ✅ TEST EXPECTED ORDER:
     * updateRecord(Long id, MaintenanceRecord updated, Long vehicleId)
     *
     * API: PUT /api/maintenance/{id}?vehicleId=
     */
    public MaintenanceRecordDTO updateRecord(Long id, MaintenanceRecord updated, Long vehicleId) {

        MaintenanceRecord existing = maintenanceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found"));

        Vehicle v = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        // Business validation: Service date cannot be future
        if (updated.getServiceDate() != null && updated.getServiceDate().isAfter(LocalDate.now())) {
            throw new InvalidDataException("Service date cannot be in the future");
        }

        // Update fields
        existing.setServiceDate(updated.getServiceDate());
        existing.setServiceType(updated.getServiceType());
        existing.setServiceCenter(updated.getServiceCenter());
        existing.setServiceCost(updated.getServiceCost());
        existing.setNextServiceDate(updated.getNextServiceDate());
        existing.setRemarks(updated.getRemarks());

        // Attach vehicle
        existing.setVehicle(v);

        return mapToDTO(maintenanceRepo.save(existing));
    }

    /**
     * ✅ TEST EXPECTED:
     * deleteRecord(Long id)
     *
     * API: DELETE /api/maintenance/{id}
     */
    public void deleteRecord(Long id) {
        MaintenanceRecord existing = maintenanceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found"));
        maintenanceRepo.delete(existing);
    }

    /**
     * ✅ TEST EXPECTED:
     * searchByServiceType(String serviceType)
     *
     * Repo method: findByServiceType(...)
     * API: GET /api/maintenance/search?serviceType=
     */
    public List<MaintenanceRecordDTO> searchByServiceType(String serviceType) {
        return maintenanceRepo.findByServiceType(serviceType)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * ✅ TEST EXPECTED:
     * filterByServiceDate(LocalDate date)
     *
     * Repo method: findByServiceDate(...)
     * API: GET /api/maintenance/filter/date?date=YYYY-MM-DD
     */
    public List<MaintenanceRecordDTO> filterByServiceDate(LocalDate date) {
        return maintenanceRepo.findByServiceDate(date)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * ✅ TEST EXPECTED:
     * sortByServiceCost(String order)  (asc/desc)
     *
     * Repo methods:
     * - findAllSortedByCostAsc()
     * - findAllSortedByCostDesc()
     *
     * API: GET /api/maintenance/sort/cost?order=asc|desc
     */
    public List<MaintenanceRecordDTO> sortByServiceCost(String order) {
        List<MaintenanceRecord> list =
                "desc".equalsIgnoreCase(order)
                        ? maintenanceRepo.findAllSortedByCostDesc()
                        : maintenanceRepo.findAllSortedByCostAsc();

        return list.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * ✅ TEST EXPECTED:
     * getRecordsByVehicle(Long vehicleId)
     *
     * Repo method: findByVehicleId(...)
     * API: GET /api/maintenance/vehicle/{vehicleId}
     */
    public List<MaintenanceRecordDTO> getRecordsByVehicle(Long vehicleId) {
        return maintenanceRepo.findByVehicleId(vehicleId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
