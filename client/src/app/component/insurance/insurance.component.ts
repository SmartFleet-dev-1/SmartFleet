import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpService } from '../../../services/http.service';

@Component
({
  selector: 'app-insurance',
  templateUrl: './insurance.component.html',
  styleUrls: ['./insurance.component.scss']
})
export class InsuranceComponent implements OnInit {

  insuranceForm!: FormGroup;

  insuranceRecords: any[] = [];
  vehicles: any[] = [];

  submitted: boolean = false;
  editing: boolean = false;
  editingId: number | null = null;

  successMessage: string = '';
  errorMessage: string = '';

  searchProviderName: string = '';
  filterCoverageType: string = '';

  constructor(
    private fb: FormBuilder,
    private httpService: HttpService
  ) {}

  ngOnInit(): void {
    this.insuranceForm = this.fb.group(
      {
        vehicleId: ['', Validators.required],
        providerName: ['', Validators.required],
        policyNumber: ['', Validators.required],
        startDate: ['', Validators.required],
        endDate: ['', Validators.required],
        premiumAmount: [0, [Validators.required, Validators.min(1)]],
        coverageType: ['Full', Validators.required]
      },
      {
        validators: this.endDateAfterStartDateValidator
      }
    );

    this.loadVehicles();
    this.loadInsuranceRecords();
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

  loadInsuranceRecords(): void {
    this.httpService.getAllInsurance().subscribe({
      next: (data: any[]) => {
        this.insuranceRecords = data;
      },
      error: (error: any) => {
        this.errorMessage = this.extractErrorMessage(error, 'Failed to load insurance records.');
      }
    });
  }

  saveInsurance(): void {
    this.submitted = true;
    this.successMessage = '';
    this.errorMessage = '';

    if (this.insuranceForm.invalid) {
      this.insuranceForm.markAllAsTouched();
      return;
    }

    const vehicleId = Number(this.insuranceForm.value.vehicleId);

    const insurancePayload = {
      providerName: this.insuranceForm.value.providerName,
      policyNumber: this.insuranceForm.value.policyNumber,
      startDate: this.insuranceForm.value.startDate,
      endDate: this.insuranceForm.value.endDate,
      premiumAmount: Number(this.insuranceForm.value.premiumAmount),
      coverageType: this.insuranceForm.value.coverageType
    };

    if (this.editing && this.editingId !== null) {
      this.httpService.updateInsurance(this.editingId, insurancePayload,vehicleId).subscribe({
        next: () => {
          this.successMessage = 'Insurance record updated successfully!';
          this.resetForm();
          this.loadInsuranceRecords();
        },
        error: (error: any) => {
          this.errorMessage = this.extractErrorMessage(error, 'Failed to update insurance record.');
        }
      });
    } else {
      this.httpService.addInsurance(insurancePayload,vehicleId).subscribe({
        next: () => {
          this.successMessage = 'Insurance record added successfully!';
          this.resetForm();
          this.loadInsuranceRecords();
        },
        error: (error: any) => {
          this.errorMessage = this.extractErrorMessage(error, 'Failed to add insurance record.');
        }
      });
    }
  }

  editInsurance(record: any): void {
    this.editing = true;
    this.editingId = record.insuranceId;
    this.successMessage = '';
    this.errorMessage = '';

    this.insuranceForm.patchValue({
      vehicleId: record.vehicle?.vehicleId || '',
      providerName: record.providerName,
      policyNumber: record.policyNumber,
      startDate: record.startDate,
      endDate: record.endDate,
      premiumAmount: record.premiumAmount,
      coverageType: record.coverageType
    });

    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }

  deleteInsurance(id: number): void {
    if (!id) {
      return;
    }

    const confirmDelete = confirm('Are you sure you want to delete this insurance record?');

    if (!confirmDelete) {
      return;
    }

    this.successMessage = '';
    this.errorMessage = '';

    this.httpService.deleteInsurance(id).subscribe({
      next: () => {
        this.successMessage = 'Insurance record deleted successfully!';
        this.loadInsuranceRecords();
      },
      error: (error: any) => {
        this.errorMessage = this.extractErrorMessage(error, 'Failed to delete insurance record.');
      }
    });
  }

  searchByProvider(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (!this.searchProviderName.trim()) {
      this.loadInsuranceRecords();
      return;
    }

    this.httpService.searchInsuranceByProvider(this.searchProviderName.trim()).subscribe({
      next: (data: any[]) => {
        this.insuranceRecords = data;
      },
      error: (error: any) => {
        this.insuranceRecords = [];
        this.errorMessage = this.extractErrorMessage(error, 'No insurance records found.');
      }
    });
  }

  filterByCoverageType(): void {
    this.successMessage = '';
    this.errorMessage = '';

    if (!this.filterCoverageType) {
      this.loadInsuranceRecords();
      return;
    }

    this.httpService.filterInsuranceByCoverage(this.filterCoverageType).subscribe({
      next: (data: any[]) => {
        this.insuranceRecords = data;
      },
      error: (error: any) => {
        this.insuranceRecords = [];
        this.errorMessage = this.extractErrorMessage(error, 'No insurance records found for selected coverage type.');
      }
    });
  }

  sortByPremium(order: string): void {
    this.successMessage = '';
    this.errorMessage = '';

    this.httpService.sortInsuranceByPremium(order).subscribe({
      next: (data: any[]) => {
        this.insuranceRecords = data;
      },
      error: (error: any) => {
        this.errorMessage = this.extractErrorMessage(error, 'Failed to sort insurance records.');
      }
    });
  }

  resetFilters(): void {
    this.searchProviderName = '';
    this.filterCoverageType = '';
    this.successMessage = '';
    this.errorMessage = '';
    this.loadInsuranceRecords();
  }

  resetForm(): void {
    this.insuranceForm.reset({
      vehicleId: '',
      providerName: '',
      policyNumber: '',
      startDate: '',
      endDate: '',
      premiumAmount: 0,
      coverageType: 'Full'
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
    const control = this.insuranceForm.get(controlName);

    return !!(
      control &&
      control.invalid &&
      (control.dirty || control.touched || this.submitted)
    );
  }

  hasDateRangeError(): boolean {
    return !!(
      this.insuranceForm.hasError('endDateBeforeStartDate') &&
      (
        this.insuranceForm.get('startDate')?.dirty ||
        this.insuranceForm.get('startDate')?.touched ||
        this.insuranceForm.get('endDate')?.dirty ||
        this.insuranceForm.get('endDate')?.touched ||
        this.submitted
      )
    );
  }

  endDateAfterStartDateValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const startDate = control.get('startDate')?.value;
    const endDate = control.get('endDate')?.value;

    if (!startDate || !endDate) {
      return null;
    }

    const start = new Date(startDate);
    const end = new Date(endDate);

    if (end <= start) {
      return { endDateBeforeStartDate: true };
    }

    return null;
  }

  isExpiringSoon(endDate: string): boolean {
    if (!endDate) {
      return false;
    }

    const today = new Date();
    const expiryDate = new Date(endDate);

    today.setHours(0, 0, 0, 0);
    expiryDate.setHours(0, 0, 0, 0);

    const differenceInTime = expiryDate.getTime() - today.getTime();
    const differenceInDays = differenceInTime / (1000 * 60 * 60 * 24);

    return differenceInDays >= 0 && differenceInDays <= 30;
  }

  getVehicleDisplay(vehicle: any): string {
    if (!vehicle) {
      return 'N/A';
    }

    const vehicleNumber = vehicle.vehicleNumber || '';
    const brand = vehicle.brand || '';
    const model = vehicle.model || '';

    return `${vehicleNumber} - ${brand} ${model}`;
  }

  private extractErrorMessage(error: any, defaultMessage: string): string {
    console.error('Insurance API error:', error);

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
