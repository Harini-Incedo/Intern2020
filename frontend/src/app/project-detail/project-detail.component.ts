import { Component, OnInit } from '@angular/core';
import { Project } from '../Classes/project';
import { GeneralService } from '../Services/general.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectServiceService } from '../Services/project-service.service';
import { EngagementService } from '../Services/engagement.service';
import { Engagement } from '../Classes/engagement';
import { newArray } from '@angular/compiler/src/util';

@Component({
  selector: 'app-project-detail',
  templateUrl: './project-detail.component.html',
  styleUrls: ['./project-detail.component.css']
})
export class ProjectDetailComponent implements OnInit {

  selectedProject : Project;
  project: Project;
  engagements: Engagement[];
  selectedEngagements: Engagement[];
  selectedEngagement: Engagement;

  constructor(
    private generalService: GeneralService,
    private route: ActivatedRoute,
    private projectSerivce : ProjectServiceService,
    private router : Router,
    private engagementSerivce: EngagementService
  ) { }

  ngOnInit(): void {
    this.getProjectById();
    this.engagements = [];
    this.selectedEngagements = [];
    this.engagementSerivce.findAll(+this.route.snapshot.paramMap.get('id')).subscribe(data => {
      for (let index = 0; index < data.length; index++) {
        data[index]["engagement"]["firstName"] = data[index]["employee"]["firstName"];
        data[index]["engagement"]["lastName"] = data[index]["employee"]["lastName"];
        this.engagements.push(data[index]['engagement']);
      }
    });
  }

  goBack(e:any):void {
    this.generalService.goBack();
  }

  getProjectById(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.projectSerivce.getProjectByIdApi(id).subscribe(data=>this.selectedProject = data)
  }

  completeProject(project:Project) {
    this.projectSerivce.complete(+project.id).subscribe(d=>this.projectSerivce.gotoProjectList());
  }

  closeProject(project:Project) {
    this.projectSerivce.close(+project.id).subscribe(d=>this.projectSerivce.gotoProjectList());
  }

  startProject(project:Project) {
    this.projectSerivce.start(+project.id).subscribe(d=>this.projectSerivce.gotoProjectList());
  }

  editProject(project:Project) : void {
    this.router.navigateByUrl(`/editProject/${project.id}`);
  }

  addEngagement(project:Project):void{
    this.router.navigateByUrl(`/addEngagement/${project.id}`);
  }

  addSelectedEngagement(engagement:Engagement) : void {
    this.selectedEngagements.push(engagement);
  }

  onSelectOne(engagement:Engagement) {
    if (this.selectedEngagement && engagement.id === this.selectedEngagement.id) {
      this.selectedEngagement = null;
    } else {
      this.selectedEngagement = engagement;
      this.engagementSerivce.setSelectedEngagement(engagement);
    }
  }

  onSelectList(engagement:Engagement){
    if(this.selectedEngagements && this.selectedEngagements.length>0){
      let isInTheList : boolean = false;
      isInTheList = this.selectedEngagements.find(su=>su.id === engagement.id) === undefined ? false : true;
      if(isInTheList){
        this.selectedEngagements = this.selectedEngagements.filter(d=>d.id !==engagement.id);
      }else {
        this.selectedEngagements.push(engagement);
      }
    }else{
      this.selectedEngagements.push(engagement);
    }
  }

  editEngagement(project:Project, engagement:Engagement) : void {
    this.router.navigateByUrl(`/viewProject/${project.id}/editEngagement/${engagement.id}`);
  }

  deleteEngagement( engagement:Engagement) : void {
    this.engagementSerivce.delete(+engagement.id).subscribe(d=>   window.location.reload());
  }


}
