import { Component, OnInit } from '@angular/core';
import { Skill } from '../Classes/skill';
import { GeneralService } from '../Services/general.service';
import { ProjectServiceService } from '../Services/project-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { EngagementService } from '../Services/engagement.service';
import { UserService } from '../Services/user-service.service';
import { SkillService } from '../Services/skill.service';
import { Project } from '../Classes/project';

@Component({
  selector: 'app-skill-form',
  templateUrl: './skill-form.component.html',
  styleUrls: ['./skill-form.component.css']
})
export class SkillFormComponent implements OnInit {

  project: Project;
  skills: Skill[];
  isCreateMode: boolean;
  startDate: string;
  endDate: string;
  actualStartDate: string;
  skill: Skill = {id: 0, name: "", projectName: "", count: null, totalWeeklyHours: null, skillName: "", avgWeeklyEngHours: null }

  constructor(
    private generalService: GeneralService,
    private projectService: ProjectServiceService,
    private route:ActivatedRoute,
    private engagementSerivce: EngagementService,
    private userSerivce: UserService,
    private skillService: SkillService,
    private router: Router
  ) { }

  ngOnInit(): void {
    const projectId = +this.route.snapshot.paramMap.get('projectid');
    if (this.router.url.includes('addEngagement')) {
      const skillid = (+this.route.snapshot.paramMap.get('skillid'));
      this.skillService.getById(skillid).subscribe(resp => {
        this.skill = resp;
        this.skill.count = 0;
        this.skill.avgWeeklyEngHours = 0;
        this.skill.totalWeeklyHours = 0;
        document.querySelectorAll('button')[1].textContent = "Add";
      })
      document.querySelectorAll('select')[1].disabled = true;
      document.querySelectorAll('div')[9].hidden = true;
      document.querySelectorAll('div')[10].hidden = true;
      document.querySelectorAll('div')[11].hidden = true;
    } else if (this.router.url.includes('edit')) {
      const skillid = (+this.route.snapshot.paramMap.get('skillid'));
      this.skillService.getById(skillid).subscribe(resp => {
        this.skill = resp;
      })
      document.querySelectorAll('select')[1].disabled = true;
      document.querySelectorAll('div')[8].hidden = true;
      document.querySelectorAll('div')[12].hidden = true;
      this.engagementSerivce.findAll(projectId).subscribe(data => {
        for (let i = 0; i < data.length; i++) {
          if (data[i]["skillName"] === this.skill.skillName) {
            this.skill.count = (data[i]["engagements"].length)
            this.skill.avgWeeklyEngHours = this.skill.totalWeeklyHours / this.skill.count;
          }
        }
      })
    } else {
      document.querySelectorAll('div')[9].hidden = true;
      document.querySelectorAll('div')[10].hidden = true;
    }
    this.getProjectById(projectId);
    this.getAllSkills();
  }

  onSubmit() {
    if (this.router.url.includes('edit')) {
      let test = {
        "startDate": this.startDate,
        "endDate": this.endDate,
        "newHours": this.skill.totalWeeklyHours
      }
      this.skillService.update(this.skill.id, test).subscribe(d => this.goBack())
    } else if (this.router.url.includes('addEngagement')) {
      let test = {
        "skillName": this.skill.skillName,
        "count": this.skill.count,
        "avgWeeklyEngHours": this.skill.avgWeeklyEngHours
      }
      this.skillService.addEngagements(this.project, test).subscribe(d => this.goBack());
    } else {
      let test = {
        "skillName": this.skill.skillName,
        "avgWeeklySkillHours": this.skill.totalWeeklyHours,
        "count": this.skill.count,
        "avgWeeklyEngHours": this.skill.avgWeeklyEngHours
      };
      this.skillService.create(this.project, test).subscribe(d => this.goBack());
    }
  }

  getProjectById(projectId:number):void {
    this.projectService.getProjectByIdApi(projectId).subscribe(resp=>{
      this.project = resp;
      this.skill.projectName = resp.projectName;
      this.startDate = resp.startDate;
      this.actualStartDate = resp.startDate;
      this.endDate = resp.endDate;
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
