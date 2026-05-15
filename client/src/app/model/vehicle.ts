import { Driver } from './driver';
import { Insurance } from './insurance';
import { MaintenanceRecord } from './maintenance-record';

export interface Vehicle {
  vehicleId?: number;
  vehicleNumber: string;
  vehicleType: string;
  brand: string;
  model: string;
  manufacturingYear: number;
  fuelType: string;
  mileage: number;
  status: string;
  driver?: Driver;
  maintenanceRecords?: MaintenanceRecord[];
  insurance?: Insurance;
}