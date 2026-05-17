package com.edutech.dto;

import java.util.List;
import java.util.Map;

public class DashboardStatsDTO {

    // Counts
    private long totalVehicles;
    private long activeVehicles;
    private long totalDrivers;
    private long activeDrivers;
    private long assignedDrivers;
    private long totalTrips;
    private long completedTrips;
    private long activeTrips;
    private long pendingApprovals;

    // Expense
    private double totalMaintenanceCost;
    private double totalInsuranceCost;

    // Expiry alerts
    private List<Map<String, Object>> expiringInsurance;
    private List<Map<String, Object>> expiringLicenses;

    // Chart data
    private Map<String, Long> vehiclesByFuelType;
    private Map<String, Double> monthlyMaintenanceCost;

    public DashboardStatsDTO() {}

    public long getTotalVehicles() {
        return totalVehicles;
    }

    public void setTotalVehicles(long totalVehicles) {
        this.totalVehicles = totalVehicles;
    }

    public long getActiveVehicles() {
        return activeVehicles;
    }

    public void setActiveVehicles(long activeVehicles) {
        this.activeVehicles = activeVehicles;
    }

    public long getTotalDrivers() {
        return totalDrivers;
    }

    public void setTotalDrivers(long totalDrivers) {
        this.totalDrivers = totalDrivers;
    }

    public long getActiveDrivers() {
        return activeDrivers;
    }

    public void setActiveDrivers(long activeDrivers) {
        this.activeDrivers = activeDrivers;
    }

    public long getAssignedDrivers() {
        return assignedDrivers;
    }

    public void setAssignedDrivers(long assignedDrivers) {
        this.assignedDrivers = assignedDrivers;
    }

    public long getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(long totalTrips) {
        this.totalTrips = totalTrips;
    }

    public long getCompletedTrips() {
        return completedTrips;
    }

    public void setCompletedTrips(long completedTrips) {
        this.completedTrips = completedTrips;
    }

    public long getActiveTrips() {
        return activeTrips;
    }

    public void setActiveTrips(long activeTrips) {
        this.activeTrips = activeTrips;
    }

    public long getPendingApprovals() {
        return pendingApprovals;
    }

    public void setPendingApprovals(long pendingApprovals) {
        this.pendingApprovals = pendingApprovals;
    }

    public double getTotalMaintenanceCost() {
        return totalMaintenanceCost;
    }

    public void setTotalMaintenanceCost(double totalMaintenanceCost) {
        this.totalMaintenanceCost = totalMaintenanceCost;
    }

    public double getTotalInsuranceCost() {
        return totalInsuranceCost;
    }

    public void setTotalInsuranceCost(double totalInsuranceCost) {
        this.totalInsuranceCost = totalInsuranceCost;
    }

    public List<Map<String, Object>> getExpiringInsurance() {
        return expiringInsurance;
    }

    public void setExpiringInsurance(List<Map<String, Object>> expiringInsurance) {
        this.expiringInsurance = expiringInsurance;
    }

    public List<Map<String, Object>> getExpiringLicenses() {
        return expiringLicenses;
    }

    public void setExpiringLicenses(List<Map<String, Object>> expiringLicenses) {
        this.expiringLicenses = expiringLicenses;
    }

    public Map<String, Long> getVehiclesByFuelType() {
        return vehiclesByFuelType;
    }

    public void setVehiclesByFuelType(Map<String, Long> vehiclesByFuelType) {
        this.vehiclesByFuelType = vehiclesByFuelType;
    }

    public Map<String, Double> getMonthlyMaintenanceCost() {
        return monthlyMaintenanceCost;
    }

    public void setMonthlyMaintenanceCost(Map<String, Double> monthlyMaintenanceCost) {
        this.monthlyMaintenanceCost = monthlyMaintenanceCost;
    }
}