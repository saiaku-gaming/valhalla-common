package com.valhallagame.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckLowercaseValidator implements ConstraintValidator<CheckLowercase, String> {

	@Override
	public void initialize(CheckLowercase constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintContext) {
		if (value == null) {
			return true;
		}

		boolean valid = value.equals(value.toLowerCase());

		if (!valid) {
			constraintContext.disableDefaultConstraintViolation();
			constraintContext
					.buildConstraintViolationWithTemplate(
							"The value was not lowercase. Got: " + value + " expected: " + value.toLowerCase())
					.addConstraintViolation();
		}

		return valid;
	}

}
