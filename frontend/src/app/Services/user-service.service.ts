import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { User } from '../Classes/user';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import {catchError} from 'rxjs/operators'
import {Location} from '@angular/common';
import { Engagement } from '../Classes/engagement';
import { Skill } from '../Classes/skill';
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

  public findRecommended(sampleMap:object): Observable<User[]> {
    return this.http.post<User[]>(this.usersUrl + "/recommended", sampleMap);
  }

  public create(user: User) {
    return this.http.post<User>(this.usersUrl, user)
      .pipe(catchError(this.handleSecondError));
  }

  public delete(id: number) {
    return this.http.delete<User>(this.usersUrl +"/" +id ,this.httpOptions);
  }

  public edit(user: User) {
    return this.http.put<User>(this.usersUrl + "/" + user.id, user)
      .pipe(catchError(this.handleThirdError));
  }

  public getUserByIdApi(id:number): Observable<User>{
    return this.http.get<User>(this.usersUrl + "/" + id)
      .pipe(catchError((err,router) => this.handleError(err,this.router)));
  }

  public getSkills(): any{
    return this.http.get("http://localhost:8080/skills", this.httpOptions);
  }

  public getSkillByID(id: number): Observable<Skill>{
    return this.http.get<Skill>("http://localhost:8080/skills/" + id);
  }

  public gotoUserList() {
    this.router.navigate(['/employees']);
  }

  public getEngagementByUser(id:number): Observable<Engagement[]> {
    return this.http.get<Engagement[]>(this.usersUrl + "/" + id + "/engagements")
      .pipe(catchError((err,router) => this.handleError(err,this.router)));
  }

  public activateEmployee(id:number): Observable<User> {
    return this.http.put<User>(this.usersUrl + `/${id}/activate`, this.httpOptions);
  }

  private handleError(errorResponse: HttpErrorResponse, router: Router) {
    alert(errorResponse.error.errorMessage + ". " + errorResponse.error.debugMessage);
    this._location.back();
    return throwError(errorResponse);
  }

  private handleSecondError(errorResponse: HttpErrorResponse) {
    alert(errorResponse.error.errorMessage + ". " + errorResponse.error.debugMessage);
    return throwError(errorResponse);
  }

  private handleThirdError(errorResponse: HttpErrorResponse) {
    alert(errorResponse.error.errorMessage + ". " + errorResponse.error.debugMessage);
    return throwError(errorResponse);
  }

}