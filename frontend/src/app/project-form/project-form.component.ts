import { Component, OnInit } from '@angular/core';
import { Project } from '../project';

@Component({
  selector: 'app-project-form',
  templateUrl: './project-form.component.html',
  styleUrls: ['./project-form.component.css']
})
export class ProjectFormComponent implements OnInit {

  project: Project;

  constructor() { }

  ngOnInit(): void {
  }

}
