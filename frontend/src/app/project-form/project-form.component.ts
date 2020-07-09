import { Component, OnInit } from '@angular/core';
import { Project } from '../Classes/project';
import { GeneralService } from '../Services/general.service';
import { ProjectServiceService } from '../Services/project-service.service';
import { ActivatedRoute } from '@angular/router';

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

}
