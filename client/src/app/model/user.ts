export enum Role {
  ADMIN = 'ADMIN'
}

export interface User {
  id?: number;
  username: string;
  password?: string;
  email: string;
  contactNumber?: number;
  role: string;
}