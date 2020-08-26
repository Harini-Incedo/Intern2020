import { Component, OnInit } from '@angular/core';
import { Project } from '../Classes/project';
import { ProjectServiceService } from '../Services/project-service.service';
import { Router } from '@angular/router';
import * as $ from 'jquery';
import * as d3 from 'd3';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit {

  projects: Project[];
  selectedProjects: Project[] ;
  selectedProject: Project ;
  counter = {};
  projectName : String = null;
  clientName: String = null;
  startDate: String = null;
  endDate: String = null;
  selectedStatus: String = null;
  statuses: String[];

  constructor(private projectService: ProjectServiceService, private router: Router) { }

  ngOnInit(): void {
    this.counter = {
      "projectName": 0,
      "clientName": 0,
      "startDate": 0,
      "endDate": 0,
      "status": 0
    }
    this.getStatuses();
    this.projectService.findAll().subscribe(data => {
      this.projects = data;
      this.projectService.setProjects(data);
      this.selectedProjects = [];
    });
    setTimeout(()=> {
      $('tbody#paginated').each(function() {
        let currentPage = 0;
        let numPerPage = 5;
        let $table = $('tbody#paginated');
        $table.bind('repaginate', function() {
          $table.find('tr').hide().slice(currentPage * numPerPage, (currentPage + 1) * numPerPage).show();
        });
        $table.trigger('repaginate');
        let numRows = $table.find('tr').length;
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
        $pager.insertAfter($('table')).find('span.page-number:first').addClass('active');
    });
    }, 1000)
    setTimeout(() => {
      d3.selectAll('div.pager span').attr('style', 'display:inline-block; width: 1.8em; height: 1.8em; line-height: 1.8; text-align: center; cursor: pointer; background: #000; color: #fff; margin-right: 0.5em;');
    }, 1010);
  }

  getStatuses() {
    this.projectService.getProjectStatuses().subscribe(d => this.statuses = d);
  }

  sort(obj: string) {
      if (this.counter[obj] % 2 === 0) {
        this.projects.sort((a, b) => a[obj].localeCompare(b[obj]))
      } else {
        this.projects.sort((a, b) => b[obj].localeCompare(a[obj]))
      }
      this.counter[obj]++;
  }

  filter() {
    console.log('filter button clicked');
  }

  onSelectOne(project:Project){
    this.projectService.setSelectedProject(project);
  }

  addSelectedProject(project:Project) : void {
    this.selectedProjects.push(project);
  }

  onSelectList(project:Project){
    if(this.selectedProjects && this.selectedProjects.length>0){
      let isInTheList : boolean = false;
      isInTheList = this.selectedProjects.find(su=>su.id === project.id) === undefined ? false : true;
      if(isInTheList){
        this.selectedProjects = this.selectedProjects.filter(d=>d.id !==project.id);
      }else {
        this.selectedProjects.push(project);
      }
    }else{
      this.selectedProjects.push(project);
    }
  }

  completeProject(project:Project) {
    let check = confirm("Are you sure you want to complete " + project.projectName + "?");
    if (check) {
      this.projectService.complete(+project.id).subscribe(d=>this.projectService.gotoProjectList());
      window.location.reload();
    }
  }


  startProject(project:Project) {
    let check = confirm("Are you sure you want to start " + project.projectName + "?");
    if (check) {
      this.projectService.start(+project.id).subscribe(d=>this.projectService.gotoProjectList());
      window.location.reload();
    }
  }

  closeProject(project:Project) {
    let check = confirm("Are you sure you want to close " + project.projectName + "?");
    if (check) {
      this.projectService.close(+project.id).subscribe(d=>this.projectService.gotoProjectList());
      window.location.reload();
    }
  }

  editProject(project:Project) : void {
    this.router.navigateByUrl(`/editProject/${project.id}`);
  }

}
