import { Component, OnInit } from '@angular/core';
import { GeneralService } from '../Services/general.service';
import { UserService } from '../Services/user-service.service';
import { Skill } from '../Classes/skill';
import { ActivatedRoute } from '@angular/router';
import { Project } from '../Classes/project';
import { ProjectServiceService } from '../Services/project-service.service';
import { User } from '../Classes/user';
import { Employee } from '../Classes/employee';
import { SkillService } from '../Services/skill.service';
import { sample } from 'rxjs/operators';
import { EngagementService } from '../Services/engagement.service';

@Component({
  selector: 'app-employee-skill-form',
  templateUrl: './employee-skill-form.component.html',
  styleUrls: ['./employee-skill-form.component.css']
})
export class EmployeeSkillFormComponent implements OnInit {

  skills: Skill[];
  sampleMap: {};
  users: User[];
  project: Project;
  skill: Skill = {id: 0, name: "", projectName: "", count: null, totalWeeklyHours: 0, skillName: "", avgWeeklyEngHours: 0}
  employee: Employee = {id: 0, skillName: "", employeeID: null, projectName: "", avgWeeklyHours: null, week: [], hours: [], billable: false}

  constructor(
    private generalService: GeneralService,
    private userService: UserService,
    private projectService: ProjectServiceService,
    private route: ActivatedRoute,
    private skillService: SkillService,
    private engagementService: EngagementService
  ) { }

  ngOnInit(): void {
    this.getSkills();
    const projectid = +this.route.snapshot.paramMap.get('projectid');
    this.getProjectById(projectid);
    const skillid = +this.route.snapshot.paramMap.get('skillName');
    this.skillService.getById(skillid).subscribe(data=> {
      this.sampleMap["skill"] = data.skillName;
      this.skill["skill"] = data.skillName;
      this.skill = data;
      this.userService.findRecommended(this.sampleMap).subscribe(data => {
        this.users = data;
      });
    })
  }

  onSubmit() {
    const projectid = +this.route.snapshot.paramMap.get('projectid');
    const skillid = +this.route.snapshot.paramMap.get('skillName');
    this.engagementService.findAll(projectid).subscribe(resp=>{
      for (let x = 0; x < resp.length; x++) {
        if (resp[x].skillName === this.skill.skillName) {
          if (resp[x]["engagements"][0]["engagement"].skillID === skillid) {
            for (let i = 0; i < resp[x]["engagements"].length; i++) {
              if (resp[x]["engagements"][i]["employee"] === null) {
                let response = {
                  "id": resp[x]["engagements"][i]["engagement"]["id"],
                  "employeeID": this.employee.employeeID,
                  "projectID": projectid,
                  "skillID": skillid,
                  "startDate": this.project.startDate,
                  "endDate": this.project.endDate,
                  "assignedWeeklyHours": resp[x]["engagements"][i]["engagement"]["assignedWeeklyHours"],
                  "billable": document.querySelector('input').checked
                }
                this.engagementService.edit(resp[x]["engagements"][i]["engagement"]["id"], response).subscribe(result => this.goBack());
                break;
              }
            }
          }
        }
      }
    })
  }

  getSkills():void {
    this.userService.getSkills().subscribe(resp=> {
      this.skills = resp;
    })
  }

  getProjectById(id:number):void{
    this.projectService.getProjectByIdApi(id).subscribe(resp=>{
      this.project = resp;
      this.employee.projectName = resp.projectName;
      if (resp) {
        this.sampleMap = {
          "startDate" : resp.startDate,
          "endDate" : resp.endDate
        }
      }
    })
  }

  goBack():void{
    this.generalService.goBack();
  }

  printForbiddenMessage(value:number | any ){
    return this.generalService.printForbiddenMessage(value,"engagement");
  }

}
