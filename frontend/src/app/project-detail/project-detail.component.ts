import { Component, OnInit } from '@angular/core';
import { Project } from '../Classes/project';
import { GeneralService } from '../Services/general.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectServiceService } from '../Services/project-service.service';
import { EngagementService } from '../Services/engagement.service';
import { Engagement } from '../Classes/engagement';
import { UserService } from '../Services/user-service.service';
import { User } from '../Classes/user';
import { Skill } from '../Classes/skill';
import * as d3 from 'd3';
import * as $ from 'jquery';
import { SkillService } from '../Services/skill.service';

@Component({
  selector: 'app-project-detail',
  templateUrl: './project-detail.component.html',
  styleUrls: ['./project-detail.component.css']
})
export class ProjectDetailComponent implements OnInit {

  dateTimeFormat : any = new Intl.DateTimeFormat('en', {  month: 'short', day: '2-digit' })
  newD : any = new Intl.DateTimeFormat('en-US')
  selectedProject : Project;
  employee : User;
  skill: Skill;
  selectedSkill: Skill;
  selectedSkills: Skill[];
  skills: Skill[];
  project: Project;
  engagements: Engagement[];
  selectedEngagements: Engagement[];
  selectedEngagement: Engagement;
  disableEdit: boolean = true;

  constructor(
    private generalService: GeneralService,
    private route: ActivatedRoute,
    private projectSerivce : ProjectServiceService,
    private router : Router,
    private engagementSerivce: EngagementService,
    private skillService: SkillService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.selectedEngagements = [];
    this.selectedSkills = [];
    this.getProjectById();
    setTimeout(()=> {
      $('table.paginated').each(function() {
        let currentPage = 0;
        let numPerPage = 15;
        let $table = $('table.paginated');
        $table.bind('repaginate', function() {
          $table.find('td.dates').hide().slice(currentPage * numPerPage, (currentPage + 1) * numPerPage).show();
          let length = $("th[class*='header']").length;
          for (let i = 0; i < length; i++) {
            const className =  $("th[class*='header']")[i].innerText.trim().replace(' ', '-');
            let $test = $(`tr[class*= employee-${className}]`);
            let $dates = $(`td[class*= workingHours${className}]`);
            let $skillClass = $(`td[class*= hoursForSkill${className}]`);
            let length2 = $(`tr[class*= employee-${className}]`).length;
            for (let x = 0; x < length2; x++) {
              let arr = Array.from($($test[x]).children())
              arr.shift();
              $(arr).hide().slice(currentPage * numPerPage, (currentPage + 1) * numPerPage).show();
            }
            $skillClass.hide().slice(currentPage * numPerPage, (currentPage + 1) * numPerPage).show();
            $dates.hide().slice(currentPage * numPerPage, (currentPage + 1) * numPerPage).show();
          }
        });
        $table.trigger('repaginate');
        let numRows = $table.find('td.dates').length;
        let numPages = Math.ceil(numRows / numPerPage);
        let $pager = $('<div class="pager"></div>');
        for (let page = 0; page < numPages; page++) {
            $('<span class="page-number"></span>').text(page + 1).bind('click', {
                newPage: page
            }, function(event) {
                currentPage = event.data['newPage'];
                $table.trigger('repaginate');
                $(this).addClass('active').siblings().removeClass('active');
            }).appendTo($pager).addClass('clickable');
        }
        $pager.insertAfter($table).find('span.page-number:first').addClass('active');
    });
    }, 1000)
    setTimeout(() => {
      d3.selectAll('div.pager span').attr('style', 'display:inline-block; width: 1.8em; height: 1.8em; line-height: 1.8; text-align: center; cursor: pointer; background: #000; color: #fff; margin-right: 0.5em;');
      d3.selectAll('div.pager').attr('style', 'margin-left: 1em; margin-bottom: 1em;')
    }, 1010);
  }

  goBack(e:any):void {
    this.generalService.goBack();
  }

