import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from './user';
import { Observable } from 'rxjs';

@Injectable()
export class UserService {

  private usersUrl: string;

  public users : User[];

  public selectedUsers : User[];

  public selectedUser : User;

  private Data : User;

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/employees';
  }

  public setUsers(users:User[]){
    this.users = users;
  }

  public setSelectedUser(user:User):void{
    this.selectedUser = user;
  }

  public addToUsers(user:User):void{
    this.selectedUsers.push(user);
  }

  public setSelectedUsers(users:User[]):void{
    this.selectedUsers = users;
  }

  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl);
  }

  public create(user: User) {
    return this.http.post<User>(this.usersUrl, user);
  }

  public delete(id: number) {
    this.http.delete<User>(this.usersUrl+"/" +id );
  }

  public edit(user: User) {
    return this.http.put<User>(this.usersUrl + "/" + user.id, user);
  }

  public getUserByIdApi(id:number): Observable<User>{
    return this.http.get<User>(this.usersUrl + "/" + id)
  }

}