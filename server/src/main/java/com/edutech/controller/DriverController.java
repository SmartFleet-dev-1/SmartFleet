package com.edutech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutech.dto.DriverDTO;
import com.edutech.entity.Driver;
import com.edutech.service.DriverService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
@CrossOrigin("*")
public class DriverController {

     // Add the required code here!
     @Autowired
     private DriverService driverService;

     // ✅ POST /api/drivers -> Add new driver
     @PostMapping
     public ResponseEntity<DriverDTO> addDriver(@Valid @RequestBody Driver driver) {
          return ResponseEntity.status(201).body(driverService.addDriver(driver));
     }

     // ✅ GET /api/drivers -> Get all drivers
     @GetMapping
     public ResponseEntity<List<DriverDTO>> getAllDrivers() {
          return ResponseEntity.ok(driverService.getAllDrivers());
     }

     // ✅ GET /api/drivers/{id} -> Get driver by ID
     @GetMapping("/{id}")
     public ResponseEntity<DriverDTO> getDriverById(@PathVariable Long id) {
          return ResponseEntity.ok(driverService.getDriverById(id));
     }

     // ✅ PUT /api/drivers/{id} -> Update driver
     @PutMapping("/{id}")
     public ResponseEntity<DriverDTO> updateDriver(
               @PathVariable Long id,
               @Valid @RequestBody Driver driver) {

          return ResponseEntity.ok(driverService.updateDriver(id, driver));
     }

     // ✅ DELETE /api/drivers/{id} -> Delete driver
     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteDriver(@PathVariable Long id) {
          driverService.deleteDriver(id);
          return ResponseEntity.ok("Driver deleted successfully");
     }

     // ✅ GET /api/drivers/search?name=
     @GetMapping("/search")
     public ResponseEntity<List<DriverDTO>> searchByName(@RequestParam String name) {
          return ResponseEntity.ok(driverService.searchByName(name));
     }

     // ✅ GET /api/drivers/filter/availability?status=
     @GetMapping("/filter/availability")
     public ResponseEntity<List<DriverDTO>> filterByAvailability(@RequestParam String status) {
          return ResponseEntity.ok(driverService.filterByAvailability(status));
     }

     // ✅ GET /api/drivers/sort/experience?order=asc|desc
     @GetMapping("/sort/experience")
     public ResponseEntity<List<DriverDTO>> sortByExperience(@RequestParam String order) {
          return ResponseEntity.ok(driverService.sortByExperience(order));
     }
}
