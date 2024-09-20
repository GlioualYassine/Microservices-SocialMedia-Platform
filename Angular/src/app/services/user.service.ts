import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../interfaces/login-request';
import { Observable } from 'rxjs';
import { ApiResponse } from '../interfaces/api-response';
import { User } from '../interfaces/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private baseUrl: string = 'http://localhost:';

  constructor(private http: HttpClient) {}

  // Attempt to log in a user with the provided credentials
  userLogin(body: LoginRequest): Observable<ApiResponse> {
    let port  = '8089';
    return this.http.post<ApiResponse>(this.baseUrl.concat(`${port}/api/v1/auth/authenticate`), body);
  }

  // Register a new user with the provided user data
  userRegister(body: User): Observable<ApiResponse> {
    let port  = '8089';
    return this.http.post<ApiResponse>(this.baseUrl.concat(`${port}/api/v1/auth/register`), body);
  }

  userLogout(): void {
    const userString = localStorage.getItem('user');
    let token = localStorage.getItem('token');
    let port = '8089';
    if (userString) {
      // Convertir la chaîne en objet JSON
      const user = JSON.parse(userString);
      this.http.get<ApiResponse>(this.baseUrl.concat(`${port}/api/v1/auth/logout/${user.id}/${token}`)).subscribe();
      localStorage.removeItem('user');
      localStorage.removeItem('token');
    
   }   
  }

  // Retrieve a list of all users except the currently logged-in user
  getAllUsersExceptCurrentUser(): Observable<ApiResponse> {
    let port = '8090';
    return this.http.get<ApiResponse>(
      this.baseUrl.concat(`${port}/user/except/` + this.currentUser().id)
    );
  }

  // Retrieve the conversation ID between two users
  getConversationIdByUser1IdAndUser2Id(
    user1Id: number,
    user2Id: number
  ): Observable<ApiResponse> {
    let port = '8090';
    return this.http.get<ApiResponse>(this.baseUrl.concat(`${port}/user/conversation/id`), {
      params: { user1Id: user1Id, user2Id: user2Id },
    });
  }

  // Retrieve the currently logged-in user from local storage
  currentUser(): User {
    return JSON.parse(localStorage.getItem('user') || '{}');
  }


}
