import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LandingPage from '../src/components/LandingPage';
import Login from '../src/components/Login';
import Signup from '../src/components/Signup';
import AdminDashboard from './components/AdminDashboard';
import ManagerDashboard from './components/ManagerDashboard';
import Map from './components/Map/Map';
import DriverDashboard from './components/DriverDashboard';
import DriverReservations from './components/DriverReservations';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/login" element={<Login />} />
        <Route path="/manager" element={<ManagerDashboard />} />
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/map" element={<Map />} />
        <Route path="/driver" element={<DriverDashboard />} />
        <Route path="/driver-reservations" element={<DriverReservations />} />
      </Routes>
    </Router>
  );
};

export default App;