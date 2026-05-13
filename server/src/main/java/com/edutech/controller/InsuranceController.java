package com.edutech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edutech.dto.InsuranceDTO;
import com.edutech.entity.Insurance;
import com.edutech.service.InsuranceService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/insurance")
@CrossOrigin("*")
public class InsuranceController {

     @Autowired
     private InsuranceService service;

     // ✅ POST /api/insurance?vehicleId=
     @PostMapping
     public ResponseEntity<InsuranceDTO> addInsurance(
               @RequestParam Long vehicleId,
               @Valid @RequestBody Insurance insurance) {

          return ResponseEntity.status(201).body(service.addInsurance(insurance, vehicleId));
     }

     // ✅ GET /api/insurance
     @GetMapping
     public ResponseEntity<List<InsuranceDTO>> getAllInsurance() {
          return ResponseEntity.ok(service.getAllInsurance());
     }

     // ✅ GET /api/insurance/{id}
     @GetMapping("/{id}")
     public ResponseEntity<InsuranceDTO> getInsuranceById(@PathVariable Long id) {
          return ResponseEntity.ok(service.getInsuranceById(id));
     }

     // ✅ PUT /api/insurance/{id}?vehicleId=
     @PutMapping("/{id}")
     public ResponseEntity<InsuranceDTO> updateInsurance(
               @PathVariable Long id,
               @RequestParam Long vehicleId,
               @Valid @RequestBody Insurance insurance) {

          return ResponseEntity.ok(service.updateInsurance(id, insurance, vehicleId));
     }

     // ✅ DELETE /api/insurance/{id}
     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteInsurance(@PathVariable Long id) {
          service.deleteInsurance(id);
          return ResponseEntity.ok("Insurance deleted successfully");
     }

     // ✅ GET /api/insurance/search?providerName=
     @GetMapping("/search")
     public ResponseEntity<List<InsuranceDTO>> searchByProviderName(@RequestParam String providerName) {
          return ResponseEntity.ok(service.searchByProviderName(providerName));
     }

     // ✅ GET /api/insurance/filter/coverage?coverageType=
     @GetMapping("/filter/coverage")
     public ResponseEntity<List<InsuranceDTO>> filterByCoverageType(@RequestParam String coverageType) {
          return ResponseEntity.ok(service.filterByCoverageType(coverageType));
     }

     // ✅ GET /api/insurance/sort/premium?order=asc|desc
    @GetMapping("/sort/premium")
    public ResponseEntity<List<InsuranceDTO>> sortByPremium(@RequestParam String order) {
        return ResponseEntity.ok(service.sortByPremiumAmount(order));
    }
}
