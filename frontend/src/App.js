import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/common/Navbar";
import Footer from "./components/common/Footer";


function App() {
  return (
    <BrowserRouter>
    {/* Your app components go here */}
    <Navbar />
    <div className="content">
      <Routes>
        {/* <Route path="/home" element={<Home />} /> */}
      </Routes>
    </div>
    <Footer />
    </BrowserRouter>
  );
}

export default App;
