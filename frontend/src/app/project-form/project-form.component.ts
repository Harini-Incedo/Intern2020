import { Component, OnInit } from '@angular/core';
import { Project } from '../Classes/project';
import { GeneralService } from '../Services/general.service';
import { ProjectServiceService } from '../Services/project-service.service';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-project-form',
  templateUrl: './project-form.component.html',
  styleUrls: ['./project-form.component.css']
})
export class ProjectFormComponent implements OnInit {

  project: Project = {id:0, projectName:"", status:"", clientName:"", department:"", teamSize:0, projectGoal:"", startDate:"", endDate:"", weeklyHours:0};;
  departments: String[];
  statuses: String[];
  isCreateMode: boolean;

  constructor(
    private generalService: GeneralService,
    private projectService: ProjectServiceService,
    private route:ActivatedRoute) {
  }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.getAllDepartments();
    if (!id) {
      this.project = new Project();
      this.isCreateMode = true;
    } else {
      this.projectService.getProjectByIdApi(id).subscribe(d=>this.project = d);
      this.isCreateMode = false;
    }
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

  printForbiddenMessageWeeklyHours(value:number){
    if(value<1){
      return "is less than 1, the smallest processable value.";
    }else if(value>1000){
      return "is greater than 1000, the largest processable value.";
    }
  }

  private handleError(errorResponse: HttpErrorResponse) {
    let check = confirm(errorResponse.error.errorMessage + ". " + errorResponse.error.debugMessage);
    return throwError(errorResponse);
  }
}