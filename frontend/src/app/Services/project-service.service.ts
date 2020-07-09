import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Project } from '../Classes/project';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectServiceService {

  private projectsUrl: string;
  public projects : Project[];
  public selectedProject : Project;
  public selectedProjects : Project[];

  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient, private router: Router) {
    this.projectsUrl = 'http://localhost:8080/projects';
  }

  public setSelectedProject(project:Project):void{
    this.selectedProject = project;
  }

  gotoProjectList() : void {
    this.router.navigate(['/projects']);
  }

  public create(project: Project) {
    return this.http.post<Project>(this.projectsUrl, project);
  }

  public edit(project: Project) {
    return this.http.put<Project>(this.projectsUrl + "/" + project.id, project);
  }

  public findAll(): Observable<Project[]> {
    return this.http.get<Project[]>(this.projectsUrl);
  }

  public addToProjects(project:Project):void{
    this.selectedProjects.push(project);
  }

  public setProjects(projects:Project[]){
    this.projects = projects;
  }

  public getProjectByIdApi(id:number): Observable<Project>{
    return this.http.get<Project>(this.projectsUrl + "/" + id);
  }

  public complete(id: number) {
    return this.http.put<Project>(this.projectsUrl + "/complete/" + id ,this.httpOptions);
  }

  public close(id: number) {
    return this.http.put<Project>(this.projectsUrl + "/close/" + id ,this.httpOptions);
  }

  public start(id: number) {
    return this.http.put<Project>(this.projectsUrl + "/start/" + id ,this.httpOptions);
  }

}
