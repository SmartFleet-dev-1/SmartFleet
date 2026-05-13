package com.edutech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutech.dto.MaintenanceRecordDTO;
import com.edutech.entity.MaintenanceRecord;
import com.edutech.service.MaintenanceRecordService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin("*")
public class MaintenanceRecordController {

     // Add the required code here!
     @Autowired
     private MaintenanceRecordService service;

     // ✅ POST /api/maintenance?vehicleId=
     @PostMapping
     public ResponseEntity<MaintenanceRecordDTO> addRecord(
               @RequestParam Long vehicleId,
               @Valid @RequestBody MaintenanceRecord record) {

          return ResponseEntity.status(201)
                    .body(service.addRecord(record, vehicleId));
     }

     // ✅ GET /api/maintenance
     @GetMapping
     public ResponseEntity<List<MaintenanceRecordDTO>> getAllRecords() {
          return ResponseEntity.ok(service.getAllRecords());
     }

     // ✅ GET /api/maintenance/{id}
     @GetMapping("/{id}")
     public ResponseEntity<MaintenanceRecordDTO> getRecordById(@PathVariable Long id) {
          return ResponseEntity.ok(service.getRecordById(id));
     }

     // ✅ PUT /api/maintenance/{id}?vehicleId=
     @PutMapping("/{id}")
     public ResponseEntity<MaintenanceRecordDTO> updateRecord(
               @PathVariable Long id,
               @RequestParam Long vehicleId,
               @Valid @RequestBody MaintenanceRecord record) {

          return ResponseEntity.ok(service.updateRecord(id, record, vehicleId));
     }

     // ✅ DELETE /api/maintenance/{id}
     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteRecord(@PathVariable Long id) {
          service.deleteRecord(id);
          return ResponseEntity.ok("Maintenance record deleted successfully");
     }

     // ✅ GET /api/maintenance/search?serviceType=
     @GetMapping("/search")
     public ResponseEntity<List<MaintenanceRecordDTO>> searchByServiceType(
               @RequestParam String serviceType) {

          return ResponseEntity.ok(service.searchByServiceType(serviceType));
     }

     // ✅ GET /api/maintenance/filter/date?date=
     @GetMapping("/filter/date")
     public ResponseEntity<List<MaintenanceRecordDTO>> filterByDate(
               @RequestParam String date) {

          return ResponseEntity.ok(
                    service.filterByServiceDate(LocalDate.parse(date)));
     }

     // ✅ GET /api/maintenance/sort/cost?order=asc|desc
     @GetMapping("/sort/cost")
     public ResponseEntity<List<MaintenanceRecordDTO>> sortByCost(
               @RequestParam String order) {

          return ResponseEntity.ok(service.sortByServiceCost(order));
     }

     // ✅ GET /api/maintenance/vehicle/{vehicleId}
     @GetMapping("/vehicle/{vehicleId}")
     public ResponseEntity<List<MaintenanceRecordDTO>> getRecordsByVehicle(
               @PathVariable Long vehicleId) {

          return ResponseEntity.ok(service.getRecordsByVehicle(vehicleId));
     }
}
