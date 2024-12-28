import React from 'react';
import { useNavigate } from "react-router-dom";

const DriverDashboard = () => {
  const navigate = useNavigate();

  const handleOnMapClick = () => {
    navigate("/map")
  }

  const fetchReservations = () => {
    navigate("/driver-reservations")
  }

  return (
    <div style={styles.container}>
      <div style={styles.buttonWrapper}>
        <button
          onClick={fetchReservations}
          style={{ ...styles.button, backgroundColor: '#007bff' }}
        >
          Manage your reservations
        </button>

        <button
          onClick={handleOnMapClick}
          style={{ ...styles.button, backgroundColor: '#007bff' }}
        >
          Reserve spot on map
        </button>
      </div>

    </div>
  );
};

const styles = {
  container: {
    padding: '40px',
    textAlign: 'center',
    display: 'grid',
    placeItems: 'center',
    minHeight: '100vh',
    background: 'linear-gradient(135deg, #f4f4f9,rgb(255, 255, 255))',
    fontFamily: '"Roboto", sans-serif',
  },
  buttonWrapper: {
    display: 'grid',
    gap: '20px',
    marginBottom: '20px',
    gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
    justifyItems: 'center',
  },
  button: {
    padding: '15px 30px',
    fontSize: '18px',
    fontWeight: '600',
    cursor: 'pointer',
    border: 'none',
    borderRadius: '8px',
    color: '#fff',
    backgroundColor: '#007bff',
    boxShadow: '0 4px 15px rgba(0, 0, 0, 0.2)',
    transition: 'all 0.3s ease-in-out',
    textTransform: 'uppercase',
    outline: 'none',
    transform: 'scale(1)',
  },
};

export default DriverDashboard;
