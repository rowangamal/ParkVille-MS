import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LandingPage from '../src/components/LandingPage';
import Login from '../src/components/Login';
import Signup from '../src/components/Signup';
import Map from './components/Map/Map';
import MapUser from './components/MapUser/MapUser';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/login" element={<Login />} />
        <Route path="/map" element={<Map />} />
        <Route path="/map-user" element={<MapUser />} />
      </Routes>
    </Router>
  );
};

export default App;