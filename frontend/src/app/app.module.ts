import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app-component/app.component';
import { UserListComponent } from './user-list/user-list.component';
import { UserFormComponent } from './user-form/user-form.component';
import { UserService } from './Services/user-service.service';
import { ViewPageComponent } from './view-page/view-page.component';
import { ProjectListComponent } from './project-list/project-list.component';
import { ProjectDetailComponent } from './project-detail/project-detail.component';
import { ProjectFormComponent } from './project-form/project-form.component';
import { ForbiddenHoursValidatorDirective } from './validate-hours-forms.directive';
import { SkillFormComponent } from './skill-form/skill-form.component';
import { EmployeeSkillFormComponent } from './employee-skill-form/employee-skill-form.component';
import { EditHoursComponent } from './edit-hours/edit-hours.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    AppComponent,
    UserListComponent,
    UserFormComponent,
    ViewPageComponent,
    ProjectListComponent,
    ProjectDetailComponent,
    ProjectFormComponent,
    ForbiddenHoursValidatorDirective,
    SkillFormComponent,
    EmployeeSkillFormComponent,
    EditHoursComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NoopAnimationsModule
  ],
  providers: [UserService],
  bootstrap: [AppComponent]
})
export class AppModule { }