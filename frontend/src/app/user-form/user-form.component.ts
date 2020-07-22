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
  skills: String[];
  selectedSkills: String[];
  user: User = {id:"", firstName:"", lastName:"", email:"", role:"", skills:[], department:"", startDate:"", endDate:"", location:"", timezone:"", workingHours:"", manager:""};
  isCreateMode: boolean;
  inValidDate: boolean = false;

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
    this.skills = [];
    this.skills.push("Test");
    this.getAllDepartments();
    this.getAllRoles();
    this.selectedSkills = [];
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

  addSkill():void{
    if (document.querySelectorAll("select")[1].value.length > 0) {
      if (this.selectedSkills.includes(document.querySelectorAll("select")[1].value)) {
        alert("This value is already in the skills. Please choose another skill.")
      } else {
        this.selectedSkills.push(document.querySelectorAll("select")[1].value);
        this.selectedSkills.sort();
      }
    } else {
      alert("Please enter a valid input");
    }
    document.querySelectorAll("select")[1].value = "";
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

  getAllRoles():void{
    this.generalService.getRoles().subscribe(resp=>{
      this.roles = resp;
    })
  }

  goBack(e:any):void {
    this.generalService.goBack();
  }

  validateEndDate(endDate:any,startDate:any){
    this.inValidDate = this.generalService.validateEndDate(endDate,startDate);
  }

}