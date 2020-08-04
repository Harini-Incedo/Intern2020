import { Component, OnInit, Input } from '@angular/core';
import { User } from '../Classes/user';
import { ActivatedRoute } from '@angular/router';
import {UserService} from '../Services/user-service.service';
import { GeneralService } from '../Services/general.service';
import { Engagement } from '../Classes/engagement';
import { Project } from '../Classes/project';
@Component({
  selector: 'app-view-page',
  templateUrl: './view-page.component.html',
  styleUrls: ['./view-page.component.css']
})
export class ViewPageComponent implements OnInit {

  selectedUser : User = {id: 0, firstName: "", lastName: "", email: "", role: "", skills:[],
 department: "", startDate:"", endDate:"", location:"", timezone:"", workingHours:"", manager:""};
  user: User;
  projects: Project[];
  engagements: Engagement[];
  skills: String[];

  constructor( private route: ActivatedRoute, public userService : UserService, public generalService : GeneralService) {

  }

  ngOnInit(): void {
    this.getUserById();
  }

  getUserById(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.userService.getUserByIdApi(id).subscribe(data=>{
      this.selectedUser = data;
      if (this.selectedUser) {
        this.getEngagementsByEmployee();
      }
    });
  }

  deleteUser(user:User) : void {
    let check = confirm("Are you sure you want to delete " + user.firstName + " " + user.lastName + " as an employee?");
    if (check) {
      this.userService.delete(+user.id).subscribe(d=>this.userService.gotoUserList());
    }
  }

  goBack():void {
    this.generalService.goBack();
  }

  email(user:User) : void {
    window.location.href = "mailto:" + user.email;
  }

  getEngagementsByEmployee():void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.projects = [];
    this.engagements = [];
    this.userService.getEngagementByUser(id).subscribe(data=>{
      for (let index = 0; index < data.length; index++) {
        this.userService.getSkillByID(data[index]["engagement"]["skillID"])
          .subscribe(data1 => {
            data[index]["engagement"]["skillName"] = data1["skillName"];
          })
        data[index]["engagement"]["projectName"] = data[index]['project']['projectName'];
        this.engagements.push(data[index]['engagement']);
        this.engagements.push();
      }
    });
  }

}