  getProjectById(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.projectSerivce.getProjectByIdApi(id).subscribe(data=>{
      this.selectedProject = data;
      if (this.selectedProject) {
        this.skills = [];
        this.engagementSerivce.findAll(id).subscribe(data => {
          for (let i = 0; i < data.length; i++) {
            this.skills.push(data[i]);
          }
          let dates = d3.timeMondays(new Date(this.selectedProject.startDate), new Date(this.selectedProject.endDate));
          if (this.skills.length > 0) {
            for (let index = 0; index < dates.length; index++) {
              let td = d3.selectAll('tr.first').insert('td');
              td.attr('class', 'dates');
              td.text(this.dateTimeFormat.format(dates[index]));
              td.attr('style', 'width: 3em')
            }
          }
          for (let i = 0; i < this.skills.length; i++) {
            let map = new Map();
            let map2 = new Map();
            let tr = d3.selectAll('table').insert('tr').attr('class', 'dates');
            tr.attr('style', 'background-color:#B7E9F7;')
            let th = tr.insert('th');
            th.attr('style', 'width: 11em')
            th.attr("class", "header");
            let skillAssignedWeeklyHours = this.skills[i]["assignedWeeklyHours"];
            let weekDates = Object.keys(skillAssignedWeeklyHours);
            for (let date = 0; date < dates.length; date++) {
              for (let w = 0; w < weekDates.length; w++) {
                if (this.convertDateToUTC(new Date(weekDates[w])).getTime() === dates[date].getTime()) {
                  let td = tr.insert('td');
                  td.text((Object.values(skillAssignedWeeklyHours)[w]).toString());
                  map2.set(dates[date], (Object.values(skillAssignedWeeklyHours)[w]));
                  td.attr('class', 'hoursForSkill' + this.skills[i].skillName.replace(' ', '-'))
                }
              }
            }
            th.append('input').attr('type', 'checkbox')
              .on('change', ()=> {
                this.onSelectSkillList(this.skills[i]);
              });
            th.append('span').text(' ');
            let span = th.append('span').text(this.skills[i]["skillName"]);
            span.attr('style', 'font-size: 13pt;')
            th.attr('colspan', 3);
            for (let b = 0; b < this.skills[i]["engagements"].length; b++) {
              let tr2 = d3.selectAll('table').insert('tr');
              let td = tr2.insert('td');
              if (this.skills[i]["engagements"][b]["employee"] === null) {
                td.append('input').attr('type', 'checkbox')
                  .on('click', () => {
                    this.onSelectList(this.skills[i]["engagements"][b]["engagement"], d3.event.path[1].textContent);
                  });
                td.append('span').text(' ');
                let a = td.append('a').text("Pending Employee");
                a.attr('colspan', 3);
                a.attr('ng-reflect-router-link', '/addEmployee/'+ this.skills[i]["engagements"][b]["engagement"]["projectID"] +'/skill/' + this.skills[i]["engagements"][b]["engagement"]["skillID"]);
                a.attr('href', '/addEmployee/'+ this.skills[i]["engagements"][b]["engagement"]["projectID"] +'/skill/' + this.skills[i]["engagements"][b]["engagement"]["skillID"]);
                td.attr('colspan', 3);
                tr2.attr('style', 'background-color: #FFF7E4')
                for (let x = 0; x < dates.length; x++) {
                  let assignedWeeklyHours = new Array(this.skills[i]["engagements"][b]["engagement"]["assignedWeeklyHours"]);
                  let vals = Object.values(this.skills[i]["engagements"][b]["engagement"]["assignedWeeklyHours"]);
                  let td = tr2.insert('td').text(+vals[x]);
                  td.attr('class', 'anotherClass' + this.skills[i].skillName.replace(' ', '-'))
                  // td.on('click', ()=>{
                  //   this.editWorkingHours(dates[x].toLocaleDateString('zh-Hans-CN', {year:'numeric', month:"2-digit", day:"2-digit"}), Number(Object.keys(assignedWeeklyHours).toString()),
                  //   this.skills[i]["engagements"][b]["engagement"]);
                  // });
                  tr2.attr("class", "employee-" + this.skills[i].skillName.replace(' ', '-'));
                }
              }
              if (this.skills[i]["engagements"][b]["employee"] !== null) {
                td.append('input').attr('type', 'checkbox')
                .on('click', () => {
                  this.onSelectList(this.skills[i]["engagements"][b]["engagement"], d3.event.path[1].textContent);
                });
                td.append('span').text('    ');
                let a = td.append('a').text(' ' + this.skills[i]["engagements"][b]["employee"]["firstName"] + " " + this.skills[i]["engagements"][b]["employee"]["lastName"]);
                a.attr('ng-reflect-router-link', '/viewEmployee/' + this.skills[i]["engagements"][b]["employee"]["id"]);
                a.attr('href', '/viewEmployee/' + this.skills[i]["engagements"][b]["employee"]["id"]);
                td.attr("class", "employee-" + this.skills[i].skillName.replace(' ', '-'));
                td.attr("colspan", 3);
                if (!this.skills[i]["engagements"][b]["engagement"]["billable"]) {
                  td.append('span').text('*').attr('style', 'color:red');
                }
                for (let x = 0; x < dates.length; x++) {
                  let assignedWeeklyHours = new Array(this.skills[i]["engagements"][b]["engagement"]["assignedWeeklyHours"]);
                  let td = tr2.insert('td');
                  if (this.skills[i]["engagements"][b]["engagement"]["employee"] === null) {
                    td.text(0);
                    if (map.has(dates[x])) {
                      map.set(dates[x], map.get(dates[x]) + 0);
                    } else {
                      map.set(dates[x], 0);
                    }
                  } else {
                    for (let w = 0; w < Object.keys(assignedWeeklyHours[0]).length; w++) {
                      if (this.convertDateToUTC(new Date(Object.keys(assignedWeeklyHours[0])[w])).getTime() === dates[x].getTime()) {
                        td.text((Object.values(assignedWeeklyHours[0])[w]).toString());
                        if (map.has(dates[x])) {
                          map.set(dates[x], map.get(dates[x]) + (Object.values(assignedWeeklyHours[0])[w]));
                        } else {
                          map.set(dates[x], (Object.values(assignedWeeklyHours[0])[w]));
                        }
                        td.attr('class', 'anotherClass' + this.skills[i].skillName.replace(' ', '-'))
                        // td.on('click', ()=>{
                        //   this.editWorkingHours(dates[x].toLocaleDateString('zh-Hans-CN',
                        //    {year:'numeric', month:"2-digit", day:"2-digit"}),
                        //     Number((Object.values(assignedWeeklyHours[0])[w]).toString()),
                        //     this.skills[i]["engagements"][b]["engagement"]);
                        // });
                      }
                    }
                  }
                  tr2.attr("class", "employee-" + this.skills[i].skillName.replace(' ', '-'));
                }
              }
            }
            let jsonObject2 = {};
            let jsonObject = {};
            map.forEach((value, key) => {
                jsonObject[key] = value
            });
            map2.forEach((value, key) => {
              jsonObject2[key] = value;
            })
            let tr4 = d3.selectAll('table').insert('tr').attr('class', 'dates');
            tr4.insert('td').attr('colspan', 3).append('strong').text('Total');
            tr4.on("click", ()=>{
              $('.employee-' + this.skills[i].skillName.replace(' ', '-')).slideToggle(0, function(){
              });
            });
            for (let w = 0; w < Object.keys(jsonObject).length; w++) {
              let td1 = tr4.insert('td').text(Object.values(jsonObject)[w].toString() + '/' + Object.values(jsonObject2)[w]).attr('class', 'workingHours' + this.skills[i].skillName.replace(' ', '-'));
              if (Number(Object.values(jsonObject)[w])/Number(Object.values(jsonObject2)[w]) > 1) {
                td1.attr('style', 'background-color:#98FB98;');
              } else if (Number(Object.values(jsonObject)[w])/Number(Object.values(jsonObject2)[w]) < 1) {
                td1.attr('style', 'background-color:#FF6961;');
              }
            }
            span.on("click", ()=>{
              $('.employee-' + this.skills[i].skillName.replace(' ', '-')).slideToggle(0, function(){
              });
            });
            d3.selectAll('table').append('br');
          }
        });

      }
    });
  }

