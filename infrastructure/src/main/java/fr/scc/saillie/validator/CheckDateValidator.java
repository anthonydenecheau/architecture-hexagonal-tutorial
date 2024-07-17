package fr.scc.saillie.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckDateValidator implements ConstraintValidator<CheckDateFormat, String> {

    private String pattern;

    @Override
    public void initialize(CheckDateFormat constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {

        if (object == null) {
            return true;
        }
        DateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(object);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}