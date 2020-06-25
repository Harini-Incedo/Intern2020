import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListComponent } from './user-list/user-list.component';
import { UserFormComponent } from './user-form/user-form.component';
import { ViewPageComponent } from './view-page/view-page.component';

const routes: Routes = [
  { path: 'employees', component: UserListComponent },
  { path: 'addEmployee', component: UserFormComponent },
  { path: 'viewEmployee/:id', component: ViewPageComponent},
  { path: 'editEmployee/:id', component: UserFormComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }