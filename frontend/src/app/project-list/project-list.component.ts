import { Component, OnInit } from '@angular/core';
import { Project } from '../project';
import { ProjectServiceService } from '../project-service.service';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit {

  projects: Project[];
  selectedProjects: Project[] ;
  selectedProject: Project ;

  constructor(private projectService: ProjectServiceService) { }

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

}
