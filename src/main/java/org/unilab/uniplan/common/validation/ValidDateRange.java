package org.unilab.uniplan.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidDateRangeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidDateRange {

    String message() default "date from must be after date to";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String dateFrom();

    String dateTo();
}