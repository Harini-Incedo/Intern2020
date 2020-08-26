import { Injectable } from '@angular/core';
import { Project } from '../Classes/project';
import { Skill } from '../Classes/skill';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import {catchError} from 'rxjs/operators'
import { Router } from '@angular/router';
import { GeneralService } from './general.service';

@Injectable({
  providedIn: 'root'
})
export class SkillService {

  url : string;
  skillsURL: string;

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json;charset=utf-8' })
  };

  constructor(private http: HttpClient, private router: Router, private generalService: GeneralService) {
    this.url = "http://localhost:8080/projects/";
    this.skillsURL = "http://localhost:8080/skills/";
   }



  public create(project: Project, test:object) {
    return this.http.post<any>(this.url + `${project.id}/skills`, test, this.httpOptions)
      .pipe(catchError((err) => this.handleError(err)));
  }

  public getById(id: number):Observable<Skill>{
    return this.http.get<Skill>(this.skillsURL + id);
  }

  public update(id: number, test: object) {
    return this.http.put<any>(this.skillsURL + id + '/update', test, this.httpOptions)
  }

  public delete(id: number) {
    return this.http.delete<any>(this.skillsURL + id);
  }

  public addEngagements(project: Project, test: object) {
    return this.http.put<any>(this.url + `${project.id}/skills`, test, this.httpOptions);
  }

  private handleError(errorResponse: HttpErrorResponse) {
    alert(errorResponse.error.errorMessage + ". " + errorResponse.error.debugMessage);
    this.generalService.goBack();
    return throwError(errorResponse);
  }
}
