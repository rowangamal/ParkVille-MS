import React, { useState, useEffect } from 'react';

const DriverReservations = () => {
    const [JWT, setJWT] = useState('');
    const [reservedSpots, setReservedSpots] = useState([]);
    const [error, setError] = useState(null);

    const fetchReservedSpots = async () => {
        const storedJWT = localStorage.getItem('jwtToken');
        if(storedJWT) {
            setJWT(storedJWT);
        }
        try {
            const response = await fetch('http://localhost:8080/api/drivers/spot/all',
                {
                    method: 'GET',
                    headers: {  
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${storedJWT}`,
                    }, 
                }
            );
            if (!response.ok) {
                throw new Error(await response.text());
            }
            const data = (await response.json()).filter(spot => spot.leaveTime == null);
            setReservedSpots(data);
        } catch (err) {
            setError(err.message);
        }
    };

    useEffect(() => {
        fetchReservedSpots();
    }, []);

    const refreshSpots = () => {
        fetchReservedSpots();
    };

    const handleArrive = async (parkingSpotId, parkingLotId) => {
        const requestBody = {
        parkingSpotId: parseInt(parkingSpotId),
        parkingLotId: parseInt(parkingLotId),
        };
    
        try {
        const response = await fetch("http://localhost:8080/api/drivers/spot/arrive", {
            method: "PUT",
            headers: {  
                "Content-Type": "application/json",
                Authorization: `Bearer ${JWT}`,
            }, 
            body: JSON.stringify(requestBody),
        });
    
        if (response.ok) {
            refreshSpots();
        } else {
            const error = await response.text();
            console.error(error)
        }
        } catch (error) {
            console.error(error.message)
        }
    };
    
    const handleLeave = async (parkingSpotId, parkingLotId) => {
        const requestBody = {
        parkingSpotId: parseInt(parkingSpotId),
        parkingLotId: parseInt(parkingLotId),
        };
    
        try {
        const response = await fetch("http://localhost:8080/api/drivers/spot/leave", {
            method: "PUT",
            headers: {  
                "Content-Type": "application/json",
                Authorization: `Bearer ${JWT}`,
            }, 
            body: JSON.stringify(requestBody),
        });
    
        if (response.ok) {
            refreshSpots();
        } else {
            const error = await response.text();
            console.error(error)
        }
        } catch (error) {
            console.error(error.message)
        }
    };
    

    return (
        <div style={{ padding: '20px', fontFamily: 'Arial, sans-serif' }}>
            <h1>Reserved Spots</h1>
            {error && <p style={{ color: 'red' }}>Error: {error}</p>}
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '20px' }}>
                {reservedSpots.map((spot, index) => (
                    <div
                        key={index}
                        style={{
                            border: '1px solid #ccc',
                            borderRadius: '8px',
                            padding: '10px',
                            width: '300px',
                            boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                        }}
                    >
                        <h3>Spot ID: {spot.parkingSpotId}, Lot ID: {spot.parkingSpotParkingLotId}</h3>
                        <p>Start Time: {new Date(spot.startTime).toLocaleString()}</p>
                        <p>End Time: {new Date(spot.endTime).toLocaleString()}</p>
                        <p>Arrival Time: {spot.arrivalTime ? new Date(spot.arrivalTime).toLocaleString() : 'Not Arrived'}</p>
                        <p>Leave Time: {spot.leaveTime ? new Date(spot.leaveTime).toLocaleString() : 'Not Left'}</p>
                        <p>Price: ${spot.price.toFixed(2)}</p>
                        <p>Penalty: ${spot.penalty.toFixed(2)}</p>
                        <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '10px' }}>
                            {spot.arrivalTime === null && <button
                                onClick={() => handleArrive(spot.parkingSpotId, spot.parkingSpotParkingLotId)}
                                style={{
                                    backgroundColor: '#4CAF50',
                                    color: 'white',
                                    border: 'none',
                                    padding: '10px 20px',
                                    borderRadius: '4px',
                                    cursor: 'pointer',
                                }}
                            >
                                Arrive
                            </button>}
                            {spot.arrivalTime !== null && <button
                                onClick={() => handleLeave(spot.parkingSpotId, spot.parkingSpotParkingLotId)}
                                style={{
                                    backgroundColor: '#f44336',
                                    color: 'white',
                                    border: 'none',
                                    padding: '10px 20px',
                                    borderRadius: '4px',
                                    cursor: 'pointer',
                                }}
                            >
                                Leave
                            </button>}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default DriverReservations;
