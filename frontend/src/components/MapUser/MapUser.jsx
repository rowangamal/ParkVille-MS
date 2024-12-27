import React, { useEffect, useState, useRef } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import "./MapUser.css";

function MapUser() {
  const [parkingAreas, setParkingAreas] = useState([]);
  const [selectedParking, setSelectedParking] = useState(null);
  const [pricePerHour, setPricePerHour] = useState(0);
  const [totalPrice, setTotalPrice] = useState(0);
  const [fromTime, setFromTime] = useState("");
  const [toTime, setToTime] = useState("");
  const [capacity, setCapacity] = useState([]);
  const [gridState, setGridState] = useState([]); // Tracks availability and selections
  const mapRef = useRef(null);
  const modalRef = useRef(null);

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
      resetPopup();
    }
  };

  useEffect(() => {
    if (selectedParking) {
      document.addEventListener("mousedown", handleOutsideClick);
    }
    return () => {
      document.removeEventListener("mousedown", handleOutsideClick);
    };
  }, [selectedParking]);

  const handleViewportChange = () => {
    const bounds = mapRef.current.getBounds();
    fetchParkingAreas(bounds);
  };

  const handleMarkerClick = (parking) => {
    setSelectedParking(parking);
    setPricePerHour(10); // Example value
    setTotalPrice(0);
    setFromTime("");
    setToTime("");
    initializeGrid(5, 8); // Example dimensions (rows x columns)
  };

  const initializeGrid = (rows, cols) => {
    const grid = Array.from({ length: rows }, () =>
      Array.from({ length: cols }, () => ({ occupied: Math.random() < 0.3 }))
    );
    setCapacity({ rows, cols });
    setGridState(grid);
  };

  const toggleGridSpot = (row, col) => {
    const updatedGrid = [...gridState];
    if (!updatedGrid[row][col].occupied) {
      updatedGrid[row][col].selected = !updatedGrid[row][col].selected;
      const selectedCount = updatedGrid[row][col].selected ? 1 : -1;
      setTotalPrice((prev) => prev + selectedCount * pricePerHour);
    }
    setGridState(updatedGrid);
  };

  const resetPopup = () => {
    setSelectedParking(null);
    setTotalPrice(0);
    setFromTime("");
    setToTime("");
    setGridState([]);
  };

  const handleSave = async () => {
    const selectedSlots = [];
    gridState.forEach((row, rowIndex) => {
      row.forEach((cell, colIndex) => {
        if (cell.selected) {
          selectedSlots.push({ row: rowIndex, col: colIndex });
        }
      });
    });

    const payload = {
      longitude: selectedParking.lon,
      latitude: selectedParking.lat,
      pricePerHour,
      totalPrice,
      fromTime,
      toTime,
      selectedSlots,
    };

    try {
      await fetch("/api/save-parking", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      alert("Parking data saved successfully!");
      resetPopup();
    } catch (error) {
      alert("Error saving parking data.");
    }
  };

  useEffect(() => {
    if (mapRef.current) {
      const mapInstance = mapRef.current;
      mapInstance.on("moveend", handleViewportChange);
      handleViewportChange();
    }
  }, []);

  return (
    <div className="map-container">
      <MapContainer
        center={[51.505, -0.09]}
        zoom={13}
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
                <span>${pricePerHour}</span>
              </div>
              <div className="form-group">
                <label>Total Price:</label>
                <span>${totalPrice}</span>
              </div>
              <div className="form-group">
                <label>From:</label>
                <input
                  type="datetime-local"
                  value={fromTime}
                  onChange={(e) => setFromTime(e.target.value)}
                />
              </div>
              <div className="form-group">
                <label>To:</label>
                <input
                  type="datetime-local"
                  value={toTime}
                  onChange={(e) => setToTime(e.target.value)}
                />
              </div>
              <div className="grid">
                {gridState.map((row, rowIndex) => (
                  <div key={rowIndex} className="grid-row">
                    {row.map((cell, colIndex) => (
                      <div
                        key={colIndex}
                        className={`grid-cell ${
                          cell.occupied
                            ? "occupied"
                            : cell.selected
                            ? "selected"
                            : "available"
                        }`}
                        onClick={() => toggleGridSpot(rowIndex, colIndex)}
                      />
                    ))}
                  </div>
                ))}
              </div>
            </div>
            <div className="modal-actions">
              <button onClick={handleSave}>Save</button>
              <button onClick={resetPopup}>Close</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default MapUser;
