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

function App() {
  return ( 
    <BrowserRouter>
    {/* Your app components go here */}
    <Navbar />
    <div className="content">
      <Routes>
        {/*Auth pages*/}
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />

        <Route path="/home" element={<HomePage />} />
        <Route path="/categories" element={<CategoriesPage />} />

        <Route path="/menu" element={<MenuPage />} />
        <Route path="/menu/:id" element={<MenuDetailsPage />} />

        <Route path="/profile" element={<CustomerRoute element={ProfilePage} />} />
        <Route path="/update" element={<CustomerRoute element={UpdateProfilePage} />} />

        <Route path="/my-order-history" element={<CustomerRoute element={OrderHistoryPage} />} />

        <Route path="/cart" element={<CustomerRoute element={CartPage} />} />

      </Routes>
    </div>
    <Footer />
    </BrowserRouter>
  );
}

export default App;
