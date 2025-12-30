import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/common/Navbar";
import Footer from "./components/common/Footer";
import RegisterPage from "./components/auth/RegisterPage";


function App() {
  return (
    <BrowserRouter>
    {/* Your app components go here */}
    <Navbar />
    <div className="content">
      <Routes>
        <Route path="/register" element={<RegisterPage />} />
      </Routes>
    </div>
    <Footer />
    </BrowserRouter>
  );
}

export default App;
