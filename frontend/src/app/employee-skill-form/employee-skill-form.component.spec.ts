import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeSkillFormComponent } from './employee-skill-form.component';

describe('EmployeeSkillFormComponent', () => {
  let component: EmployeeSkillFormComponent;
  let fixture: ComponentFixture<EmployeeSkillFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmployeeSkillFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeSkillFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
