import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import SalonList from "./pages/SalonList";
import SalonDetails from "./pages/SalonDetails";
import EditSalon from "./pages/EditSalon";


function App() {
  return (
    <Router>
      <div className="infinite-loader"></div>

      <Routes>
        <Route path="/" element={<SalonList />} />
        <Route path="/salon/:id" element={<SalonDetails />} />
        <Route path="/edit/:id" element={<EditSalon />} />
      </Routes>
    </Router>
  );
}

export default App;