package com.framework.core.validation;

import java.util.ArrayList;
import java.util.List;

import com.framework.core.config.EnvConfig;
import com.framework.core.excepions.FrameworkException;

public class ConfigValidator {

    public void validate(EnvConfig config) {

        if (config == null) {
            throw new FrameworkException(
                    "Environment configuration cannot be null.");
        }

        List<String> errors = new ArrayList<>();

        if (config.getBrowserType() == null) {
            errors.add("Browser configuration is missing.");
        }

        if (config.getBaseUrl() == null
                || config.getBaseUrl().isBlank()) {

            errors.add("Base URL configuration is missing.");
        }

        if (!errors.isEmpty()) {
            throw new FrameworkException(buildMessage(errors));
        }
    }

    private String buildMessage(List<String> errors) {

        StringBuilder builder =
                new StringBuilder("Configuration validation failed:\n");

        errors.forEach(error ->
                builder.append(" - ").append(error).append("\n"));

        return builder.toString();
    }
}