import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListComponent } from './user-list/user-list.component';
import { UserFormComponent } from './user-form/user-form.component';
import { ViewPageComponent } from './view-page/view-page.component';
import { ProjectListComponent } from './project-list/project-list.component';
import { ProjectFormComponent } from './project-form/project-form.component';
import { ProjectDetailComponent } from './project-detail/project-detail.component';
import { EngagementFormComponent } from './engagement-form/engagement-form.component';
import { Engagement } from './engagement';

const routes: Routes = [
  { path: 'employees', component: UserListComponent },
  { path: 'addEmployee', component: UserFormComponent },
  { path: 'viewEmployee/:id', component: ViewPageComponent},
  { path: 'editEmployee/:id', component: UserFormComponent},
  { path: 'projects', component: ProjectListComponent},
  { path: 'addProject', component: ProjectFormComponent },
  { path: 'viewProject/:id', component: ProjectDetailComponent},
  { path: 'viewProject/:id/editEngagement/:engagementid', component: EngagementFormComponent},
  { path: 'viewProject/:id/addEngagement', component: EngagementFormComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }