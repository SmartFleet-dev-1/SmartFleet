
import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators
} from '@angular/forms';
import { HttpService } from '../../../services/http.service';


@Component({
  selector: 'app-maintenance',
  templateUrl: './maintenance.component.html',
  styleUrls: ['./maintenance.component.scss']
})
export class MaintenanceComponent implements OnInit {

  maintenanceForm!: FormGroup;

  maintenanceRecords: any[] = [];
  vehicles: any[] = [];

  submitted: boolean = false;
  editing: boolean = false;
  editingId: number | null = null;

  successMessage: string = '';
  errorMessage: string = '';

  searchServiceType: string = '';
  filterDate: string = '';

  today: string = '';

  constructor(
    private fb: FormBuilder,
    private httpService: HttpService
  ) {}

  ngOnInit(): void {
    this.today = new Date().toISOString().split('T')[0];

    this.maintenanceForm = this.fb.group(
      {
        vehicleId: ['', Validators.required],
        serviceDate: ['', Validators.required],
        serviceType: ['', Validators.required],
        serviceCenter: ['', Validators.required],
        serviceCost: [0, [Validators.required, Validators.min(1)]],
        nextServiceDate: [''],
        remarks: ['']
      },
      {
        validators: this.serviceDateNotFutureValidator
      }
    );

    this.loadVehicles();
    this.loadMaintenanceRecords();
  }

  loadVehicles(): void {
    this.httpService.getAllVehicles().subscribe({
      next: (data: any[]) => {
        this.vehicles = data;
      },
      error: (error: any) => {
        this.errorMessage = this.extractErrorMessage(error, 'Failed to load vehicles.');
      }
    });
  }

  loadMaintenanceRecords(): void {
    this.httpService.getAllMaintenance().subscribe({
      next: (data: any[]) => {
        this.maintenanceRecords = data;
      },
      error: (error: any) => {
        this.errorMessage = this.extractErrorMessage(error, 'Failed to load maintenance records.');
      }
    });
  }

  saveMaintenance(): void {
    this.submitted = true;
    this.successMessage = '';
    this.errorMessage = '';

    if (this.maintenanceForm.invalid) {
      this.maintenanceForm.markAllAsTouched();
      return;
    }

    const vehicleId = Number(this.maintenanceForm.value.vehicleId);

    const maintenancePayload = {
      serviceDate: this.maintenanceForm.value.serviceDate,
      serviceType: this.maintenanceForm.value.serviceType,
      serviceCenter: this.maintenanceForm.value.serviceCenter,
      serviceCost: Number(this.maintenanceForm.value.serviceCost),
      nextServiceDate: this.maintenanceForm.value.nextServiceDate || null,
      remarks: this.maintenanceForm.value.remarks
    };

    if (this.editing && this.editingId !== null) {
      this.httpService.updateMaintenance(this.editingId, maintenancePayload, vehicleId).subscribe({
        next: () => {
          this.successMessage = 'Maintenance record updated successfully!';
          this.resetForm();
          this.loadMaintenanceRecords();
        },
        error: (error: any) => {
          this.errorMessage = this.extractErrorMessage(error, 'Failed to update maintenance record.');
        }
      });
    } else {
      this.httpService.addMaintenance(maintenancePayload, vehicleId).subscribe({
        next: () => {
          this.successMessage = 'Maintenance record added successfully!';
          this.resetForm();
          this.loadMaintenanceRecords();
        },
        error: (error: any) => {
          this.errorMessage = this.extractErrorMessage(error, 'Failed to add maintenance record.');
        }
      });
    }
  }

  editMaintenance(record: any): void {
    this.editing = true;
    this.editingId = record.maintenanceId;
    this.successMessage = '';
    this.errorMessage = '';

    this.maintenanceForm.patchValue({
      vehicleId: record.vehicle?.vehicleId || '',
      serviceDate: record.serviceDate,
      serviceType: record.serviceType,
      serviceCenter: record.serviceCenter,
      serviceCost: record.serviceCost,
      nextServiceDate: record.nextServiceDate || '',
      remarks: record.remarks || ''
    });

    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }

  deleteMaintenance(id: number): void {
    if (!id) {
      return;
    }

    const confirmDelete = confirm('Are you sure you want to delete this maintenance record?');

    if (!confirmDelete) {
      return;
    }

    this.successMessage = '';
    this.errorMessage = '';

    this.httpService.deleteMaintenance(id).subscribe({
      next: () => {
        this.successMessage = 'Maintenance record deleted successfully!';
        this.loadMaintenanceRecords();
      },
      error: (error: any) => {
        this.errorMessage = this.extractErrorMessage(error, 'Failed to delete maintenance record.');
      }
    });
  }

