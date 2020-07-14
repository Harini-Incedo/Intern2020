import { Component, OnInit } from '@angular/core';
import { User } from '../Classes/user';
import { UserService } from '../Services/user-service.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: User[];
  selectedUsers: User[] ;
  selectedUser: User ;
  constructor(private userService: UserService,private router: Router) {
  }

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
    this.userService.findAll().subscribe(data => {
      this.users = data;
      this.userService.setUsers(data);
      this.selectedUsers = [];
    });
  }

  getByEmployeeType(employeeType:string) : void {
    console.log(employeeType);
  }
}