import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../Services/user-service.service';
import { User } from '../Classes/user';
import { GeneralService } from '../Services/general.service';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent {
  departments: String[];
  roles: String[];
  user: User;
  isCreateMode: boolean;

  constructor(
    private route: ActivatedRoute,
      private router: Router,
        private userService: UserService,
          private generalService: GeneralService) {
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
    this.getAllDepartments();
    this.getAllRoles();
    if (!id){
      this.user = new User();
      this.isCreateMode = true;
    }else{
      this.userService.getUserByIdApi(id).subscribe(d=>this.user = d);
      this.isCreateMode = false;
    }
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

  goBack(e:any):void {
    this.generalService.goBack();
  }
}