import { Component, OnInit } from '@angular/core';
import { Project } from '../project';
import { UserService } from '../user-service.service';

@Component({
  selector: 'app-project-detail',
  templateUrl: './project-detail.component.html',
  styleUrls: ['./project-detail.component.css']
})
export class ProjectDetailComponent implements OnInit {

  selectedProject : Project;
  project: Project;

  constructor(
    private userService: UserService
  ) { }

  ngOnInit(): void {
  }

  goBack(e:any):void {
    this.userService.goBack();
  }
}
