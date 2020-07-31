import { Component, OnInit } from '@angular/core';
import { Skill } from '../Classes/skill';
import { GeneralService } from '../Services/general.service';
import { ProjectServiceService } from '../Services/project-service.service';
import { ActivatedRoute } from '@angular/router';
import { EngagementService } from '../Services/engagement.service';
import { UserService } from '../Services/user-service.service';
import { subscribeOn } from 'rxjs/operators';
import { SkillService } from '../Services/skill.service';
import { Project } from '../Classes/project';

@Component({
  selector: 'app-skill-form',
  templateUrl: './skill-form.component.html',
  styleUrls: ['./skill-form.component.css']
})
export class SkillFormComponent implements OnInit {

  project: Project;
  skills: string[];
  isCreateMode: boolean;
  skill: Skill = {id: 0, name: "", projectName: "", count: null, totalWeeklyHours: null, skillName: "" }

  constructor(
    private generalService: GeneralService,
    private projectService: ProjectServiceService,
    private route:ActivatedRoute,
    private engagementSerivce: EngagementService,
    private userSerivce: UserService,
    private skillService: SkillService
  ) { }

  ngOnInit(): void {
    const projectId = +this.route.snapshot.paramMap.get('projectid');
    this.getProjectById(projectId);
    this.getAllSkills();
  }

  onSubmit() {
    let test = {
      "skillName": this.skill.skillName,
      "totalWeeklyHours": this.skill.totalWeeklyHours,
      "count": this.skill.count
    };
    this.skillService.create(this.project, test).subscribe(d => this.goBack());
  }

  getProjectById(projectId:number):void {
    this.projectService.getProjectByIdApi(projectId).subscribe(resp=>{
      this.project = resp;
      this.skill.projectName = resp.projectName;
    })
  }

  getAllSkills():void {
    this.userSerivce.getSkills().subscribe(resp=>{
      this.skills = resp;
    })
  }

  goBack():void{
    this.generalService.goBack();
  }

  printForbiddenMessage(value:number | any ){
    return this.generalService.printForbiddenMessage(value,"engagement");
  }

  // onSubmit() {
  //   this.engagementSerivce.create(this.skill).subscribe(result => this.goBack());
  // }

}
