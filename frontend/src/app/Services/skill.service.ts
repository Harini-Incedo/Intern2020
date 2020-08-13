import { Injectable } from '@angular/core';
import { Project } from '../Classes/project';
import { Skill } from '../Classes/skill';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SkillService {

  url : string;

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json;charset=utf-8' })
  };

  constructor(private http: HttpClient) {
    this.url = "http://localhost:8080/projects/"
   }



  public create(project: Project, test:object) {
    return this.http.put<any>(this.url + `${project.id}/skills`, test ,this.httpOptions);
  }

  public getById(id: number):Observable<Skill>{
    return this.http.get<Skill>('http://localhost:8080/skills/' + id);
  }

  public update(id: number, totalWeeklyHours: number) {
    return this.http.put<any>('http://localhost:8080/skills/' + id + '/update', totalWeeklyHours, this.httpOptions)
  }
}
