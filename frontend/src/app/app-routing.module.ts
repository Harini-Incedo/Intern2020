import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListComponent } from './user-list/user-list.component';
import { UserFormComponent } from './user-form/user-form.component';
import { ViewPageComponent } from './view-page/view-page.component';
import { ProjectListComponent } from './project-list/project-list.component';
import { ProjectFormComponent } from './project-form/project-form.component';
import { ProjectDetailComponent } from './project-detail/project-detail.component';
import { SkillFormComponent } from './skill-form/skill-form.component';
import { EmployeeSkillFormComponent } from './employee-skill-form/employee-skill-form.component';

const routes: Routes = [
  { path: 'employees', component: UserListComponent },
  { path: 'addEmployee', component: UserFormComponent },
  { path: 'viewEmployee/:id', component: ViewPageComponent},
  { path: 'editEmployee/:id', component: UserFormComponent},
  { path: 'projects', component: ProjectListComponent},
  { path: 'addProject', component: ProjectFormComponent },
  { path: 'editProject/:id', component: ProjectFormComponent},
  { path: 'viewProject/:id', component: ProjectDetailComponent},
  { path: 'addSkill/:projectid', component: SkillFormComponent},
  { path: 'addEmployee/:projectid/skill/:skillName', component: EmployeeSkillFormComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }