import { Client } from '@stomp/stompjs';

class WebSocketService {
  client;

  constructor() {
    this.client = new Client({
      brokerURL: 'ws://localhost:8080/api/ws', // Spring Boot WebSocket 엔드포인트
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });
  }

  connect(callback) {
    this.client.onConnect = frame => {
      console.log('Connected: ' + frame);
      callback();
    };

    this.client.activate();
  }

  subscribe(destination, callback) {
    this.client.subscribe(destination, message => {
      callback(JSON.parse(message.body));
    });
  }

  send(destination, body) {
    this.client.publish({ destination, body: JSON.stringify(body) });
  }
}

const webSocketService = new WebSocketService();
export default webSocketService;
