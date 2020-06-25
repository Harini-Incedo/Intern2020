import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user-service.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: User[];
  selectedUsers: User[] ;
  selectedUser: User ;
  constructor(private userService: UserService) {
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
    this.userService.delete(+user.id);
    
  }

  ngOnInit() {
    this.userService.findAll().subscribe(data => {
      this.users = data;
      this.userService.setUsers(data);
      this.selectedUsers = [];
    });
  } 
}