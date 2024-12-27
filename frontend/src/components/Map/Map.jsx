import React, { useEffect, useState, useRef } from "react";
import { MapContainer, TileLayer, Marker } from "react-leaflet";
import 'leaflet/dist/leaflet.css';
import './Map.css';

function Map() {
  const [parkingAreas, setParkingAreas] = useState([]);
  const [selectedNewParking, setSelectedNewParking] = useState(null);
  const [selectedParking, setSelectedParking] = useState(null);
  const [createdParkingLot, setCreatedParkingLot] = useState(null);
  const [price, setPrice] = useState(10); 
  const [slots, setSlots] = useState(10);
  const [role, setRole] = useState('');
  const [JWT, setJWT] = useState('');
  const [parkingType, setParkingType] = useState("Regular");
  const [gridState, setGridState] = useState([]);  
  const [capacity, setCapacity] = useState([]); 
  const mapRef = useRef(null);
  const modalRef = useRef(null);
  
  useEffect(() => {
    const storedRole = localStorage.getItem('role');
    const storedJWT = localStorage.getItem('jwtToken');
    if (storedRole) { 
      setRole(storedRole);
    } 
    if(storedJWT) {
      setJWT(storedJWT);
    } 
  }, []);

  const fetchParkingAreas = async (bounds) => {
    const overpassUrl = "https://overpass-api.de/api/interpreter";
    const query = `[out:json];
      (
        node["amenity"="parking"](${bounds.getSouth()}, ${bounds.getWest()}, ${bounds.getNorth()}, ${bounds.getEast()});
        way["amenity"="parking"](${bounds.getSouth()}, ${bounds.getWest()}, ${bounds.getNorth()}, ${bounds.getEast()});
        relation["amenity"="parking"](${bounds.getSouth()}, ${bounds.getWest()}, ${bounds.getNorth()}, ${bounds.getEast()});
      );
      out body;`;   

    const response = await fetch(overpassUrl, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: `data=${query}`,
    });
    const data = await response.json();

    const parking = [];

    const parkingNodes = data.elements.filter((element) => element.type === "node");
    parkingNodes.forEach((node) => {
      parking.push({
        lat: node.lat,
        lon: node.lon,
      });
    });

    setParkingAreas(parking);
  };

  const handleOutsideClick = (event) => {
    if (modalRef.current && !modalRef.current.contains(event.target)) {
      setSelectedNewParking(null);
    }
  };

  useEffect(() => {
    if (selectedNewParking) {
      document.addEventListener("mousedown", handleOutsideClick);
    }
    return () => {
      document.removeEventListener("mousedown", handleOutsideClick);
    }; 
  }, [selectedNewParking]);

  const handleViewportChange = () => {
    const mapInstance = mapRef.current;
    mapInstance.on("moveend", handleViewportChange);
    const bounds = mapRef.current.getBounds();
    fetchParkingAreas(bounds);
  };

  const handleMarkerClick = async (parking) => {
    try { 
      const checkPayload = {
        longitude: parking.lon,
        latitude: parking.lat,
      };
    
      const response = await fetch("http://localhost:8080/api/is-parking-lot-created", {
        method: "POST",
        headers: {  
          "Content-Type": "application/json",
          Authorization: `Bearer ${JWT}`,
        }, 
        body: JSON.stringify(checkPayload),
      });
    
      if (response.ok) {
        const responseData = await response.json();
        const numberOfRows = responseData.parkingSpots.length/10;
        const numberOfColumns = 10
        initializeGrid(numberOfRows, numberOfColumns, responseData.parkingSpots);  
        setCreatedParkingLot(responseData);
        setSelectedParking(parking);
      } else {  
        setSelectedNewParking(parking); 
        setPrice(10);
        setSlots(10);
      }
    } catch (error) {
      alert("Error Finding parking data.");
    }
  };

  const resetPopup = () => {
    setGridState([]);
    setSelectedParking(null);
  }

  const initializeGrid = (rows, cols, parkingSpots) => {
    
    const grid = Array.from({ length: rows }, () =>
      Array.from({ length: cols }, () => ({ status: 'empty' }))
    );
  
    parkingSpots.forEach(spot => {
      const { id, status } = spot;
      const rowIndex = Math.floor((id - 1) / cols); 
      const colIndex = (id - 1) % cols;
  
      grid[rowIndex][colIndex].status = status;
    });

    setCapacity({ rows, cols });
    setGridState(grid);
  };

  const handleSave = async () => {
    const payload = {
      longitude: selectedNewParking.lon,
      latitude: selectedNewParking.lat,
      pricePerHour: price,
      numberOfSlots: slots,
      parkingType,
    };

    try {
      await fetch("http://localhost:8080/api/managers/create-parking-lot", {
        method: "POST",
        headers: { 
          "Content-Type": "application/json",
          Authorization: `Bearer ${JWT}`,
        }, 
        body: JSON.stringify(payload),
      });
      setSelectedNewParking(null);
    } catch (error) {
      alert("Error saving parking data.");
    }
  };

  return (
    <div className="map-container">
      <button
        onClick={handleViewportChange}
        style={{
          position: "absolute",
          top: "10px",
          left: "60px",
          zIndex: 1000,
          padding: "10px",
          backgroundColor: "#008CBA",
          color: "white",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
        }}
      >
        Refresh Map
      </button>
      <MapContainer
        center={[31.204435, 29.9083316]}
        zoom={18} 
        scrollWheelZoom={true}
        style={{ height: "100vh", width: "100%" }}
        ref={mapRef}
      >
        <TileLayer 
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
  
        {parkingAreas.map((area, index) => (
          <Marker
            key={index}
            position={[area.lat, area.lon]}
            eventHandlers={{
              click: () => handleMarkerClick(area),
            }}
          ></Marker>
        ))}
      </MapContainer>
  
      {selectedNewParking && (
        <div className="modal-overlay">
          <div className="modal" ref={modalRef}>
            <h2>Parking Details</h2>
            <div className="modal-content">
              <div className="form-group">
                <label>Longitude:</label>
                <span>{selectedNewParking.lon}</span>
              </div>
              <div className="form-group">
                <label>Latitude:</label>
                <span>{selectedNewParking.lat}</span>
              </div>
              <div className="form-group">
                <label>Price per Hour:</label>
                <input
                  type="number"
                  min="10"
                  max="100"
                  step={5} 
                  value={price}
                  onChange={(e) => setPrice(Number(e.target.value))}
                  onKeyDown={(e) => e.preventDefault()}
                />
              </div>
              <div className="form-group">
                <label>Number of Slots:</label>
                <input
                  type="number"
                  min="10"
                  max="100"
                  step={10}
                  value={slots}
                  onChange={(e) => setSlots(Number(e.target.value))}
                  onKeyDown={(e) => e.preventDefault()}
                />
              </div>
              <div className="form-group"> 
                <label>Parking Slot Type:</label>
                <select
                  onChange={(e) => setParkingType(e.target.value)}
                  defaultValue=""
                >
                  <option value="Regular" selected>Regular</option>
                  <option value="Disabled">Disabled</option>
                  <option value="EV Charging">EV Charging</option>
                </select>
              </div>
            </div>
            <div className="modal-actions">
              <button onClick={handleSave}>Save</button>
              <button onClick={() => setSelectedNewParking(null)}>Close</button>
            </div>
          </div>
        </div>
      )}

      {selectedParking && (
        <div className="modal-overlay">
          <div className="modal" ref={modalRef}>
            <h2>Parking Details</h2>
            <div className="modal-content">
              <div className="form-group">
                <label>Longitude:</label>
                <span>{selectedParking.lon}</span>
              </div>
              <div className="form-group">
                <label>Latitude:</label>
                <span>{selectedParking.lat}</span>
              </div>
              <div className="form-group">
                <label>Price per Hour:</label>
                <span>${createdParkingLot.pricePerHour}</span>
              </div>
              <div className="form-group">
                <label>Parking type:</label>
                <span>{createdParkingLot.parkingType}</span>
              </div> 
              <div className="grid">
                {gridState.map((row, rowIndex) => (
                  <div key={rowIndex} className="grid-row">
                    {row.map((cell, colIndex) => (
                      <div
                        key={colIndex}
                        className={`grid-cell ${
                          cell.status === 'occupied'
                            ? 'occupied'
                            : cell.status === 'reserved'
                            ? 'reserved'
                            : 'available'
                        }`}
                      />
                    ))}
                  </div>
                ))}
              </div>
            </div>
            <div className="modal-actions">
              <button onClick={resetPopup}>Close</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );  
}
export default Map;
