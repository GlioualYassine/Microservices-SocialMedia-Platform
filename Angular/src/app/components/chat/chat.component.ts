import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { MessageRequest } from '../../interfaces/message-request';
import { ApiResponse } from 'src/app/interfaces/api-response';
import { User } from 'src/app/interfaces/user';
import { ConversationResponse } from 'src/app/interfaces/conversation-response';
import { MessageResponse } from 'src/app/interfaces/message-response';
import { StompService } from 'src/app/services/stomp.service';
import { Subscription } from 'rxjs';
import { WebSocketResponse } from 'src/app/interfaces/web-socket-response';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit, OnDestroy {
  currentUser: User = {
    id: 0,
    firstName: '',
    lastName: '',
    email: '',
    password: '',
  };
  // all users except current user
  users: User[] = [];
  // users all conversations
  userConversations: ConversationResponse[] = [];
  // WebSocket subscriptions
  stompUserSub: Subscription | undefined;
  stompConvSub: Subscription | undefined;
  stompOnlineUsersSub: Subscription | undefined;
  stompOfflineUsersSub: Subscription | undefined;

  // selected conversation
  selectedConversationId: number = -1;
  selectedConversationReceiverId: number = -1;
  selectedConversationReceiverName: string = '';
  selectedConversation: MessageResponse[] = [];
  showUserState: boolean = false;
  message: string = '';

  onlineUserNotification: string | null = null;

  // Assuming you have a WebSocket or similar service for user status updates
  onUserOnline(user: any) {
    this.onlineUserNotification = `${user.firstName} ${user.lastName} is online!`;
  
    // Clear the notification after 5 seconds
    setTimeout(() => {
      this.onlineUserNotification = null;
    }, 5000);
  }

  constructor(
    private router: Router,
    private userService: UserService,
    private stomp: StompService
  ) {
    this.currentUser = userService.currentUser();
  }

  ngOnInit(): void {
    this.subscribeToCurrentUserConversation();
    this.subscribeToOnlineUsers(); // Subscribe to online users
    this.subscribeToOfflineUsers(); // Subscribe to offline users
  }

  ngOnDestroy(): void {
    this.stompUserSub?.unsubscribe();
    this.stompConvSub?.unsubscribe();
    this.stompOnlineUsersSub?.unsubscribe(); // Unsubscribe from online users
  }

  // Toggle user list and get all users except the current user
  onShowHideUserConversation() {
    this.showUserState = !this.showUserState;
    if (this.showUserState) {
      this.userService
        .getAllUsersExceptCurrentUser()
        .subscribe((res: ApiResponse) => {
          this.users = res.data;
        });
    }
  }

  onCloseChat() {
    this.stompConvSub?.unsubscribe();
    this.selectedConversationId = -1;
  }

  onUserLogout() {
    this.userService.userLogout();
    this.router.navigate(['.']);
  }

  // Subscribe to the current user's conversation updates
  subscribeToCurrentUserConversation() {
    setTimeout(() => {
      this.stompUserSub = this.stomp
        .subscribe('user/' + this.currentUser.id)
        .subscribe((payload: any) => {
          let res: WebSocketResponse = payload;
          if (res.type == 'ALL') {
            this.userConversations = res.data;
            const found = this.userConversations.find(
              (item) => item.conversationId === this.selectedConversationId
            );
            if (!found) {
              this.onCloseChat();
            }
          }
        });
      // Notify server to send initial data
      this.stomp.send('user', this.currentUser.id);
    }, 1000);
  }

  // Subscribe to the online users WebSocket to receive notifications
  subscribeToOnlineUsers() {
    this.stompOnlineUsersSub = this.stomp
      .subscribe('online-users')
      .subscribe((res: WebSocketResponse) => {
        if (res.type === 'NEW_USER_ONLINE') {
          console.log(res.data); // You can display this message in the UI
          alert(res.data); // Simple alert for new online users
        }
      });
  }

  //Subscribe to the offline users WebSocket to receive notifications
  subscribeToOfflineUsers() {
    this.stompOfflineUsersSub == this.stomp
      .subscribe('offline-users')
      .subscribe((res: WebSocketResponse) => {
        if(res.type === 'NEW_USER_OFFLINE') {
          console.log(res.data); // You can display this message in the UI
          alert(res.data); // Simple alert for offline users
        }
      })
  }

  // Handle user selection to start a conversation
  onUserSelected(receiverId: number, receiverName: string) {
    this.selectedConversationReceiverId = receiverId;
    this.selectedConversationReceiverName = receiverName;
    this.userService
      .getConversationIdByUser1IdAndUser2Id(receiverId, this.currentUser.id)
      .subscribe((res: ApiResponse) => {
        this.selectedConversationId = res.data;
        this.onShowHideUserConversation();
        this.setConversation();
      });
  }

  onConversationSelected(index: number) {
    console.log(this.userConversations[index]);
    this.selectedConversationId = this.userConversations[index].conversationId;
    this.selectedConversationReceiverId = this.userConversations[index].otherUserId;
    this.selectedConversationReceiverName = this.userConversations[index].otherUserName;

    this.setConversation();
  }

  // Set a conversation for the selected conversationId
  setConversation() {
    this.stompConvSub?.unsubscribe();
    this.stompConvSub = this.stomp
      .subscribe('conv/' + this.selectedConversationId)
      .subscribe((payload: any) => {
        let res: WebSocketResponse = payload;
        if (res.type == 'ALL') {
          this.selectedConversation = res.data;
        } else if (res.type == 'ADDED') {
          let msg: MessageResponse = res.data;
          this.selectedConversation.unshift(msg);
        }
      });
    this.stomp.send('conv', this.selectedConversationId);
  }

  onSendMessage() {
    if (this.message.trim().length == 0) return;

    const timestamp = new Date();
    let body: MessageRequest = {
      conversationId: this.selectedConversationId,
      senderId: this.userService.currentUser().id,
      receiverId: this.selectedConversationReceiverId,
      message: this.message.trim(),
      timestamp: timestamp,
    };
    this.stomp.send('sendMessage', body);
    this.message = '';
  }

  onDeleteConversation() {
    this.stomp.send('deleteConversation', {
      conversationId: this.selectedConversationId,
      user1Id: this.currentUser.id,
      user2Id: this.selectedConversationReceiverId,
    });
  }

  onDeleteMessage(messageId: number) {
    this.stomp.send('deleteMessage', {
      conversationId: this.selectedConversationId,
      messageId: messageId,
    });
  }
}
