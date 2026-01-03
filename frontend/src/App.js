import { Navigate } from "react-router";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/common/Navbar";
import Footer from "./components/common/Footer";
import RegisterPage from "./components/auth/RegisterPage";
import LoginPage from "./components/auth/LoginPage";
import HomePage from "./components/home_menu/HomePage";
import CategoriesPage from "./components/home_menu/CategoriesPage";
import MenuPage from "./components/home_menu/MenuPage";
import MenuDetailsPage from "./components/home_menu/MenuDetailsPage";
import ProfilePage from "./components/profile_cart/ProfilePage";
import UpdateProfilePage from "./components/profile_cart/UpdateProfilePage";
import { AdminRoute, CustomerRoute } from "./services/Guard";
import OrderHistoryPage from "./components/profile_cart/OrderHistoryPage";
import CartPage from "./components/profile_cart/CartPage";
import ProcessPaymentPage from "./components/payment/ProcessPaymentPage";
import AdminLayout from "./components/admin/navbar/AdminLayout";
import AdminCategoriesPage from "./components/admin/AdminCategoriesPage";
import AdminCategoryFormPage from "./components/admin/AdminCategoryFormPage";
import AdminMenuPage from "./components/admin/AdminMenuPage";
import AdminMenuFormPage from "./components/admin/AdminMenuFormPage";
import AdminOrdersPage from "./components/admin/AdminOrdersPage";
import AdminOrderDetailPage from "./components/admin/AdminOrderDetailPage";
import AdminPaymentsPage from "./components/admin/AdminPaymentsPage";
import AdminPaymentDetailPage from "./components/admin/AdminPaymentDetailPage";
import AdminDashboardPage from "./components/admin/AdminDashboarPage";
import AdminUserRegistration from "./components/auth/AdminUserRegistration";
import LeaveReviewPage from "./components/profile_cart/LeaveReviewPage";


function App() {
  return ( 
    <BrowserRouter>
    <Navbar />
    <div className="content">
      <Routes>
        {/* Auth */}
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />

        {/* Public */}
        <Route path="/home" element={<HomePage />} />
        <Route path="/categories" element={<CategoriesPage />} />
        <Route path="/menu" element={<MenuPage />} />
        <Route path="/menu/:id" element={<MenuDetailsPage />} />

        {/* Customer */}
        <Route element={<CustomerRoute />}>
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/update" element={<UpdateProfilePage />} />
          <Route path="/my-order-history" element={<OrderHistoryPage />} />
          <Route path="/leave-review" element={<LeaveReviewPage />} />
          <Route path="/orders" element={<OrderHistoryPage />} />
          <Route path="/cart" element={<CartPage />} />
          <Route path="/pay" element={<ProcessPaymentPage />} />
        </Route>

        {/* Admin */}
        <Route element={<AdminRoute />}>
          <Route path="/admin" element={<AdminLayout />}>
            <Route path="categories" element={<AdminCategoriesPage />} />
            <Route path="categories/new" element={<AdminCategoryFormPage />} />
            <Route path="categories/edit/:id" element={<AdminCategoryFormPage />} />

            <Route path="menu-items" element={<AdminMenuPage />} />
            <Route path="menu-items/new" element={<AdminMenuFormPage />} />
            <Route path="menu-items/edit/:id" element={<AdminMenuFormPage />} />

            <Route path="orders" element={<AdminOrdersPage />} />
            <Route path="orders/:id" element={<AdminOrderDetailPage />} />


            <Route path="payments" element={<AdminPaymentsPage />} />
            <Route path="payments/:id" element={<AdminPaymentDetailPage />} />

            <Route index element={<AdminDashboardPage />} />

            <Route path="register" element={<AdminUserRegistration />} />
          </Route>
        </Route>
        {/* Redirect unknown routes to home */}
        <Route path="*" element={<Navigate to={"/home"} />} />
      </Routes>

    </div>
    <Footer />
    </BrowserRouter>
  );
}
export default App;
