import { Component, OnInit } from '@angular/core';
import { HttpService } from '../../services/http.service';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  
totalVehicles: number = 0;
  activeVehicles: number = 0;
  inactiveVehicles: number = 0;
  underServiceVehicles: number = 0;

  totalDrivers: number = 0;
  availableDrivers: number = 0;
  assignedDrivers: number = 0;

  totalMaintenanceRecords: number = 0;
  upcomingServices: number = 0;

  totalInsuranceRecords: number = 0;
  insuranceExpiryAlerts: number = 0;

  errorMessage: string = '';

  constructor(private httpService: HttpService) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loadVehicleSummary();
    this.loadDriverSummary();
    this.loadMaintenanceSummary();
    this.loadInsuranceSummary();
  }

  loadVehicleSummary(): void {
    this.httpService.getAllVehicles().subscribe({
      next: (vehicles: any[]) => {
        this.totalVehicles = vehicles.length;

        this.activeVehicles = vehicles.filter(
          vehicle => vehicle.status === 'Active'
        ).length;

        this.inactiveVehicles = vehicles.filter(
          vehicle => vehicle.status === 'Inactive'
        ).length;

        this.underServiceVehicles = vehicles.filter(
          vehicle => vehicle.status === 'Under Service'
        ).length;
      },
      error: () => {
        this.errorMessage = 'Failed to load vehicle summary';
      }
    });
  }

  loadDriverSummary(): void {
    this.httpService.getAllDrivers().subscribe({
      next: (drivers: any[]) => {
        this.totalDrivers = drivers.length;

        this.availableDrivers = drivers.filter(
          driver => driver.availabilityStatus === 'Available'
        ).length;

        this.assignedDrivers = drivers.filter(
          driver => driver.availabilityStatus === 'Assigned'
        ).length;
      },
      error: () => {
        this.errorMessage = 'Failed to load driver summary';
      }
    });
  }

  loadMaintenanceSummary(): void {
    this.httpService.getAllMaintenance().subscribe({
      next: (records: any[]) => {
        this.totalMaintenanceRecords = records.length;

        this.upcomingServices = records.filter(record =>
          this.isWithinNext30Days(record.nextServiceDate)
        ).length;
      },
      error: () => {
        this.errorMessage = 'Failed to load maintenance summary';
      }
    });
  }

  loadInsuranceSummary(): void {
    this.httpService.getAllInsurance().subscribe({
      next: (records: any[]) => {
        this.totalInsuranceRecords = records.length;

        this.insuranceExpiryAlerts = records.filter(record =>
          this.isWithinNext30Days(record.endDate)
        ).length;
      },
      error: () => {
        this.errorMessage = 'Failed to load insurance summary';
      }
    });
  }

  isWithinNext30Days(dateValue: string | undefined | null): boolean {
    if (!dateValue) {
      return false;
    }

    const today = new Date();
    const targetDate = new Date(dateValue);

    today.setHours(0, 0, 0, 0);
    targetDate.setHours(0, 0, 0, 0);

    const differenceInTime = targetDate.getTime() - today.getTime();
    const differenceInDays = differenceInTime / (1000 * 60 * 60 * 24);

    return differenceInDays >= 0 && differenceInDays <= 30;
  }

}
