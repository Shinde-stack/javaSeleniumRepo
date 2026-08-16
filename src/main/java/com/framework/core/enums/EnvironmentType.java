package com.framework.core.enums;

import java.util.Arrays;

import com.framework.core.excepions.FrameworkException;

/**
 * ============================================================================
 * Enum Name : EnvironmentType
 * ============================================================================
 *
 * Supported framework execution environments.
 *
 * Used by:
 * - EnvResolver
 * - ConfigLoader
 * - ExecutionContext
 * - Reporting
 * - CI/CD
 *
 * Never compare environment names using String literals.
 * Always use this enum.
 * ============================================================================
 */
public enum EnvironmentType {

    QA("qa"),
    UAT("uat"),
    STAGE("stage"),
    PROD("prod"),
    DEV("dev");

    private final String value;

    EnvironmentType(String value) {
        this.value = value;
    }

    /**
     * Returns lowercase environment name.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns config file name.
     *
     * Example:
     * QA -> qa.properties
     */
    public String getConfigFileName() {
        return value + ".properties";
    }

    /**
     * Converts String to EnvironmentType.
     *
     * Example:
     * "QA" -> QA
     * "qa" -> QA
     * "Qa" -> QA
     */
    public static EnvironmentType from(String env) {

        if (env == null || env.isBlank()) {
            throw new FrameworkException("Environment cannot be null or blank.");
        }

        return Arrays.stream(values())
                .filter(e -> e.value.equalsIgnoreCase(env.trim()))
                .findFirst()
                .orElseThrow(() -> new FrameworkException(
                        "Unsupported environment: '" + env +
                        "'. Supported values: " +
                        Arrays.toString(values())));
    }
}