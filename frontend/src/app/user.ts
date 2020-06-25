import { Time } from '@angular/common';

export class User {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
  department: string;
  startDate: string;
  endDate: string;
  location: string;
  timeZone: string;
  startWork: Time;
  endWork: Time;
  manager: string;
}
