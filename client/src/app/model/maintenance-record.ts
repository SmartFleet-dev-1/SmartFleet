import { Vehicle } from './vehicle';

export interface MaintenanceRecord {
  maintenanceId?: number;
  serviceDate: string;
  serviceType: string;
  serviceCenter: string;
  serviceCost: number;
  nextServiceDate?: string;
  remarks?: string;
  vehicle?: Vehicle;
}