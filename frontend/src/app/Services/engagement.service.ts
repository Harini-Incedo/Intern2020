import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Engagement } from '../Classes/engagement';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import {Location} from '@angular/common';
import { Project } from '../Classes/project';
import { catchError } from 'rxjs/operators';

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

  public findAll(id:number): Observable<Engagement[]> {
    return this.http.get<Engagement[]>("http://localhost:8080/projects/" + id + "/engagements")
      .pipe(catchError((err,router) => this.handleError(err,this.router)));
  }

  public create(engagement: Engagement) {
    return this.http.post<Engagement>(this.engagementsUrl, engagement)
      .pipe(catchError(this.handleSecondError));
  }

  public edit(engagement: Engagement) {
    return this.http.put<Engagement>(this.engagementsUrl + "/" + engagement.id, engagement)
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
