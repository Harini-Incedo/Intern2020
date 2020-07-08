import { Component, OnInit } from '@angular/core';
import { Project } from '../project';
import { GeneralService } from '../general.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectServiceService } from '../project-service.service';
import { EngagementService } from '../engagement.service';
import { Engagement } from '../engagement';
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
    this.engagementSerivce.findAll(+this.route.snapshot.paramMap.get('id')).subscribe(data => {
      for (let index = 0; index < data.length; index++) {
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

  onSelectOne(engagement:Engagement){
    this.selectedEngagement = engagement;
    this.engagementSerivce.setSelectedEngagement(engagement);
  }

  editEngagement(project:Project, engagement:Engagement) : void {
    this.router.navigateByUrl(`/viewProject/${project.id}/editEngagement/${engagement.id}`);
  }


}
