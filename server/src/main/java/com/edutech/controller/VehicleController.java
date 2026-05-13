package com.edutech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutech.dto.VehicleDTO;
import com.edutech.entity.Vehicle;
import com.edutech.service.VehicleService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin("*")
public class VehicleController {

     // Add the required code here!
     @Autowired
     private VehicleService service;

     //  POST
     @PostMapping
     public ResponseEntity<VehicleDTO> add(@Valid @RequestBody Vehicle v) {
          return ResponseEntity.status(201).body(service.addVehicle(v));
     }

     //  GET all
     @GetMapping
     public ResponseEntity<List<VehicleDTO>> getAll() {
          return ResponseEntity.ok(service.getAllVehicles());
     }

     //  GET by id
     @GetMapping("/{id}")
     public ResponseEntity<VehicleDTO> getById(@PathVariable Long id) {
          return ResponseEntity.ok(service.getVehicleById(id));
     }

     //  UPDATE
     @PutMapping("/{id}")
     public ResponseEntity<VehicleDTO> update(@PathVariable Long id,
               @Valid @RequestBody Vehicle v) {
          return ResponseEntity.ok(service.updateVehicle(id, v));
     }

     //  DELETE
     @DeleteMapping("/{id}")
     public ResponseEntity<String> delete(@PathVariable Long id) {
          service.deleteVehicle(id);
          return ResponseEntity.ok("Vehicle deleted");
     }

     //  SEARCH number
     @GetMapping("/search/number")
     public ResponseEntity<VehicleDTO> searchByNumber(@RequestParam String vehicleNumber) {
          return ResponseEntity.ok(service.searchByVehicleNumber(vehicleNumber));
     }

     //  SEARCH brand
     @GetMapping("/search/brand")
     public ResponseEntity<List<VehicleDTO>> searchByBrand(@RequestParam String brand) {
          return ResponseEntity.ok(service.searchByBrand(brand));
     }

     //  FILTER status
     @GetMapping("/filter/status")
     public ResponseEntity<List<VehicleDTO>> filterByStatus(@RequestParam String status) {
          return ResponseEntity.ok(service.filterByStatus(status));
     }

     //  SORT year
     @GetMapping("/sort/year")
     public ResponseEntity<List<VehicleDTO>> sortByYear(@RequestParam String order) {
          return ResponseEntity.ok(service.sortByManufacturingYear(order));
     }

     //  SORT mileage
     @GetMapping("/sort/mileage")
     public ResponseEntity<List<VehicleDTO>> sortByMileage(@RequestParam String order) {
          return ResponseEntity.ok(service.sortByMileage(order));
     }

     //  ASSIGN DRIVER
     @PutMapping("/{vehicleId}/assign-driver/{driverId}")
     public ResponseEntity<VehicleDTO> assignDriver(@PathVariable Long vehicleId,
               @PathVariable Long driverId) {
          return ResponseEntity.ok(service.assignDriver(vehicleId, driverId));
     }
}