  convertDateToUTC(date) {
    return new Date(date.getUTCFullYear(), date.getUTCMonth(), date.getUTCDate(), date.getUTCHours(), date.getUTCMinutes(), date.getUTCSeconds());
  }

  completeProject(project:Project) {
    let check = confirm("Are you sure you want to complete " + project.projectName + "?");
    if (check) {
      this.projectSerivce.complete(+project.id).subscribe(d=>this.projectSerivce.gotoProjectList());
    }
  }

  editWorkingHours(obj: string, obj2: number, obj3: Object) {
    obj = obj.replace('/', '-');
    obj = obj.replace('/', '-');
    this.engagementSerivce.navigateToEditHoursForm(obj, obj2, obj3);
  }

  editSkill(skill: Skill) {
    this.router.navigateByUrl(`editSkill/${this.selectedProject.id}/skill/${this.selectedSkills[0]["engagements"][0]["engagement"]["skillID"]}`);
  }

  deleteSkill(skill: Skill) {
    let check = confirm("Are you sure you would like to delete the " + this.selectedSkills[0].skillName + " skill?");
    if (check) {
      this.skillService.delete(this.selectedSkills[0]["engagements"][0]["engagement"]["skillID"]).subscribe(resp => window.location.reload());
    }
  }

  closeProject(project:Project) {
    let check = confirm("Are you sure you want to close " + project.projectName + "?");
    if (check) {
      this.projectSerivce.close(+project.id).subscribe(d=>this.projectSerivce.gotoProjectList());
    }
  }

