export interface LoginResponse {
  token?: string;
  jwtToken?: string;
  role?: string;
  userId?: number;
  username?: string;
}