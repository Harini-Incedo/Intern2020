import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectServiceService } from '../Services/project-service.service';
import { EngagementService } from '../Services/engagement.service';
import { GeneralService } from '../Services/general.service';
import { User } from '../Classes/user';
import { UserService } from '../Services/user-service.service';

@Component({
  selector: 'app-edit-hours',
  templateUrl: './edit-hours.component.html',
  styleUrls: ['./edit-hours.component.css']
})
export class EditHoursComponent implements OnInit {

  date: string;
  hours: number;
  employee: User = {id: 0, firstName: "", lastName: "", email: "", role: "", skills: [],
  department: "", startDate: "", endDate: "", location: "", timezone: "", workingHours: "", manager: ""}

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectServiceService,
    private engagementService: EngagementService,
    private generalService: GeneralService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.date = this.route.snapshot.paramMap.get('date');
    this.hours = +this.route.snapshot.paramMap.get('hours');
    this.userService.getUserByIdApi(+this.route.snapshot.paramMap.get('employeeid'))
      .subscribe(resp => {
        this.employee = resp;
      })
  }

  onSubmit() {
    const projectID = +this.route.snapshot.paramMap.get('projectid');
    this.projectService.getProjectByIdApi(projectID).subscribe(resp => {
      let map = new Map();
      map.set(this.date, this.hours);
      let jsonObject = {};
      map.forEach((value, key) => {
          jsonObject[key] = value
      });
      let obj = {
        "id" : +this.route.snapshot.paramMap.get('engagementid'),
        "employeeID": +this.route.snapshot.paramMap.get('employeeid'),
        "projectID": projectID,
        "skillID": +this.route.snapshot.paramMap.get('skillId'),
        "startDate": resp.startDate,
        "endDate": resp.endDate,
        "assignedWeeklyHours": jsonObject
      }
      this.engagementService.edit(+this.route.snapshot.paramMap.get("engagementid"), obj)
        .subscribe(resp => this.goBack());
    })
  }

  goBack() {
    this.generalService.goBack();
  }

}
