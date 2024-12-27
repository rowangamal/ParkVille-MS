import React, { useState } from 'react';
import Iframe from 'react-iframe';
import axios from 'axios';
import { useNavigate } from "react-router-dom";

const ManagerDashboard = () => {
  const [pdfUrl, setPdfUrl] = useState(null);
  const navigate = useNavigate();
  const token = localStorage.getItem('jwtToken'); 

  const handleOnMapClick = () => {
    navigate("/map")
  }

  const fetchPDF = async () => {
    if (!token) {
      console.error('No token found');
      return;
    }

    try {
      console.log(token); // Log the token for debugging purposes

      // Sending the token as a Bearer token in the Authorization header
      const response = await axios.get(
        'http://localhost:8080/api/report/parking-lot-manager',
        {
          headers: {
            Authorization: `Bearer ${token}`, // Bearer token in the Authorization header
          },
          responseType: 'blob', // Expecting a PDF blob as the response
        }
      );

      const url = URL.createObjectURL(response.data);
      setPdfUrl(url);
    } catch (error) {
      console.error('Error fetching the PDF:', error);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.buttonWrapper}>
        <button
          onClick={fetchPDF}
          style={{ ...styles.button, backgroundColor: '#007bff' }}
        >
          Load Parking Lot Manager Report
        </button>

        <button
          onClick={handleOnMapClick}
          style={{ ...styles.button, backgroundColor: '#007bff' }}
        >
          Manage lots on map
        </button>
      </div>

      {pdfUrl && (
        <Iframe
          url={pdfUrl}
          width="100%"
          height="600px"
          display="initial"
          position="relative"
          style={styles.iframe}
        />
      )}
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
  iframe: {
    marginTop: '30px',
    border: 'none',
    borderRadius: '12px',
    boxShadow: '0 8px 20px rgba(0, 0, 0, 0.2)',
    transform: 'scale(1)',
    transition: 'transform 0.3s ease',
  },
};

export default ManagerDashboard;
