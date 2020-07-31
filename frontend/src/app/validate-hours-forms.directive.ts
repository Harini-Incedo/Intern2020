import { Directive,Input } from '@angular/core';
import { Validator, AbstractControl, NG_VALIDATORS, ValidatorFn } from '@angular/forms';

@Directive({
  selector: '[appForbiddenHoursNeeded]',
  providers: [{provide: NG_VALIDATORS, useExisting: ForbiddenHoursValidatorDirective, multi: true}]
})

export class ForbiddenHoursValidatorDirective implements Validator {
  @Input('appForbiddenHoursNeeded') hoursNeeded: string;

  forbiddenHoursValidator(hoursNeeded: string): ValidatorFn {
    return (control: AbstractControl): {[key: string]: any} | null => {
      let forbidden :boolean = false;
      let  noneDigitValue :boolean = false;
      if(control.value){
        if(("" + control.value).includes("e") || ("" + control.value).includes("+") || ("" + control.value).includes("-") || ("" + control.value).includes(".")){
          noneDigitValue = true;
        }else{
          noneDigitValue = false;
        }
        if(hoursNeeded === "engagement"){
          forbidden = control.value >100 || control.value <1?true:false
        } else if(hoursNeeded === "project"){
          forbidden = control.value >1000 || control.value <1?true:false
        } else if(hoursNeeded === "peopleNeeded"){
          forbidden = control.value<1?true:false
        } else if(hoursNeeded === "weeklyHoursForEmployee"){
          forbidden = control.value<1?true:false
        } else if(hoursNeeded === "weeklyHoursNeeded"){
          forbidden = control.value<1?true:false
        }
      }
      return forbidden || noneDigitValue ? {'forbiddenHoursNeeded': {value: control.value}} : null;
    };
  }

  validate(control: AbstractControl): {[key: string]: any} | null {
    return this.hoursNeeded ?
    this.forbiddenHoursValidator(this.hoursNeeded)(control): null;
  }

}