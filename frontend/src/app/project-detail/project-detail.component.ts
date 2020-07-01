import { Component, OnInit } from '@angular/core';
import { Project } from '../project';
import { GeneralService } from '../general.service';
import { ActivatedRoute } from '@angular/router';
import { ProjectServiceService } from "../project-service.service";

@Component({
  selector: 'app-project-detail',
  templateUrl: './project-detail.component.html',
  styleUrls: ['./project-detail.component.css']
})
export class ProjectDetailComponent implements OnInit {

  selectedProject : Project;
  project: Project;
  projectService: any;

  constructor(
    private generalService: GeneralService,
    private route: ActivatedRoute,
    public projectSerivce : ProjectServiceService
  ) { }

  ngOnInit(): void {
    this.getProjectById();
  }

  goBack(e:any):void {
    this.generalService.goBack();
  }

  getProjectById(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.projectSerivce.getProjectByIdApi(id).subscribe(data=>this.selectedProject = data)
  }
}
