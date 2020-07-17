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

  public getRoles(): Observable<any> {
    return this.http.get("http://localhost:8080/roles",this.httpOptions)
  }

  public printForbiddenMessage(value:number | null , type:string ):string {
    if(value === null){
      return "entered value is incorrect"
    }else if(value<1){
      return "is less than 1, the smallest processable value.";
    }else if(value>100 && type === "engagement"){
      return "is greater than 100, the largest processable value.";
    }else if(value>1000 && type === "project"){
       return "the value is greater than 1000, the largest processable value."
    }
  }

  public validateEndDate(endDate:any,startDate:any){
    if(endDate.value < startDate.value){
      endDate.control.setErrors({'incorrect': true});
      return true;
    }else{
      return false;
    }    
  }
}
