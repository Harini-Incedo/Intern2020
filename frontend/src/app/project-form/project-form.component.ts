import { Component, OnInit } from '@angular/core';
import { Project } from '../project';
import { GeneralService } from '../general.service';
import { ProjectServiceService } from '../project-service.service';

@Component({
  selector: 'app-project-form',
  templateUrl: './project-form.component.html',
  styleUrls: ['./project-form.component.css']
})
export class ProjectFormComponent implements OnInit {

  project: Project;
  departments: String[];
  statuses: String[];
  isCreateMode: boolean;

  constructor(
    private generalService: GeneralService,
    private projectService: ProjectServiceService) {
  }

  ngOnInit(): void {
    this.getAllDepartments();
  }

  getAllDepartments():void{
    this.generalService.getDepartments().subscribe(resp=>{
      this.departments = resp;
    })
  }

  onSubmit() {
    if(this.isCreateMode){
      this.projectService.create(this.project).subscribe(result => this.projectService.gotoProjectList());
    }else{
      this.projectService.edit(this.project).subscribe(result => this.projectService.gotoProjectList());
    }
  }

  goBack(e:any):void{
    this.generalService.goBack();
  }

}
