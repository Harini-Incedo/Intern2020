import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Engagement } from './engagement';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import {Location} from '@angular/common';
import { Project } from './project';

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
    return this.http.get<Engagement[]>("http://localhost:8080/projects/" + id + "/engagements");
  }

  public create(engagement: Engagement) {
    return this.http.post<Engagement>(this.engagementsUrl, engagement);
  }

  public edit(engagement: Engagement) {
    return this.http.put<Engagement>(this.engagementsUrl + "/" + engagement.id, engagement);
  }

  public delete (id: number) {
    return this.http.delete<Engagement>(this.engagementsUrl + "/" + id);
  }

  public getEngagementByIdApi(id:number): Observable<Engagement>{
    return this.http.get<Engagement>(this.engagementsUrl + "/" + id);
  }

  public setSelectedEngagement(engagement:Engagement):void{
    this.selectedEngagement = engagement;
  }

  public gotoProjectView(id:number) {
    this.router.navigate(['/viewProject/' + id]);
  }

}
