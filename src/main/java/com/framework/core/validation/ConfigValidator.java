package com.framework.core.validation;

import com.framework.core.config.EnvConfig;
import com.framework.core.excepions.FrameworkException;

/**
 * ============================================================================
 * Class Name : ConfigValidator
 * ============================================================================
 *
 * Validates framework configuration.
 *
 * ============================================================================
 */
public class ConfigValidator {

    public void validate(EnvConfig config) {

        if (config == null) {
            throw new FrameworkException(
                    "Environment configuration cannot be null.");
        }

        if (config.getBrowserType() == null) {
            throw new FrameworkException(
                    "Browser configuration is missing.");
        }

        if (config.getBaseUrl() == null
                || config.getBaseUrl().isBlank()) {

            throw new FrameworkException(
                    "Base URL configuration is missing.");
        }
    }
}