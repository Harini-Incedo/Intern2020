import { Component, OnInit } from '@angular/core';
import { Project } from '../project';
import { ProjectServiceService } from '../project-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit {

  projects: Project[];
  selectedProjects: Project[] ;
  selectedProject: Project ;

  constructor(private projectService: ProjectServiceService, private router: Router) { }

  ngOnInit(): void {
    this.projectService.findAll().subscribe(data => {
      this.projects = data;
      this.projectService.setProjects(data);
      this.selectedProjects = [];
    });
  }

  onSelectOne(project:Project){
    this.projectService.setSelectedProject(project);
  }

  addSelectedProject(project:Project) : void {
    this.selectedProjects.push(project);
  }

  onSelectList(project:Project){
    if(this.selectedProjects && this.selectedProjects.length>0){
      let isInTheList : boolean = false;
      isInTheList = this.selectedProjects.find(su=>su.id === project.id) === undefined ? false : true;
      if(isInTheList){
        this.selectedProjects = this.selectedProjects.filter(d=>d.id !==project.id);
      }else {
        this.selectedProjects.push(project);
      }
    }else{
      this.selectedProjects.push(project);
    }
  }

  completeProject(project:Project) {
    this.projectService.complete(+project.id).subscribe(d=>this.projectService.gotoProjectList());
    window.location.reload();
  }

  
  startProject(project:Project) {
    this.projectService.start(+project.id).subscribe(d=>this.projectService.gotoProjectList());
    window.location.reload();
  }

  closeProject(project:Project) {
    this.projectService.close(+project.id).subscribe(d=>this.projectService.gotoProjectList());
    window.location.reload();
  }

  editProject(project:Project) : void {
    this.router.navigateByUrl(`/editProject/${project.id}`);
  }

}
