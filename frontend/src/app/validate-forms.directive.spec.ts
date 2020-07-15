import { ForbiddenHoursValidatorDirective } from './validate-hours-forms.directive';

describe('ValidateFormsDirective', () => {
  it('should create an instance', () => {
    const directive = new ForbiddenHoursValidatorDirective();
    expect(directive).toBeTruthy();
  });
});
