package org.unilab.uniplan.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.springframework.beans.BeanWrapperImpl;

public class ValidDateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String dateFrom;

    private String dateTo;

    @Override
    public void initialize(final ValidDateRange dateRange) {
        this.dateFrom = dateRange.dateFrom();
        this.dateTo = dateRange.dateTo();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        final BeanWrapperImpl wrapper = new BeanWrapperImpl(value);

        final LocalDate from = (LocalDate) wrapper.getPropertyValue(dateFrom);
        final LocalDate to = (LocalDate) wrapper.getPropertyValue(dateTo);

        if (from == null || to == null) {
            return true;
        }

        if (!from.isAfter(to)) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                   context.getDefaultConstraintMessageTemplate())
               .addPropertyNode(dateFrom)
               .addConstraintViolation();
        return false;
    }
}