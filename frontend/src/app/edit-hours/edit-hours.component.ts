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

  startDate: string;
  endDate: string;
  hours: number;
  employee: User = {id: 0, firstName: "", lastName: "", email: "", role: "", skills: [],
  department: "", startDate: "", endDate: "", location: "", timezone: "", workingHours: "", manager: "",
  active: true}

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectServiceService,
    private engagementService: EngagementService,
    private generalService: GeneralService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    const engagementID = +this.route.snapshot.paramMap.get('engagementid')
    this.engagementService.getEngagementByIdApi(engagementID).subscribe(resp => {
      this.startDate = resp["engagement"]["startDate"]
      this.endDate = resp["engagement"]["endDate"]
      if (resp["engagement"].employeeID !== 0) {
        this.userService.getUserByIdApi(resp["engagement"].employeeID).subscribe(resp => {
          this.employee = resp;
        })
      } else {
        this.employee.firstName = "Pending"
        this.employee.lastName = "Employee"
      }
    })
  }

  onSubmit() {
    const engagementID = +this.route.snapshot.paramMap.get('engagementid')
    //Refactor here ->
    let test = {
      "startDate": document.querySelectorAll('input')[0].value,
      "weekCount": Number(document.querySelectorAll('input')[1].value),
      "newHours": Number(document.querySelectorAll('input')[2].value)
    }
    this.engagementService.massUpdateHours(engagementID, test).subscribe(resp => this.goBack())
  }

  goBack() {
    this.generalService.goBack();
  }

}
