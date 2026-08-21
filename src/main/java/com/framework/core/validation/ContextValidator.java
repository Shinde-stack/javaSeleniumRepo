package com.framework.core.validation;

import java.util.ArrayList;
import java.util.List;

import com.framework.core.context.ExecutionContext;
import com.framework.core.excepions.FrameworkException;

public class ContextValidator {

    private final ConfigValidator configValidator =
            new ConfigValidator();

    private final DriverContextValidator driverContextValidator =
            new DriverContextValidator();

    private final TestMetadataValidator testMetadataValidator =
            new TestMetadataValidator();

    public void validate(ExecutionContext context) {

        if (context == null) {
            throw new FrameworkException(
                    "ExecutionContext cannot be null.");
        }

        List<String> errors = new ArrayList<>();

        validateComponent(
                () -> configValidator.validate(context.getConfig()),
                errors);

        validateComponent(
                () -> driverContextValidator.validate(
                        context.getDriverContext()),
                errors);

        validateComponent(
                () -> testMetadataValidator.validate(
                        context.getMetadataContext()),
                errors);

        if (!errors.isEmpty()) {

            StringBuilder builder =
                    new StringBuilder("ExecutionContext validation failed:\n");

            errors.forEach(error ->
                    builder.append(" - ").append(error).append("\n"));

            throw new FrameworkException(builder.toString());
        }
    }

    private void validateComponent(
            Runnable validator,
            List<String> errors) {

        try {
            validator.run();
        }
        catch (FrameworkException ex) {
            errors.add(ex.getMessage());
        }
    }
}