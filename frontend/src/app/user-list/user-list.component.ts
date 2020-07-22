import { Component, OnInit } from '@angular/core';
import { User } from '../Classes/user';
import { UserService } from '../Services/user-service.service';
import { Router } from '@angular/router';
import { GeneralService } from '../Services/general.service';
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
  constructor(
    private userService: UserService,
    private router: Router,
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

  openDialog() {
    console.log("Filter button clicked");
  }

  addSelectedUser(user:User) : void {
    this.selectedUsers.push(user);
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
    this.getEmployeeByType();
    this.getAllDepartments();
    this.getAllRoles();
  }

  getEmployeeByType(): void {
    this.userService.findAll(document.querySelector("select").value).subscribe(data => {
      this.users = data;
      this.userService.setUsers(data);
      this.selectedUsers = [];
    });
  }

  getAllDepartments():void{
    this.generalService.getDepartments().subscribe(resp=>{
      this.departments = resp;
    })
  }

  getAllRoles():void{
    this.generalService.getRoles().subscribe(resp=>{
      this.roles = resp;
    })
  }
}