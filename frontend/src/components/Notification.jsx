import React, { useState, useEffect } from 'react';
import createSocket from '../Socket.js';

function Notification() {
    const [notifications, setNotifications] = useState([]);
    const [status, setStatus] = useState('Disconnected');

    useEffect(() => {
        const handleNotification = (notification) => {
            setNotifications((prevNotifications) => [notification, ...prevNotifications]);
        };
        const deactivateSocket = createSocket('/topic/notification/1', handleNotification);
        setStatus('Connected');
        return () => {
            deactivateSocket(); 
            setStatus('Disconnected');
        };
    }, []);

    return (
        <div>
            <h2>Notifications ({status})</h2>
            <ul>
                {notifications.map((msg, index) => (
                    <li key={index}>{msg.message}</li> // Ensure message has a 'message' property
                ))}
            </ul>
        </div>
    );
}

export default Notification;

// useEffect(() => {
    //     const stompClient = new Stomp.Client({
    //         brokerURL: 'ws://localhost:8080/ws', 
    //         reconnectDelay: 5000, 
    //         onConnect: () => {
    //             setStatus('Connected');
    //             console.log('WebSocket connection established');
    //             stompClient.subscribe('/topic/notification', (message) => {
    //                 const notification = JSON.parse(message.body);
    //                 setNotifications((prevNotifications) => [notification, ...prevNotifications]);
    //             });
    //         },
    //         onStompError: (frame) => {
    //             console.error('Broker reported error: ', frame.headers['message']);
    //             console.error('Additional details: ', frame.body);
    //         },
    //         onDisconnect: () => {
    //             setStatus('Disconnected');
    //             console.log('WebSocket connection closed');
    //         },
    //     });

    //     stompClient.activate(); 

    //     return () => {
    //         if (stompClient.active) {
    //             stompClient.deactivate();
    //             console.log('WebSocket connection deactivated');
    //         }
    //     };
    // }, []);