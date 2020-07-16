import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { User } from '../Classes/user';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import {catchError} from 'rxjs/operators'
import {Location} from '@angular/common';
import { Engagement } from '../Classes/engagement';
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

  constructor(private http: HttpClient,private router: Router,private _location: Location) {
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

  public findAll(employeeType:string): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl + "/" + employeeType)
      .pipe(catchError((err,router) => this.handleError(err,this.router)));
  }

  public create(user: User) {
    return this.http.post<User>(this.usersUrl, user)
      .pipe(catchError(this.handleSecondError));
  }

  public delete(id: number) {
    return this.http.delete<User>(this.usersUrl +"/" +id ,this.httpOptions);
  }

  public edit(user: User) {
    return this.http.put<User>(this.usersUrl + "/" + user.id, user);
  }

  public getUserByIdApi(id:number): Observable<User>{
    return this.http.get<User>(this.usersUrl + "/" + id)
      .pipe(catchError((err,router) => this.handleError(err,this.router)));
  }

  public gotoUserList() {
    this.router.navigate(['/employees']);
  }

  public getEngagementByUser(id:number): Observable<Engagement[]> {
    return this.http.get<Engagement[]>(this.usersUrl + "/" + id + "/engagements")
      .pipe(catchError((err,router) => this.handleError(err,this.router)));
  }

  private handleError(errorResponse: HttpErrorResponse, router: Router) {
    let check = confirm(errorResponse.error.errorMessage + ". " + errorResponse.error.debugMessage);
    if (check) {
      router.navigate(['/employees']);
    }
    return throwError(errorResponse);
  }

  private handleSecondError(errorResponse: HttpErrorResponse) {
    let check = confirm(errorResponse.error.errorMessage + ". " + errorResponse.error.debugMessage);
    return throwError(errorResponse);
  }

}