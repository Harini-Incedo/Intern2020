import { Component, OnInit } from '@angular/core';
import { User } from '../Classes/user';
import { UserService } from '../Services/user-service.service';
import { Router } from '@angular/router';
import { GeneralService } from '../Services/general.service';
import { Skill } from '../Classes/skill';
import * as $ from 'jquery';
import * as d3 from 'd3';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: User[];
  selectedUsers: User[] ;
  selectedUser: User ;
  departments: String[];
  roles: String[];
  skills: Skill[];
  counter: {};
  firstName: String = null;
  lastName: String = null;
  selectedDepartment: String;
  location: String = null;
  selectedRole: String = null;
  selectedSkills: String[] = null;
  selectedSkill: String = null;
  employeeType: string;

  constructor(
    private userService: UserService,
    public router: Router,
    private generalService: GeneralService
  ) {}

  onSelectOne(user:User){
    this.userService.setSelectedUser(user);
  }

  onSelectList(user:User){
    if(this.selectedUsers && this.selectedUsers.length>0){
      let isInTheList : boolean = false;
      isInTheList = this.selectedUsers.find(su=>su.id === user.id) === undefined ? false : true;
      if(isInTheList){
        this.selectedUsers = this.selectedUsers.filter(d=>d.id !==user.id);
      }else {
        this.selectedUsers.push(user);
      }
    }else{
      this.selectedUsers.push(user);
    }
  }

  addSkill() {
    if (this.selectedSkill.length > 0) {
      if (this.selectedSkills.includes(this.selectedSkill)) {
        alert("This value is already in the skills. Please choose another skill.")
      } else {
        this.selectedSkills.push(this.selectedSkill);
        this.selectedSkills.sort();
      }
    } else {
      alert("Please enter a valid input");
    }
    this.selectedSkill = "";
  }

  removeSkill(com:string):void{
    let newSkills = [];
    for (let i = 0; i < this.selectedSkills.length; i++) {
      if (com != this.selectedSkills[i]) {
        newSkills.push(this.selectedSkills[i]);
      }
    }
    this.selectedSkills = newSkills.sort();
  }

  filter() {
    d3.select('div.pager').attr('hidden', true);
    let test = {
      "firstName": this.firstName,
      "lastName": this.lastName,
      "department": this.selectedDepartment,
      "location": this.location,
      "role": this.selectedRole,
      "skills": this.selectedSkills.toLocaleString(),
      "status": this.employeeType
    }
    if (this.selectedSkills.toLocaleString().length == 0) {
      test["skills"] = null;
    }
    this.userService.getFilteredEmployee(test).subscribe(d => {
      this.users = d;
    });
    this.paginate();
  }

  paginate() {
    d3.select('div.pager').attr('hidden', true);
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
            }).appendTo($pager);
        }
        $pager.insertAfter($('table')).find('span.page-number:first').addClass('active');
    });
    }, 1000)
    setTimeout(() => {
      d3.selectAll('div.pager span').attr('style', 'display:inline-block; width: 1.8em; height: 1.8em; line-height: 1.8; text-align: center; cursor: pointer; background: #000; color: #fff; margin-right: 0.5em;');
    }, 1010);
  }

  sort(obj: any) {
    if (this.counter[obj] % 2 === 0) {
      this.users.sort((a, b) => a[obj].localeCompare(b[obj]))
    } else {
      this.users.sort((a, b) => b[obj].localeCompare(a[obj]))
    }
    this.counter[obj]++;
  }

  sortSkills(obj:any) {
    if (this.counter[obj] % 2 === 0) {
      this.users.sort((a, b) => a[obj][0].localeCompare(b[obj][0]))
    } else {
      this.users.sort((a, b) => b[obj][0].localeCompare(a[obj][0]))
    }
    this.counter[obj]++;
  }

  addSelectedUser(user:User) : void {
    this.selectedUsers.push(user);
  }

  checkStatus(users:User[]) : boolean {
    if(users && users.length === 1){
      return !users[0].active;
    }
    return true
  }

  checkInactiveStatus(users:User[]) : boolean {
    if(users && users.length === 1){
      return users[0].active;
    }
    return true;
  }

  activateUser(user:User) : void {
    this.userService.activateEmployee(user.id).subscribe(resp => {
      window.location.reload();
    })
  }

  deleteUser(user:User) : void{
    let check = confirm("Are you sure you want to delete " + user.firstName + " " + user.lastName + " as an employee?");
    if (check) {
      this.userService.delete(+user.id).subscribe(d=>this.userService.gotoUserList());
      window.location.reload();
    }
  }

  editUser(user:User) : void {
    this.router.navigateByUrl(`/editEmployee/${user.id}`);
  }

  ngOnInit() {
    d3.select('div.pager').attr('hidden', true);
    this.employeeType = "Active";
    this.counter = {
      "firstName": 0,
      "lastName": 0,
      "department": 0,
      "location": 0,
      "role": 0,
      "skills": 0
    }
    this.selectedSkills = [];
    this.getEmployeeByType();
    this.getAllDepartments();
    this.getAllRoles();
    this.getAllSkills();
  }

  getEmployeeByType(): void {
    this.userService.findAll(this.employeeType).subscribe(data => {
      this.users = data;
      this.userService.setUsers(data);
      this.selectedUsers = [];
    });
    d3.select('div.pager').attr('hidden', true);
    this.paginate();
  }

  getAllDepartments():void{
    this.generalService.getDepartments().subscribe(resp=>{
      this.departments = resp;
    })
  }

  getAllSkills():void{
    this.userService.getSkills().subscribe(resp=>{
      this.skills = resp;
    })
  }

  getAllRoles():void{
    this.generalService.getRoles().subscribe(resp=>{
      this.roles = resp;
    })
  }
}