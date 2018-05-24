package ml.echelon133.model.form.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AuthorityValidator implements ConstraintValidator<ValidAuthority, String> {

    public static final String PREFIX = "ROLE_";

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.startsWith(PREFIX);
    }

    @Override
    public void initialize(ValidAuthority constraintAnnotation) {}
}
