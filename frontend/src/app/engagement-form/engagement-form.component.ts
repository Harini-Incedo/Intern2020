import { Component, OnInit } from '@angular/core';
import { Project } from '../Classes/project';
import { User } from '../Classes/user';
import { GeneralService } from '../Services/general.service';
import { ProjectServiceService } from '../Services/project-service.service';
import { ActivatedRoute } from '@angular/router';
import { Engagement } from '../Classes/engagement';
import { EngagementService } from '../Services/engagement.service';
import { UserService } from '../Services/user-service.service';

@Component({
  selector: 'app-engagement-form',
  templateUrl: './engagement-form.component.html',
  styleUrls: ['./engagement-form.component.css']
})
export class EngagementFormComponent implements OnInit {

  project: Project;
  engagement: Engagement = {id:0,employeeID:0, projectID:0, role:"", hoursNeeded:"", startDate:"", endDate:"", projectName:""};
  users: User[];
  selectedUser: User;
  roles: String[];
  employeeName: String;
  isCreateMode: boolean;


  constructor(
    private generalService: GeneralService,
    private projectService: ProjectServiceService,
    private route:ActivatedRoute,
    private engagementSerivce: EngagementService,
    private userSerivce: UserService
  ) { }

  ngOnInit(): void {
    this.userSerivce.findAll("Active").subscribe(data => {
      this.users = data;
    });
    const projectId = +this.route.snapshot.paramMap.get('id');
    this.getProjectById(projectId);
    this.getAllRoles();
    const engagementId = +this.route.snapshot.paramMap.get('engagementid');
    if (!engagementId) {
      this.engagement = new Engagement();
      this.engagement.projectID = projectId;
      this.isCreateMode = true;
    } else {
      this.engagementSerivce.getEngagementByIdApi(engagementId).subscribe(d=>this.engagement = d["engagement"]);
      this.isCreateMode = false;
    }
  }

  getProjectById(projectId:number):void {
    this.projectService.getProjectByIdApi(projectId).subscribe(resp=>{
      this.project=resp;
    })
  }

  onSelectOne(user:User){
    this.userSerivce.setSelectedUser(user);
  }

  getAllRoles():void{
    this.generalService.getRoles().subscribe(resp=>{
      this.roles = resp;
    })
  }

  onSubmit() {
    if(this.isCreateMode){
      this.engagement.employeeID = +this.engagement.employeeID;
      this.engagementSerivce.create(this.engagement).subscribe(result => this.goBack());
    }else{
      this.engagementSerivce.edit(this.engagement).subscribe(result => this.goBack());
    }
  }

  goBack():void{
    this.generalService.goBack();
  }

  deleteEngagement(engagement:Engagement) {
    this.engagementSerivce.delete(engagement.id).subscribe(d=>this.engagementSerivce.gotoProjectView(this.project.id));
    window.location.reload();
  }

  printForbiddenMessage(value:number){
    if(value<1){
      return "value is less than 1";
    }else if(value>100){
      return "value is greater than 100";
    }
  }
}
