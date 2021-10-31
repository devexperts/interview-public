package com.devexperts.rest.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
@Constraint(validatedBy = AccountIdValidator.class)
public @interface ExistingAccountId {

    String message() default "Account not exists.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
