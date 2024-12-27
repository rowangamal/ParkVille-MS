import * as Stomp from '@stomp/stompjs';

const createSocket = (path, onMessageReceived) => {
    const stompClient = new Stomp.Client({
        brokerURL: 'ws://localhost:8080/ws', 
        reconnectDelay: 5000, 
        onConnect: () => {
            console.log('WebSocket connection established');

            stompClient.subscribe(path, (message) => {
                const notification = JSON.parse(message.body);
                onMessageReceived(notification); 
            });
        },
        onStompError: (frame) => {
            console.error('Broker reported error: ', frame.headers['message']);
            console.error('Additional details: ', frame.body);
        },
        onDisconnect: () => {
            console.log('WebSocket connection closed');
        },
    });

    stompClient.activate(); 

    return () => {
        if (stompClient.active) {
            stompClient.deactivate();
            console.log('WebSocket connection deactivated');
        }
    };
};

export default createSocket;