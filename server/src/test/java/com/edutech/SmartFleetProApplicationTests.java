package com.edutech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.edutech.dto.*;
import com.edutech.entity.*;
import com.edutech.exception.*;
import com.edutech.repository.*;
import com.edutech.service.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SmartFleetProApplicationTests {

    @Autowired private VehicleService vehicleService;
    @Autowired private DriverService driverService;
    @Autowired private MaintenanceRecordService maintenanceRecordService;
    @Autowired private InsuranceService insuranceService;
    @Autowired private UserService userService;

    @Autowired private VehicleRepository vehicleRepository;
    @Autowired private DriverRepository driverRepository;
    @Autowired private MaintenanceRecordRepository maintenanceRecordRepository;
    @Autowired private InsuranceRepository insuranceRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private int counter = 0;

    @BeforeEach
    void setUp() {
        insuranceRepository.deleteAll();
        maintenanceRecordRepository.deleteAll();
        vehicleRepository.deleteAll();
        driverRepository.deleteAll();
        userRepository.deleteAll();
        counter = 0;
    }

    // ─── Vehicle Tests (15) ───────────────────────────────────────────────────

    @Test
    void test01_addVehicle_success() {
        VehicleDTO dto = vehicleService.addVehicle(makeVehicle("KA01AB1001"));
        assertNotNull(dto.getVehicleId());
        assertEquals("KA01AB1001", dto.getVehicleNumber());
    }

    @Test
    void test02_addVehicle_duplicateThrows() {
        vehicleService.addVehicle(makeVehicle("KA01AB1002"));
        assertThrows(DuplicateResourceException.class, () -> vehicleService.addVehicle(makeVehicle("KA01AB1002")));
    }

    @Test
    void test03_getAllVehicles_returnsList() {
        vehicleService.addVehicle(makeVehicle("KA01AB1003"));
        vehicleService.addVehicle(makeVehicle("KA01AB1004"));
        assertEquals(2, vehicleService.getAllVehicles().size());
    }

    @Test
    void test04_getVehicleById_found() {
        VehicleDTO saved = vehicleService.addVehicle(makeVehicle("KA01AB1005"));
        VehicleDTO found = vehicleService.getVehicleById(saved.getVehicleId());
        assertEquals("KA01AB1005", found.getVehicleNumber());
    }

    @Test
    void test05_getVehicleById_notFound() {
        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getVehicleById(9999L));
    }

    @Test
    void test06_updateVehicle_success() {
        VehicleDTO saved = vehicleService.addVehicle(makeVehicle("KA01AB1006"));
        Vehicle updated = makeVehicle("KA01AB1006-U");
        updated.setStatus("Under Service");
        VehicleDTO result = vehicleService.updateVehicle(saved.getVehicleId(), updated);
        assertEquals("Under Service", result.getStatus());
    }

    @Test
    void test07_deleteVehicle_success() {
        VehicleDTO saved = vehicleService.addVehicle(makeVehicle("KA01AB1007"));
        vehicleService.deleteVehicle(saved.getVehicleId());
        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getVehicleById(saved.getVehicleId()));
    }

    @Test
    void test08_deleteVehicle_notFound() {
        assertThrows(ResourceNotFoundException.class, () -> vehicleService.deleteVehicle(9999L));
    }

    @Test
    void test09_searchVehicleByNumber() {
        vehicleService.addVehicle(makeVehicle("MH02CD5678"));
        VehicleDTO result = vehicleService.searchByVehicleNumber("MH02CD5678");
        assertEquals("MH02CD5678", result.getVehicleNumber());
    }

    @Test
    void test10_searchVehicleByBrand() {
        vehicleService.addVehicle(makeVehicle("KA01AB1009"));
        List<VehicleDTO> result = vehicleService.searchByBrand("Toyota");
        assertFalse(result.isEmpty());
    }

    @Test
    void test11_filterVehicleByStatus() {
        vehicleService.addVehicle(makeVehicle("KA01AB1010"));
        List<VehicleDTO> result = vehicleService.filterByStatus("Active");
        assertFalse(result.isEmpty());
    }

    @Test
    void test12_sortVehicleByYearAsc() {
        vehicleService.addVehicle(makeVehicle("KA01AB1011"));
        List<VehicleDTO> result = vehicleService.sortByManufacturingYear("asc");
        assertFalse(result.isEmpty());
    }

    @Test
    void test13_sortVehicleByYearDesc() {
        vehicleService.addVehicle(makeVehicle("KA01AB1012"));
        List<VehicleDTO> result = vehicleService.sortByManufacturingYear("desc");
        assertFalse(result.isEmpty());
    }

    @Test
    void test14_sortVehicleByMileageAsc() {
        vehicleService.addVehicle(makeVehicle("KA01AB1013"));
        List<VehicleDTO> result = vehicleService.sortByMileage("asc");
        assertFalse(result.isEmpty());
    }

    @Test
    void test15_assignDriverToVehicle() {
        VehicleDTO vehicle = vehicleService.addVehicle(makeVehicle("KA01AB1014"));
        DriverDTO driver = driverService.addDriver(makeDriver("DL-ASSIGN-01"));
        VehicleDTO result = vehicleService.assignDriver(vehicle.getVehicleId(), driver.getDriverId());
        assertEquals(driver.getDriverId(), result.getDriverId());
    }

    // ─── Driver Tests (15) ────────────────────────────────────────────────────

    @Test
    void test16_addDriver_success() {
        DriverDTO dto = driverService.addDriver(makeDriver("DL001"));
        assertNotNull(dto.getDriverId());
        assertEquals("DL001", dto.getLicenseNumber());
    }

    @Test
    void test17_addDriver_duplicateLicenseThrows() {
        driverService.addDriver(makeDriver("DL002"));
        assertThrows(DuplicateResourceException.class, () -> driverService.addDriver(makeDriver("DL002")));
    }

    @Test
    void test18_getAllDrivers_returnsList() {
        driverService.addDriver(makeDriver("DL003"));
        driverService.addDriver(makeDriver("DL004"));
        assertEquals(2, driverService.getAllDrivers().size());
    }

    @Test
    void test19_getDriverById_found() {
        DriverDTO saved = driverService.addDriver(makeDriver("DL005"));
        DriverDTO found = driverService.getDriverById(saved.getDriverId());
        assertEquals("DL005", found.getLicenseNumber());
    }

    @Test
    void test20_getDriverById_notFound() {
        assertThrows(ResourceNotFoundException.class, () -> driverService.getDriverById(9999L));
    }

    @Test
    void test21_updateDriver_success() {
        DriverDTO saved = driverService.addDriver(makeDriver("DL006"));
        Driver updated = makeDriver("DL006");
        updated.setDriverName("Jane Smith");
        DriverDTO result = driverService.updateDriver(saved.getDriverId(), updated);
        assertEquals("Jane Smith", result.getDriverName());
    }

    @Test
    void test22_deleteDriver_success() {
        DriverDTO saved = driverService.addDriver(makeDriver("DL007"));
        driverService.deleteDriver(saved.getDriverId());
        assertThrows(ResourceNotFoundException.class, () -> driverService.getDriverById(saved.getDriverId()));
    }

    @Test
    void test23_deleteDriver_notFound() {
        assertThrows(ResourceNotFoundException.class, () -> driverService.deleteDriver(9999L));
    }

    @Test
    void test24_searchDriverByName() {
        driverService.addDriver(makeDriver("DL008"));
        List<DriverDTO> result = driverService.searchByName("John");
        assertFalse(result.isEmpty());
    }

    @Test
    void test25_filterDriverByAvailability() {
        driverService.addDriver(makeDriver("DL009"));
        List<DriverDTO> result = driverService.filterByAvailability("Available");
        assertFalse(result.isEmpty());
    }

    @Test
    void test26_sortDriverByExperienceAsc() {
        driverService.addDriver(makeDriver("DL010"));
        List<DriverDTO> result = driverService.sortByExperience("asc");
        assertFalse(result.isEmpty());
    }

    @Test
    void test27_sortDriverByExperienceDesc() {
        driverService.addDriver(makeDriver("DL011"));
        List<DriverDTO> result = driverService.sortByExperience("desc");
        assertFalse(result.isEmpty());
    }

    @Test
    void test28_addDriver_verifyPhoneNumber() {
        DriverDTO dto = driverService.addDriver(makeDriver("DL012"));
        assertEquals("9876543210", dto.getPhoneNumber());
    }

    @Test
    void test29_addDriver_verifyExperience() {
        DriverDTO dto = driverService.addDriver(makeDriver("DL013"));
        assertEquals(5, dto.getExperienceYears());
    }

    @Test
    void test30_addDriver_verifyAvailabilityStatus() {
        DriverDTO dto = driverService.addDriver(makeDriver("DL014"));
        assertEquals("Available", dto.getAvailabilityStatus());
    }

    // ─── Maintenance Tests (15) ───────────────────────────────────────────────

    @Test
    void test31_addMaintenanceRecord_success() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("MV001"));
        MaintenanceRecordDTO dto = maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        assertNotNull(dto.getMaintenanceId());
        assertEquals("Oil Change", dto.getServiceType());
    }

    @Test
    void test32_addMaintenance_vehicleNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> maintenanceRecordService.addRecord(makeMaintenance(), 9999L));
    }

    @Test
    void test33_getAllMaintenanceRecords() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("MV002"));
        maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        assertEquals(2, maintenanceRecordService.getAllRecords().size());
    }

    @Test
    void test34_getMaintenanceById_found() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("MV003"));
        MaintenanceRecordDTO saved = maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        MaintenanceRecordDTO found = maintenanceRecordService.getRecordById(saved.getMaintenanceId());
        assertNotNull(found);
    }

    @Test
    void test35_getMaintenanceById_notFound() {
        assertThrows(ResourceNotFoundException.class, () -> maintenanceRecordService.getRecordById(9999L));
    }

    @Test
    void test36_updateMaintenanceRecord() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("MV004"));
        MaintenanceRecordDTO saved = maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        MaintenanceRecord updated = makeMaintenance();
        updated.setServiceType("Engine Repair");
        MaintenanceRecordDTO result = maintenanceRecordService.updateRecord(saved.getMaintenanceId(), updated, v.getVehicleId());
        assertEquals("Engine Repair", result.getServiceType());
    }

    @Test
    void test37_deleteMaintenanceRecord() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("MV005"));
        MaintenanceRecordDTO saved = maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        maintenanceRecordService.deleteRecord(saved.getMaintenanceId());
        assertThrows(ResourceNotFoundException.class, () -> maintenanceRecordService.getRecordById(saved.getMaintenanceId()));
    }

    @Test
    void test38_deleteMaintenanceRecord_notFound() {
        assertThrows(ResourceNotFoundException.class, () -> maintenanceRecordService.deleteRecord(9999L));
    }

    @Test
    void test39_searchMaintenanceByServiceType() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("MV006"));
        maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        List<MaintenanceRecordDTO> result = maintenanceRecordService.searchByServiceType("Oil");
        assertFalse(result.isEmpty());
    }

    @Test
    void test40_filterMaintenanceByDate() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("MV007"));
        maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        List<MaintenanceRecordDTO> result = maintenanceRecordService.filterByServiceDate(LocalDate.now().minusDays(10));
        assertFalse(result.isEmpty());
    }

    @Test
    void test41_sortMaintenanceByCostAsc() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("MV008"));
        maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        List<MaintenanceRecordDTO> result = maintenanceRecordService.sortByServiceCost("asc");
        assertFalse(result.isEmpty());
    }

    @Test
    void test42_sortMaintenanceByCostDesc() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("MV009"));
        maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        List<MaintenanceRecordDTO> result = maintenanceRecordService.sortByServiceCost("desc");
        assertFalse(result.isEmpty());
    }

    @Test
    void test43_getMaintenanceByVehicle() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("MV010"));
        maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        List<MaintenanceRecordDTO> result = maintenanceRecordService.getRecordsByVehicle(v.getVehicleId());
        assertFalse(result.isEmpty());
    }

    @Test
    void test44_maintenanceRecord_verifyServiceCenter() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("MV011"));
        MaintenanceRecordDTO dto = maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        assertEquals("AutoCare Center", dto.getServiceCenter());
    }

    @Test
    void test45_maintenanceRecord_verifyServiceCost() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("MV012"));
        MaintenanceRecordDTO dto = maintenanceRecordService.addRecord(makeMaintenance(), v.getVehicleId());
        assertEquals(2500.00, dto.getServiceCost());
    }

    // ─── Insurance Tests (15) ─────────────────────────────────────────────────

    @Test
    void test46_addInsurance_success() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("IV001"));
        InsuranceDTO dto = insuranceService.addInsurance(makeInsurance("POL001"), v.getVehicleId());
        assertNotNull(dto.getInsuranceId());
        assertEquals("POL001", dto.getPolicyNumber());
    }

    @Test
    void test47_addInsurance_duplicatePolicyThrows() {
        VehicleDTO v1 = vehicleService.addVehicle(makeVehicle("IV002"));
        VehicleDTO v2 = vehicleService.addVehicle(makeVehicle("IV003"));
        insuranceService.addInsurance(makeInsurance("POL002"), v1.getVehicleId());
        assertThrows(DuplicateResourceException.class, () -> insuranceService.addInsurance(makeInsurance("POL002"), v2.getVehicleId()));
    }

    @Test
    void test48_addInsurance_vehicleNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> insuranceService.addInsurance(makeInsurance("POL-X"), 9999L));
    }

    @Test
    void test49_getAllInsurance() {
        VehicleDTO v1 = vehicleService.addVehicle(makeVehicle("IV004"));
        VehicleDTO v2 = vehicleService.addVehicle(makeVehicle("IV005"));
        insuranceService.addInsurance(makeInsurance("POL003"), v1.getVehicleId());
        insuranceService.addInsurance(makeInsurance("POL004"), v2.getVehicleId());
        assertEquals(2, insuranceService.getAllInsurance().size());
    }

    @Test
    void test50_getInsuranceById_found() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("IV006"));
        InsuranceDTO saved = insuranceService.addInsurance(makeInsurance("POL005"), v.getVehicleId());
        InsuranceDTO found = insuranceService.getInsuranceById(saved.getInsuranceId());
        assertNotNull(found);
    }

    @Test
    void test51_getInsuranceById_notFound() {
        assertThrows(ResourceNotFoundException.class, () -> insuranceService.getInsuranceById(9999L));
    }

    @Test
    void test52_updateInsurance() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("IV007"));
        InsuranceDTO saved = insuranceService.addInsurance(makeInsurance("POL006"), v.getVehicleId());
        Insurance updated = makeInsurance("POL006");
        updated.setProviderName("New Insurer");
        InsuranceDTO result = insuranceService.updateInsurance(saved.getInsuranceId(), updated, v.getVehicleId());
        assertEquals("New Insurer", result.getProviderName());
    }

    @Test
    void test53_deleteInsurance_success() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("IV008"));
        InsuranceDTO saved = insuranceService.addInsurance(makeInsurance("POL007"), v.getVehicleId());
        insuranceService.deleteInsurance(saved.getInsuranceId());
        assertThrows(ResourceNotFoundException.class, () -> insuranceService.getInsuranceById(saved.getInsuranceId()));
    }

    @Test
    void test54_deleteInsurance_notFound() {
        assertThrows(ResourceNotFoundException.class, () -> insuranceService.deleteInsurance(9999L));
    }

    @Test
    void test55_searchInsuranceByProvider() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("IV009"));
        insuranceService.addInsurance(makeInsurance("POL008"), v.getVehicleId());
        List<InsuranceDTO> result = insuranceService.searchByProviderName("HDFC");
        assertFalse(result.isEmpty());
    }

    @Test
    void test56_filterInsuranceByCoverage() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("IV010"));
        insuranceService.addInsurance(makeInsurance("POL009"), v.getVehicleId());
        List<InsuranceDTO> result = insuranceService.filterByCoverageType("Full");
        assertFalse(result.isEmpty());
    }

    @Test
    void test57_sortInsuranceByPremiumAsc() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("IV011"));
        insuranceService.addInsurance(makeInsurance("POL010"), v.getVehicleId());
        List<InsuranceDTO> result = insuranceService.sortByPremiumAmount("asc");
        assertFalse(result.isEmpty());
    }

    @Test
    void test58_sortInsuranceByPremiumDesc() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("IV012"));
        insuranceService.addInsurance(makeInsurance("POL011"), v.getVehicleId());
        List<InsuranceDTO> result = insuranceService.sortByPremiumAmount("desc");
        assertFalse(result.isEmpty());
    }

    @Test
    void test59_insurance_verifyPremiumAmount() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("IV013"));
        InsuranceDTO dto = insuranceService.addInsurance(makeInsurance("POL012"), v.getVehicleId());
        assertEquals(12000.00, dto.getPremiumAmount());
    }

    @Test
    void test60_insurance_verifyVehicleNumber() {
        VehicleDTO v = vehicleService.addVehicle(makeVehicle("IV014"));
        InsuranceDTO dto = insuranceService.addInsurance(makeInsurance("POL013"), v.getVehicleId());
        assertEquals("IV014", dto.getVehicleNumber());
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private Vehicle makeVehicle(String vehicleNumber) {
        Vehicle v = new Vehicle();
        v.setVehicleNumber(vehicleNumber);
        v.setVehicleType("Truck");
        v.setBrand("Toyota");
        v.setModel("Hilux");
        v.setManufacturingYear(2020);
        v.setFuelType("Diesel");
        v.setMileage(15000);
        v.setStatus("Active");
        return v;
    }

    private Driver makeDriver(String licenseNumber) {
        Driver d = new Driver();
        d.setDriverName("John Doe");
        d.setLicenseNumber(licenseNumber);
        d.setPhoneNumber("9876543210");
        d.setExperienceYears(5);
        d.setAddress("123 Main Street, Chennai");
        d.setAvailabilityStatus("Available");
        return d;
    }

    private MaintenanceRecord makeMaintenance() {
        MaintenanceRecord m = new MaintenanceRecord();
        m.setServiceDate(LocalDate.now().minusDays(10));
        m.setServiceType("Oil Change");
        m.setServiceCenter("AutoCare Center");
        m.setServiceCost(2500.00);
        m.setNextServiceDate(LocalDate.now().plusMonths(3));
        m.setRemarks("Routine service");
        return m;
    }

    private Insurance makeInsurance(String policyNumber) {
        Insurance i = new Insurance();
        i.setProviderName("HDFC Ergo");
        i.setPolicyNumber(policyNumber);
        i.setStartDate(LocalDate.now().minusMonths(1));
        i.setEndDate(LocalDate.now().plusYears(1));
        i.setPremiumAmount(12000.00);
        i.setCoverageType("Full");
        return i;
    }
}
