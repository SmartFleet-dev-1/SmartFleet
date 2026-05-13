package com.edutech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.dto.DriverDTO;
import com.edutech.entity.Driver;
import com.edutech.exception.DuplicateResourceException;
import com.edutech.exception.ResourceNotFoundException;
import com.edutech.repository.DriverRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService {

     // Add the required code here!
     @Autowired
     private DriverRepository driverRepository;

     // Convert Entity → DTO
     private DriverDTO mapToDTO(Driver d) {
          DriverDTO dto = new DriverDTO();

          dto.setDriverId(d.getDriverId());
          dto.setDriverName(d.getDriverName());
          dto.setLicenseNumber(d.getLicenseNumber());
          dto.setPhoneNumber(d.getPhoneNumber());
          dto.setExperienceYears(d.getExperienceYears());
          dto.setAddress(d.getAddress());
          dto.setAvailabilityStatus(d.getAvailabilityStatus());

          return dto;
     }

     // POST /api/drivers
     public DriverDTO addDriver(Driver driver) {

          // Use your custom exists query
          if (driverRepository.existsByLicenseNumber(driver.getLicenseNumber())) {
               throw new DuplicateResourceException("licenseNumber already exists");
          }

          return mapToDTO(driverRepository.save(driver));
     }

     // GET /api/drivers
     public List<DriverDTO> getAllDrivers() {
          return driverRepository.findAll()
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
     }

     // GET /api/drivers/{id}
     public DriverDTO getDriverById(Long id) {
          Driver driver = driverRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

          return mapToDTO(driver);
     }

     // PUT /api/drivers/{id}
     public DriverDTO updateDriver(Long id, Driver updated) {

          Driver existing = driverRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

          // Check duplicate license if changed
          if (!existing.getLicenseNumber().equals(updated.getLicenseNumber())) {
               if (driverRepository.existsByLicenseNumber(updated.getLicenseNumber())) {
                    throw new DuplicateResourceException("licenseNumber already exists");
               }
          }

          existing.setDriverName(updated.getDriverName());
          existing.setLicenseNumber(updated.getLicenseNumber());
          existing.setPhoneNumber(updated.getPhoneNumber());
          existing.setExperienceYears(updated.getExperienceYears());
          existing.setAddress(updated.getAddress());
          existing.setAvailabilityStatus(updated.getAvailabilityStatus());

          return mapToDTO(driverRepository.save(existing));
     }

     // DELETE /api/drivers/{id}
     public void deleteDriver(Long id) {

          Driver d = driverRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

          driverRepository.delete(d);
     }

     // GET /api/drivers/search?name=
     public List<DriverDTO> searchByName(String name) {

          // Uses your custom JPQL query
          return driverRepository.findByNameIgnoreCase(name)
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
     }

     // GET /api/drivers/filter/availability?status=
     public List<DriverDTO> filterByAvailability(String status) {

          return driverRepository.findByAvailabilityStatus(status)
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
     }

     // GET /api/drivers/sort/experience?order=asc|desc
     public List<DriverDTO> sortByExperience(String order) {

          List<Driver> list;

          if ("desc".equalsIgnoreCase(order)) {
               list = driverRepository.findAllSortedByExperienceDesc();
          } else {
               list = driverRepository.findAllSortedByExperienceAsc();
          }

          return list.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
     }
}
