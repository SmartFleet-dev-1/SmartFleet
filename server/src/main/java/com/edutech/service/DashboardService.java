package com.edutech.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutech.dto.DashboardStatsDTO;
import com.edutech.entity.*;
import com.edutech.repository.*;

@Service
public class DashboardService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private MaintenanceRecordRepository maintenanceRepository;

    @Autowired
    private UserRepository userRepository;

    public DashboardStatsDTO getAdminDashboard() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // Vehicle counts
        List<Vehicle> allVehicles = vehicleRepository.findAll();
        stats.setTotalVehicles(allVehicles.size());
        stats.setActiveVehicles(
                allVehicles.stream().filter(v -> "Active".equals(v.getStatus())).count()
        );

        // Driver counts
        List<Driver> allDrivers = driverRepository.findAll();
        stats.setTotalDrivers(allDrivers.size());
        stats.setActiveDrivers(
                allDrivers.stream().filter(d -> "Active".equals(d.getAvailabilityStatus())).count()
        );
        stats.setAssignedDrivers(
                allDrivers.stream().filter(d -> "Assigned".equals(d.getAvailabilityStatus())).count()
        );

        // Trip counts
        stats.setTotalTrips(tripRepository.countAll());
        stats.setCompletedTrips(tripRepository.countByStatus(TripStatus.COMPLETED));
        stats.setActiveTrips(tripRepository.countByStatus(TripStatus.IN_PROGRESS));

        // Pending approvals
        stats.setPendingApprovals(userRepository.countByStatus(UserStatus.PENDING));

        // Expense summary
        List<MaintenanceRecord> allMaintenance = maintenanceRepository.findAll();
        double totalMaintCost = allMaintenance.stream()
                .mapToDouble(MaintenanceRecord::getServiceCost).sum();
        stats.setTotalMaintenanceCost(totalMaintCost);

        List<Insurance> allInsurance = insuranceRepository.findAll();
        double totalInsCost = allInsurance.stream()
                .mapToDouble(Insurance::getPremiumAmount).sum();
        stats.setTotalInsuranceCost(totalInsCost);

        // Expiry alerts — insurance expiring within 30 days
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysLater = now.plusDays(30);

        List<Map<String, Object>> expiringIns = new ArrayList<>();
        for (Insurance ins : allInsurance) {
            if (ins.getEndDate() != null
                    && ins.getEndDate().isAfter(now)
                    && ins.getEndDate().isBefore(thirtyDaysLater)) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("policyNumber", ins.getPolicyNumber());
                alert.put("vehicleNumber", ins.getVehicle().getVehicleNumber());
                alert.put("endDate", ins.getEndDate().toString());
                alert.put("daysLeft", java.time.temporal.ChronoUnit.DAYS.between(now, ins.getEndDate()));
                expiringIns.add(alert);
            }
        }
        stats.setExpiringInsurance(expiringIns);

        // Expiry alerts — driver license expiring within 30 days
        List<Map<String, Object>> expiringLic = new ArrayList<>();
        for (Driver driver : allDrivers) {
            if (driver.getLicenseExpiryDate() != null
                    && driver.getLicenseExpiryDate().isAfter(now)
                    && driver.getLicenseExpiryDate().isBefore(thirtyDaysLater)) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("driverName", driver.getDriverName());
                alert.put("licenseNumber", driver.getLicenseNumber());
                alert.put("expiryDate", driver.getLicenseExpiryDate().toString());
                alert.put("daysLeft", java.time.temporal.ChronoUnit.DAYS.between(now, driver.getLicenseExpiryDate()));
                expiringLic.add(alert);
            }
        }
        stats.setExpiringLicenses(expiringLic);

        // Chart data — vehicles by fuel type
        Map<String, Long> fuelTypeMap = allVehicles.stream()
                .collect(Collectors.groupingBy(Vehicle::getFuelType, Collectors.counting()));
        stats.setVehiclesByFuelType(fuelTypeMap);

        // Chart data — monthly maintenance cost (last 6 months)
        Map<String, Double> monthlyCost = new LinkedHashMap<>();
        for (int i = 5; i >= 0; i--) {
            LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            String monthKey = monthStart.getMonth().toString().substring(0, 3) + " " + monthStart.getYear();

            double cost = allMaintenance.stream()
                    .filter(m -> m.getServiceDate() != null
                            && !m.getServiceDate().isBefore(monthStart)
                            && !m.getServiceDate().isAfter(monthEnd))
                    .mapToDouble(MaintenanceRecord::getServiceCost)
                    .sum();

            monthlyCost.put(monthKey, cost);
        }
        stats.setMonthlyMaintenanceCost(monthlyCost);

        return stats;
    }

    // Driver dashboard — minimal stats
    public Map<String, Object> getDriverDashboard(User user) {
        Map<String, Object> data = new HashMap<>();

        // Find driver linked to user
        List<Driver> allDrivers = driverRepository.findAll();
        Driver myDriver = null;
        for (Driver d : allDrivers) {
            if (d.getUser() != null && d.getUser().getId().equals(user.getId())) {
                myDriver = d;
                break;
            }
        }

        if (myDriver != null) {
            data.put("driverId", myDriver.getDriverId());
            data.put("driverName", myDriver.getDriverName());
            data.put("availabilityStatus", myDriver.getAvailabilityStatus());
            data.put("licenseExpiryDate", myDriver.getLicenseExpiryDate());

            // Find assigned vehicle
            List<Vehicle> allVehicles = vehicleRepository.findAll();
            Vehicle myVehicle = null;
            for (Vehicle v : allVehicles) {
                if (v.getDriver() != null && v.getDriver().getDriverId().equals(myDriver.getDriverId())) {
                    myVehicle = v;
                    break;
                }
            }

            if (myVehicle != null) {
                Map<String, Object> vehicleData = new HashMap<>();
                vehicleData.put("vehicleId", myVehicle.getVehicleId());
                vehicleData.put("vehicleNumber", myVehicle.getVehicleNumber());
                vehicleData.put("vehicleType", myVehicle.getVehicleType());
                vehicleData.put("brand", myVehicle.getBrand());
                vehicleData.put("model", myVehicle.getModel());
                vehicleData.put("fuelType", myVehicle.getFuelType());
                vehicleData.put("status", myVehicle.getStatus());
                data.put("vehicle", vehicleData);
            } else {
                data.put("vehicle", null);
            }

            // My trips
            List<Trip> myTrips = tripRepository.findByDriverId(myDriver.getDriverId());
            data.put("totalTrips", myTrips.size());
            data.put("activeTrips", myTrips.stream()
                    .filter(t -> t.getStatus() == TripStatus.IN_PROGRESS).count());
            data.put("completedTrips", myTrips.stream()
                    .filter(t -> t.getStatus() == TripStatus.COMPLETED).count());

            // License expiry alert
            if (myDriver.getLicenseExpiryDate() != null) {
                LocalDate now = LocalDate.now();
                long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(now, myDriver.getLicenseExpiryDate());
                if (daysLeft <= 30 && daysLeft > 0) {
                    data.put("licenseAlert", "Your license expires in " + daysLeft + " days!");
                }
            }
        }

        return data;
    }
}