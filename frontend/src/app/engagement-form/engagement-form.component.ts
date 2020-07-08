import { Component, OnInit } from '@angular/core';
import { Project } from '../project';
import { User } from '../user';
import { GeneralService } from '../general.service';
import { ProjectServiceService } from '../project-service.service';
import { ActivatedRoute } from '@angular/router';
import { Engagement } from '../engagement';
import { EngagementService } from '../engagement.service';

@Component({
  selector: 'app-engagement-form',
  templateUrl: './engagement-form.component.html',
  styleUrls: ['./engagement-form.component.css']
})
export class EngagementFormComponent implements OnInit {

  project: Project;
  engagement: Engagement;
  employee: User;
  employeeNames: String[];
  roles: String[];
  employeeName: String;
  isCreateMode: boolean;


  constructor(
    private generalService: GeneralService,
    private projectService: ProjectServiceService,
    private route:ActivatedRoute,
    private engagementSerivce: EngagementService
  ) { }

  ngOnInit(): void {
    const projectId = +this.route.snapshot.paramMap.get('id');
    this.getProjectById(projectId);
    this.getAllRoles();
    const engagementId = +this.route.snapshot.paramMap.get('engagementid');
    if (!engagementId) {
      this.engagement = new Engagement();
      this.isCreateMode = true;
    } else {
      this.engagementSerivce.getEngagementByIdApi(engagementId).subscribe(d=>this.engagement = d);
      this.isCreateMode = false;
    }
  }

  getProjectById(projectId:number):void {
    this.projectService.getProjectByIdApi(projectId).subscribe(resp=>{
      this.project=resp;
    })
  }

  getAllRoles():void{
    this.generalService.getRoles().subscribe(resp=>{
      this.roles = resp;
    })
  }

  onSubmit() {
    if(this.isCreateMode){
      this.engagementSerivce.create(this.engagement).subscribe(result => this.engagementSerivce.gotoProjectView(this.project.id));
    }else{
      this.projectService.edit(this.project).subscribe(result => this.projectService.gotoProjectList());
    }
  }

  goBack(e:any):void{
    this.generalService.goBack();
  }

}
