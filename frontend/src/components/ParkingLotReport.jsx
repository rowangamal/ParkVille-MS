import React from 'react';
import axios from 'axios';

const ParkingLotReport = () => {
  
  const downloadReport = async () => {
    try {
      const response = await axios.get('http://localhost:8080/reports/parking-lot-performance', {
        responseType: 'blob',
      });
      
      // Create a link to download the file
      const blob = response.data;
      const link = document.createElement('a');
      const url = window.URL.createObjectURL(blob);
      link.href = url;
      link.setAttribute('download', 'parking_lot_performance.pdf');
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (error) {
      console.error("There was an error downloading the report:", error);
    }
  };

  return (
    <div className="report-container">
      <h1>Parking Lot Performance Report</h1>
      <button onClick={downloadReport}>Download PDF Report</button>
    </div>
  );
};

export default ParkingLotReport;
