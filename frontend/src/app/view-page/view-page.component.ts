import { Component, OnInit, Input } from '@angular/core';
import { User } from '../user';
import { ActivatedRoute } from '@angular/router';
import {UserService} from '../user-service.service';
@Component({
  selector: 'app-view-page',
  templateUrl: './view-page.component.html',
  styleUrls: ['./view-page.component.css']
})
export class ViewPageComponent implements OnInit {

  selectedUser : User;
  user: User;

  constructor( private route: ActivatedRoute, public userService : UserService) {
    
  }

  ngOnInit(): void {
    this.getUserById();
  }

  
  deleteUser(user:User) : void{
    this.userService.delete(+user.id).subscribe(d=>this.userService.gotoUserList()) 
  }
  
  getUserById(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.userService.getUserByIdApi(id).subscribe(data=>this.selectedUser = data)
  }

}
