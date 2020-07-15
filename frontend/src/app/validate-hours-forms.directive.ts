import { Directive,Input } from '@angular/core';
import { Validator, AbstractControl, NG_VALIDATORS, ValidatorFn } from '@angular/forms';

@Directive({
  selector: '[appForbiddenHoursNeeded]',
  providers: [{provide: NG_VALIDATORS, useExisting: ForbiddenHoursValidatorDirective, multi: true}]
})

export class ForbiddenHoursValidatorDirective implements Validator {
  @Input('appForbiddenHoursNeeded') hoursNeeded: number;

  forbiddenHoursValidator(hoursNeeded: number): ValidatorFn {
    return (control: AbstractControl): {[key: string]: any} | null => {
      const forbidden = control.value >100 || control.value <1 ;
      return forbidden ? {'forbiddenHoursNeeded': {value: control.value}} : null;
    };
  }

  validate(control: AbstractControl): {[key: string]: any} | null {
    return this.hoursNeeded ?
    this.forbiddenHoursValidator(this.hoursNeeded)(control): null;
  }

}