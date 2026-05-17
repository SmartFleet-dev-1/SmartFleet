package com.edutech.controller;

import javax.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.edutech.dto.TripDTO;
import com.edutech.entity.Driver;
import com.edutech.entity.Role;
import com.edutech.entity.Trip;
import com.edutech.entity.TripStatus;
import com.edutech.entity.User;
import com.edutech.repository.DriverRepository;
import com.edutech.service.NotificationService;
import com.edutech.service.TripService;
import com.edutech.service.UserService;

@RestController
@RequestMapping("/api/trips")
@CrossOrigin("*")
public class TripController {

    @Autowired
    private TripService tripService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DriverRepository driverRepository;

    // POST /api/trips?vehicleId=&driverId=
    @PostMapping
    public ResponseEntity<TripDTO> createTrip(
            @RequestParam Long vehicleId,
            @RequestParam Long driverId,
            @Valid @RequestBody Trip trip,
            @AuthenticationPrincipal UserDetails userDetails) {

        User admin = userService.findByUsername(userDetails.getUsername());
        TripDTO created = tripService.createTrip(vehicleId, driverId, trip, admin);

        Driver driver = driverRepository.findById(driverId).orElse(null);
        if (driver != null && driver.getUser() != null) {
            notificationService.notifyTripAssigned(
                    driver.getUser(),
                    trip.getStartLocation(),
                    trip.getEndLocation());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /api/trips
    @GetMapping
    public ResponseEntity<List<TripDTO>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    // GET /api/trips/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> getTripById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTripById(id));
    }

    // GET /api/trips/driver/{driverId}
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<TripDTO>> getTripsByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(tripService.getTripsByDriver(driverId));
    }

    // GET /api/trips/my-trips (driver sees own trips)
    @GetMapping("/my-trips")
    public ResponseEntity<List<TripDTO>> getMyTrips(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername());
        Long driverId = findDriverIdByUser(user);

        if (driverId == null) {
            return ResponseEntity.ok(List.of());
        }

        return ResponseEntity.ok(tripService.getTripsByDriver(driverId));
    }

    // GET /api/trips/filter/status?status=
    @GetMapping("/filter/status")
    public ResponseEntity<List<TripDTO>> getTripsByStatus(@RequestParam String status) {
        TripStatus tripStatus = TripStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(tripService.getTripsByStatus(tripStatus));
    }

    // PUT /api/trips/{id}/start
    @PutMapping("/{id}/start")
    public ResponseEntity<TripDTO> startTrip(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.startTrip(id));
    }

    // PUT /api/trips/{id}/complete
    @PutMapping("/{id}/complete")
    public ResponseEntity<TripDTO> completeTrip(@PathVariable Long id) {
        TripDTO completed = tripService.completeTrip(id);

        notificationService.notifyTripCompleted(
                completed.getDriverName(),
                completed.getStartLocation(),
                completed.getEndLocation());

        return ResponseEntity.ok(completed);
    }

    // PUT /api/trips/{id}/cancel
    @PutMapping("/{id}/cancel")
    public ResponseEntity<TripDTO> cancelTrip(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername());
        boolean isAdmin = user.getRole() == Role.ADMIN;

        return ResponseEntity.ok(tripService.cancelTrip(id, isAdmin));
    }

    // DELETE /api/trips/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.ok("Trip deleted successfully");
    }

    // Helper: find driverId linked to user
    private Long findDriverIdByUser(User user) {
        List<Driver> allDrivers = driverRepository.findAll();
        for (Driver d : allDrivers) {
            if (d.getUser() != null && d.getUser().getId().equals(user.getId())) {
                return d.getDriverId();
            }
        }
        return null;
    }
}