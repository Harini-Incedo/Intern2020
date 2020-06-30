import { Component, OnInit } from '@angular/core';
import { UserService } from '../user-service.service';
import { Project } from '../project';

@Component({
  selector: 'app-project-form',
  templateUrl: './project-form.component.html',
  styleUrls: ['./project-form.component.css']
})
export class ProjectFormComponent implements OnInit {

  project: Project;
  departments: String[];

  constructor(
    private userService: UserService) {
  }

  ngOnInit(): void {
    this.getAllDepartments();
  }

  getAllDepartments():void{
    this.userService.getDepartments().subscribe(resp=>{
      this.departments = resp;
    })
  }

  goBack(e:any):void{
    this.userService.goBack();
  }

}
