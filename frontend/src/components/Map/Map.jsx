import React, { useEffect, useState, useRef } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import 'leaflet/dist/leaflet.css';
import './Map.css';

function Map() {
  const [parkingAreas, setParkingAreas] = useState([]);
  const [selectedParking, setSelectedParking] = useState(null);
  const [price, setPrice] = useState(0);
  const [slots, setSlots] = useState(5);
  const [managerId, setManagerId] = useState(1); // TODO to be sent to backend
  const [parkingType, setParkingType] = useState("");
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
      setSelectedParking(null);
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
    setPrice(0);
    setSlots(5);
  };

  const handleSave = async () => {
    const payload = {
      longitude: selectedParking.lon,
      latitude: selectedParking.lat,
      pricePerHour: price,
      numberOfSlots: slots,
      parkingType,
    };

    try {
      await fetch("/api/save-parking", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      alert("Parking data saved successfully!");
      setSelectedParking(null); // Close the popup
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
                <input
                  type="number"
                  min="0"
                  max="100"
                  value={price}
                  onChange={(e) => setPrice(Number(e.target.value))}
                />
              </div>
              <div className="form-group">
                <label>Number of Slots:</label>
                <input
                  type="number"
                  min="5"
                  max="100"
                  value={slots}
                  onChange={(e) => setSlots(Number(e.target.value))}
                />
              </div>
              <div className="form-group">
                <label>Parking Slot Type:</label>
                <select
                  onChange={(e) => setParkingType(e.target.value)}
                  defaultValue=""
                >
                  <option value="" disabled>
                    Select Slot Type
                  </option>
                  <option value="Regular">Regular</option>
                  <option value="Disabled">Disabled</option>
                  <option value="EV Charging">EV Charging</option>
                </select>
              </div>
            </div>
            <div className="modal-actions">
              <button onClick={handleSave}>Save</button>
              <button onClick={() => setSelectedParking(null)}>Close</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );  
}

export default Map;
