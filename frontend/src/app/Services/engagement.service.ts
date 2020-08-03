import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Engagement } from '../Classes/engagement';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import {Location} from '@angular/common';
import { Project } from '../Classes/project';
import { catchError } from 'rxjs/operators';
import { Skill } from '../Classes/skill';
import { Employee } from '../Classes/employee';

@Injectable({
  providedIn: 'root'
})
export class EngagementService {

  private engagementsUrl: string;

  public engagements : Engagement[];

  public selectedEngagements : Engagement[];

  public selectedEngagement : Engagement;

  private Data : Engagement;

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient,private router: Router,private _location: Location) {
    this.engagementsUrl = 'http://localhost:8080/engagements';
  }

  public findAll(id:number): Observable<Skill[]> {
    return this.http.get<Skill[]>("http://localhost:8080/projects/" + id + "/engagements")
      .pipe(catchError((err,router) => this.handleError(err,this.router)));
  }

  public create(skill: Skill) {
    return this.http.post<Skill>(this.engagementsUrl, skill)
      .pipe(catchError(this.handleSecondError));
  }

  public navigateToEditHoursForm(obj: string, obj2: number, obj3: Object) {
    this.router.navigate(
      ['/engagement/' + obj3["id"] + '/editEmployee/' + obj3["employeeID"] + '/project/' + obj3["projectID"] + '/skill/' + obj3["skillID"] + '/date/' + obj+ '/hours/' + obj2]);
  }

  public edit(id: number, obj: object) {
    return this.http.put<any>(this.engagementsUrl + "/" + id, obj, this.httpOptions)
      .pipe(catchError(this.handleSecondError));
  }

  public delete (id: number) {
    return this.http.delete<Engagement>(this.engagementsUrl + "/" + id, this.httpOptions);
  }

  public getEngagementByIdApi(id:number): Observable<Engagement>{
    return this.http.get<Engagement>(this.engagementsUrl + "/" + id)
      .pipe(catchError((err,router) => this.handleError(err,this.router)));
  }

  public setSelectedEngagement(engagement:Engagement):void{
    this.selectedEngagement = engagement;
  }

  public gotoProjectView(id:number) {
    this.router.navigate(['/viewProject/' + id]);
  }

  private handleError(errorResponse: HttpErrorResponse, router: Router) {
    let check = confirm(errorResponse.error.errorMessage + ". " + errorResponse.error.debugMessage);
      if (check) {
        router.navigate(['/projects']);
      }
      return throwError(errorResponse);
   }

  private handleSecondError(errorResponse: HttpErrorResponse) {
    let check = confirm(errorResponse.error.errorMessage + ". " + errorResponse.error.debugMessage);
    return throwError(errorResponse);
  }

}
