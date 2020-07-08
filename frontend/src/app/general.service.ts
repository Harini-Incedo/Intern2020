import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import {Location} from '@angular/common';
import { Observable } from 'rxjs';
import { HttpHeaders, HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GeneralService {



  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private router: Router,private _location: Location, private http: HttpClient) { }

  public getDepartments(): Observable<any> {
    return this.http.get("http://localhost:8080/departments",this.httpOptions)
  }

  public goBack(){
    this._location.back();
  }
}