  startProject(project:Project) {
    let check = confirm("Are you sure you want to start " + project.projectName + "?");
    if (check) {
      this.projectSerivce.start(+project.id).subscribe(d=>this.projectSerivce.gotoProjectList());
    }
  }

  editProject(project:Project) : void {
    this.router.navigateByUrl(`/editProject/${project.id}`);
  }

  // addEngagement(project:Project):void{
  //   this.router.navigateByUrl(`/addEngagement/${project.id}`);
  // }

  addSelectedEngagement(engagement:Engagement) : void {
    this.selectedEngagements.push(engagement);
  }

  // onSelectOne(engagement:Engagement) {
  //   if (this.selectedEngagement && engagement.id === this.selectedEngagement.id) {
  //     this.selectedEngagement = null;
  //   } else {
  //     this.selectedEngagement = engagement;
  //     this.engagementSerivce.setSelectedEngagement(engagement);
  //   }
  // }

  onSelectList (engagement:Engagement, name:string) {
    if(this.selectedEngagements && this.selectedEngagements.length>0){
      let isInTheList : boolean = false;
      isInTheList = this.selectedEngagements.find(su=>su.id === engagement.id) === undefined ? false : true;
      if (isInTheList) {
        this.selectedEngagements = this.selectedEngagements.filter(d=>d.id !==engagement.id);
      } else {
        this.selectedEngagements.push(engagement);
      }
    } else {
      if (name !== " Pending Employee") {
        console.log('test');
        this.disableEdit = false;
      } else {
        console.log('success');
        this.disableEdit = true;
      }
      this.selectedEngagements.push(engagement);
    }
  }

  checkEmployeeID(engagements: Engagement[]) {
    if(engagements[0]){
      console.log(engagements[0].employeeID !== 0);
      if (engagements[0].employeeID !== 0) {
        return false;
      } else {
        return true;
      }
    }
    return true;
  }

  onSelectSkillList(skill:Skill) {
    if(this.selectedSkills && this.selectedSkills.length>0){
      let isInTheList : boolean = false;
      isInTheList = this.selectedSkills.find(su=>su.id === skill.id) === undefined ? false : true;
      if(isInTheList){
        this.selectedSkills = this.selectedSkills.filter(d=>d.id !==skill.id);
      }else {
        this.selectedSkills.push(skill);
      }
    }else{
      this.selectedSkills.push(skill);
    }
  }

  editEngagement(engagement:Engagement) : void {
    if (this.disableEdit) {
      alert("You may not edit a pending employee's hours!");
    } else {
      this.router.navigateByUrl(`/editEngagement/${this.selectedEngagements[0].id}`);
    }
  }

  checkID() : void {
    console.log(this.selectedEngagements[0]);
  }

  addEngagement(skill: Skill) : void {
    this.router.navigateByUrl(`addEngagement/${this.selectedSkills[0]["engagements"][0]["engagement"].skillID}/project/${this.selectedProject.id}`);
  }

  deleteEngagement(engagement:Engagement) : void {
    if (this.selectedEngagements[0]["employeeID"]) {
      this.userService.getUserByIdApi(this.selectedEngagements[0]["employeeID"]).subscribe(resp => {
        let check = confirm("Are you sure you want to delete " + resp.firstName+ "'s engagement from " + this.selectedProject.projectName + "?");
        if (check) {
          this.engagementSerivce.delete(this.selectedEngagements[0]["id"]).subscribe(d=>window.location.reload());
        }
      })
    } else {
      let check = confirm("Are you sure you want to delete this engagement from " + this.selectedProject.projectName + "?");
      if (check) {
        this.engagementSerivce.delete(this.selectedEngagements[0]["id"]).subscribe(d=>window.location.reload());
      }
    }
  }

  addSkill(project:Project):void {
    this.router.navigateByUrl(`/addSkill/${project.id}`);
  }

  addEmployee(project:Project, skill: any):void {
    this.router.navigateByUrl(`/addEmployee/${project.id}/skill/${skill.skillID}`);
  }

}
