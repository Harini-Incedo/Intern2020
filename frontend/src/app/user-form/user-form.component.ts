import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user-service.service';
import { User } from '../user';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent {

  user: User;
  isCreateMode: boolean;

  constructor(
    private route: ActivatedRoute,
      private router: Router,
        private userService: UserService) {
  }

  onSubmit() {
    if(this.isCreateMode){
      this.userService.create(this.user).subscribe(result => this.userService.gotoUserList());
    }else{
      this.userService.edit(this.user).subscribe(result => this.userService.gotoUserList());
    }
  }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    if (!id){
      this.user = new User();
      this.isCreateMode = true;
    }else{
      this.userService.getUserByIdApi(id).subscribe(d=>this.user = d);
      this.isCreateMode = false;
    }
  }
}