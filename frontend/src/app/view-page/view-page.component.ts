import { Component, OnInit } from '@angular/core';
import { User } from '../user';

@Component({
  selector: 'app-view-page',
  templateUrl: './view-page.component.html',
  styleUrls: ['./view-page.component.css']
})
export class ViewPageComponent implements OnInit {

  user: User;

  constructor() {
    this.user = new User();
  }

  ngOnInit(): void {
  }

}
