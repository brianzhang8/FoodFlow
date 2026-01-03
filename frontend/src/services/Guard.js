import { Navigate, Outlet, useLocation } from "react-router-dom";
import ApiService from "./ApiService";

export const CustomerRoute = () => {
  const location = useLocation();

  return ApiService.isCustomer() ? (
    <Outlet />
  ) : (
    <Navigate to="/login" replace state={{ from: location }} />
  );
};


export const AdminRoute = () => {
  const location = useLocation();

  return ApiService.isAdmin() ? (
    <Outlet />
  ) : (
    <Navigate to="/login" replace state={{ from: location }} />
  );
};