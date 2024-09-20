import { Injectable } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';

@Injectable({
  providedIn: 'root',
})
export class StompService {
  private socket: any;
  private stompClient: any;

  constructor() {
    // Initialize WebSocket connection on service initialization
    this.connect();
  }

  // Establish the connection to the WebSocket server using SockJS and Stomp
  connect() {
    this.socket = new SockJS('http://localhost:8090/stomp-endpoint');
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.debug = null;  // Disable debug logs for cleaner output

    // Connect and provide a callback for successful connection
    this.stompClient.connect({}, () => {
      console.log('Connected to WebSocket server.');
    });
  }

  // Subscribe to a WebSocket topic and return an Observable
  subscribe(topic: string): Observable<any> {
    return new Observable((observer) => {
      this.stompClient.subscribe('/topic/' + topic, (message: any): any => {
        // Parse and send the message body to the observer
        observer.next(JSON.parse(message.body));
      });
    });
  }

  // Send data to a specific application endpoint over WebSocket
  send(app: string, data: any) {
    this.stompClient.send('/app/' + app, {}, JSON.stringify(data));
  }

  // Disconnect the WebSocket client when no longer needed
  disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect(() => {
        console.log('Disconnected from WebSocket server.');
      });
    }
  }
}