  searchByServiceType(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (!this.searchServiceType.trim()) {
      this.loadMaintenanceRecords();
      return;
    }

    this.httpService.searchMaintenanceByType(this.searchServiceType.trim()).subscribe({
      next: (data: any[]) => {
        this.maintenanceRecords = data;
      },
      error: (error: any) => {
        this.maintenanceRecords = [];
        this.errorMessage = this.extractErrorMessage(error, 'No maintenance records found.');
      }
    });
  }

  filterByServiceDate(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (!this.filterDate) {
      this.loadMaintenanceRecords();
      return;
    }

    this.httpService.filterMaintenanceByDate(this.filterDate).subscribe({
      next: (data: any[]) => {
        this.maintenanceRecords = data;
      },
      error: (error: any) => {
        this.maintenanceRecords = [];
        this.errorMessage = this.extractErrorMessage(error, 'No records found for selected service date.');
      }
    });
  }

  sortByCost(order: string): void {
    this.successMessage = '';
    this.errorMessage = '';

    this.httpService.sortMaintenanceByCost(order).subscribe({
      next: (data: any[]) => {
        this.maintenanceRecords = data;
      },
      error: (error: any) => {
        this.errorMessage = this.extractErrorMessage(error, 'Failed to sort maintenance records.');
      }
    });
  }

  resetFilters(): void {
    this.searchServiceType = '';
    this.filterDate = '';
    this.successMessage = '';
    this.errorMessage = '';
    this.loadMaintenanceRecords();
  }

  resetForm(): void {
    this.maintenanceForm.reset({
      vehicleId: '',
      serviceDate: '',
      serviceType: '',
      serviceCenter: '',
      serviceCost: 0,
      nextServiceDate: '',
      remarks: ''
    });

    this.submitted = false;
    this.editing = false;
    this.editingId = null;
  }

  cancelEdit(): void {
    this.resetForm();
    this.successMessage = '';
    this.errorMessage = '';
  }

  isInvalid(controlName: string): boolean {
    const control = this.maintenanceForm.get(controlName);

    return !!(
      control &&
      control.invalid &&
      (control.dirty || control.touched || this.submitted)
    );
  }

  hasServiceDateFutureError(): boolean {
    return !!(
      this.maintenanceForm.hasError('serviceDateFuture') &&
      (
        this.maintenanceForm.get('serviceDate')?.dirty ||
        this.maintenanceForm.get('serviceDate')?.touched ||
        this.submitted
      )
    );
  }

  serviceDateNotFutureValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const serviceDate = control.get('serviceDate')?.value;

    if (!serviceDate) {
      return null;
    }

    const selectedDate = new Date(serviceDate);
    const todayDate = new Date();

    selectedDate.setHours(0, 0, 0, 0);
    todayDate.setHours(0, 0, 0, 0);

    if (selectedDate > todayDate) {
      return { serviceDateFuture: true };
    }

    return null;
  }

  isUpcomingService(nextServiceDate: string): boolean {
    if (!nextServiceDate) {
      return false;
    }

    const todayDate = new Date();
    const targetDate = new Date(nextServiceDate);

    todayDate.setHours(0, 0, 0, 0);
    targetDate.setHours(0, 0, 0, 0);

    const diffTime = targetDate.getTime() - todayDate.getTime();
    const diffDays = diffTime / (1000 * 60 * 60 * 24);

    return diffDays >= 0 && diffDays <= 30;
  }

  getVehicleDisplay(vehicle: any): string {
    if (!vehicle) {
      return 'N/A';
    }

    const vehicleNumber = vehicle.vehicleNumber || '';
    const brand = vehicle.brand || '';
    const model = vehicle.model || '';

    return `${vehicleNumber} - ${brand} ${model}`.trim();
  }

  private extractErrorMessage(error: any, defaultMessage: string): string {
    console.error('Maintenance API error:', error);

    if (error?.error?.message) {
      return error.error.message;
    }

    if (error?.error?.error) {
      return error.error.error;
    }

    if (typeof error?.error === 'string') {
      return error.error;
    }

    if (error?.message) {
      return error.message;
    }

    return defaultMessage;
  }
}
