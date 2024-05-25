import { Client } from '@stomp/stompjs';

class WebSocketService {
	client;

	constructor() {
		this.client = new Client({
			//brokerURL: 'ws://13.125.161.122:8080/api/ws', // Spring Boot WebSocket 엔드포인트
			brokerURL: 'ws://localhost:8080/api/ws', // Spring Boot WebSocket 엔드포인트
			reconnectDelay: 5000,
			heartbeatIncoming: 4000,
			heartbeatOutgoing: 4000,
		});
	}

	connect(callback) {
		this.client.onConnect = frame => {
			callback();
		};

		this.client.activate();
	}

	subscribe(destination, callback) {
		console.log("Subscribing to: ", destination); // 구독 시도 로그
		this.client.subscribe(destination, message => {
			callback(JSON.parse(message.body));
		});
	}

	send(destination, body, nickname) {
    const messageWithNickname = {
      ...body,
      nickname
    };
    this.client.publish({ destination, body: JSON.stringify(messageWithNickname) });
  }
}

const webSocketService = new WebSocketService();
export default webSocketService;
