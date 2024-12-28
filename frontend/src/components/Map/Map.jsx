import React, { useEffect, useState, useRef } from "react";
import { MapContainer, TileLayer, Marker } from "react-leaflet";
import 'leaflet/dist/leaflet.css';
import './Map.css';
import { bounds } from "leaflet";
import { use } from "react";
import Popup from "../PopUp";
import createSocket from "../../Socket.js";

function Map() {
  const [parkingAreas, setParkingAreas] = useState([]);
  const [selectedNewParking, setSelectedNewParking] = useState(null);
  const [selectedParking, setSelectedParking] = useState(null);
  const [createdParkingLot, setCreatedParkingLot] = useState(null);
  const [price, setPrice] = useState(10); 
  const [duration, setDuration] = useState(1); 
  const [slots, setSlots] = useState(10);
  const [role, setRole] = useState('');
  const [JWT, setJWT] = useState('');
  const [parkingType, setParkingType] = useState("Regular");
  const [gridState, setGridState] = useState([]);  
  const [capacity, setCapacity] = useState([]); 
  const [isDriver, setIsDriver] = useState(true);
  const [reservedSpots, setReservedSpots] = useState(1);
  const [parkingLotId, setParkingLotId] = useState(null);
  const [parkingSpotId, setParkingSpotId] = useState(null);
  const mapRef = useRef(null);
  const modalRef = useRef(null);
  const [id , setId] = useState(null);
  const [isVisiable, setIsVisible] = useState(false);
  const [notifications, setNotifications] = useState("");


  
  useEffect(() => {
    const storedRole = localStorage.getItem('userRole');
    const storedJWT = localStorage.getItem('jwtToken');
    const storedId = localStorage.getItem('userId');
    if (storedId) {
      setId(storedId);
    }
    if (storedRole) { 
      setRole(storedRole);
      console.log(storedRole);
      if(storedRole === "ROLE_MANAGER"){
        setIsDriver(false);
      }
    } 
    if(storedJWT) {
      setJWT(storedJWT);
    } 
  }, []);

  useEffect(() => {
    const handleNotification = (notification) => {
      console.log(notification);
        setNotifications(notification.message);
        setIsVisible(true);
    };
    const deactivateSocket = createSocket(`/topic/notification/drive/${id}`, handleNotification);
    console.log(id)
    return () => {
        deactivateSocket(); 
    };
}, []);
useEffect(() => {
  const handleNotification = (notification) => {
    console.log(notification);
      setNotifications(notification.message);
      setIsVisible(true);
  };
  const deactivateSocket = createSocket(`/topic/notification/penalty/${id}`, handleNotification);
  console.log(id)
  return () => {
      deactivateSocket(); 
  };
}, []);
useEffect(() => {
  const handleNotification = (notification) => {
    console.log(notification);
      setNotifications(notification.message);
      setIsVisible(true);
  };
  const deactivateSocket = createSocket(`/topic/notification/penalty/${id}`, handleNotification);
  console.log(id)
  return () => {
      deactivateSocket(); 
  };
}, []);

const fetchCreatedParkingAreas = async () => { // DONE
    try {
      const response = await fetch('http://localhost:8080/api/all-lots');
      if (!response.ok) {
        throw new Error(`Backend error: ${response.statusText}`);
      }
      const parkingLots = await response.json();
      const parking = [];
      parkingLots.forEach((lot) => {
        parking.push({
            lat: lot.latitude, 
            lon: lot.longitude,
        });
        });
        setParkingAreas(parking);
    } catch (error) {
        console.error('Error fetching parking areas:', error.message);
    }

};

  const fetchParkingAreas = async (bounds) => { // DONE
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

  const handleOutsideClick = (event) => { // DONE
    if (modalRef.current && !modalRef.current.contains(event.target)) {
      setSelectedNewParking(null);
    }
  };

  useEffect(() => { // DONE
    if (selectedNewParking) {
      document.addEventListener("mousedown", handleOutsideClick);
    }
    return () => {
      document.removeEventListener("mousedown", handleOutsideClick);
    }; 
  }, [selectedNewParking]);

  const handleViewportChange = () => { // DONE
    const mapInstance = mapRef.current;
    mapInstance.on("moveend", handleViewportChange);
    const bounds = mapRef.current.getBounds();
    if(role === "ROLE_MANAGER"){
        fetchParkingAreas(bounds);
    } else {
        fetchCreatedParkingAreas();
    }
  };

  const toggleGridSpot = (row, col) => {
    const updatedGrid = [...gridState];
    if (!(updatedGrid[row][col].status == "occupied")) {
      updatedGrid[row][col].status = updatedGrid[row][col].status == "empty" ? "reserved" : "empty";
      const selectedCount = updatedGrid[row][col].status == "reserved" ? 1 : -1;
      setReservedSpots(reservedSpots + selectedCount);
      if(parkingSpotId === null){
        setParkingSpotId(row * 10 + col + 1)
      } else {
        const rowIndex = Math.floor((parkingSpotId - 1) / 10); 
        const colIndex = (parkingSpotId - 1) % 10;
        updatedGrid[rowIndex][colIndex].status = "empty";
        setParkingSpotId(row * 10 + col + 1)
      }
    }
    setGridState(updatedGrid);
  };

  const handleMarkerClick = async (parking) => { // DONE
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
        setParkingLotId(responseData.id);
      } else {  
        setSelectedNewParking(parking); 
        setPrice(10);
        setSlots(10);
      }
    } catch (error) {
      alert("Error Finding parking data.");
    }
  };

  const resetPopup = async () => { //DONE
    setDuration(1)
    setReservedSpots(1)
    setGridState([]);
    setSelectedParking(null);
    setParkingLotId(null);
    setParkingSpotId(null);
  }

  const onReserveClick = async () => {// - tO be implemented
    if(parkingSpotId === null){
        return;
    }
    const url = "http://localhost:8080/api/drivers/spot/reserve"; 

    const requestData = {
        parkingSpotId: parkingSpotId,
        parkingLotId: parkingLotId,
        duration: duration,
    };

    try {
        const response = await fetch(url, {
        method: "POST",
        headers: {  
            "Content-Type": "application/json",
            Authorization: `Bearer ${JWT}`,
        }, 
        body: JSON.stringify(requestData),
        });

        if (response.ok) {
        console.log("Spot reserved successfully!");
        } else {
        const error = await response.json();
        console.error("Error reserving spot:", error);
        }
    } catch (err) {
        console.error("Network error:", err);
    }
    setDuration(1)
    setReservedSpots(1)
    setGridState([]);
    setSelectedParking(null);
    setParkingLotId(null);
    setParkingSpotId(null);
  }

  const initializeGrid = (rows, cols, parkingSpots) => { // DONE
    
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

  const handleCreationSave = async () => { //DONE
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
              <button onClick={handleCreationSave}>Save</button>
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
              { isDriver && <div className="form-group">
                <label>Duration in Hours:</label>
                <input
                  type="number"
                  min="1"
                  max="48"
                  step={1} 
                  value={duration}
                  onChange={(e) => setDuration(Number(e.target.value))}
                  onKeyDown={(e) => e.preventDefault()}
                />
              </div> }
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
                        onClick={() => {
                            if (isDriver) {
                              toggleGridSpot(rowIndex, colIndex);
                            }
                          }}
                        />
                    ))}
                  </div>
                ))}
              </div>
            </div>
            <div className="modal-actions">
              { isDriver && <button onClick={onReserveClick}>Reserve</button>}
              <button onClick={resetPopup}>Close</button>
            </div>
          </div>
        </div>
      )}
      {isVisiable && <Popup message={notifications} type="success" onClose={() => setIsVisible(false)} />}
    </div>
  );  
}

export default Map;
