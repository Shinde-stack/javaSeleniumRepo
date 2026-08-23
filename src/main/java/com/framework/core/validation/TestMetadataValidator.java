package com.framework.core.validation;

import java.util.ArrayList;
import java.util.List;

import com.framework.core.context.TestMetadataContext;
import com.framework.core.excepions.FrameworkException;

public class TestMetadataValidator {

    public void validate(TestMetadataContext metadata) {

        if (metadata == null) {
            throw new FrameworkException(
                    "TestMetadataContext cannot be null.");
        }

        List<String> errors = new ArrayList<>();

        if (metadata.getCorrelationId() == null
                || metadata.getCorrelationId().isBlank()) {

            errors.add("CorrelationId cannot be null.");
        }

        if (metadata.getStartTime() <= 0) {
            errors.add("Execution start time is invalid.");
        }

        if (!errors.isEmpty()) {
            throw new FrameworkException(buildMessage(errors));
        }
    }

    private String buildMessage(List<String> errors) {

        StringBuilder builder =
                new StringBuilder("Test metadata validation failed:\n");

        errors.forEach(error ->
                builder.append(" - ").append(error).append("\n"));

        return builder.toString();
    }
}