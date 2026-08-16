package com.framework.core.validation;

import com.framework.core.context.ExecutionContext;

/**
 * ============================================================================
 * Class Name : ContextValidator
 * ============================================================================
 *
 * Central validator for ExecutionContext.
 *
 * Responsibilities
 * ----------------
 * - Validate all components of an ExecutionContext.
 * - Delegate validation to specialized validators.
 *
 * This class acts as the facade for context validation, keeping lifecycle
 * classes independent of individual validators.
 *
 * Validation Flow
 * ---------------
 *
 * ExecutionContext
 *        │
 *        ├── ConfigValidator
 *        ├── DriverContextValidator
 *        └── TestMetadataValidator
 *
 * ============================================================================
 */
public class ContextValidator {

    private final ConfigValidator configValidator;

    private final DriverContextValidator driverContextValidator;

    private final TestMetadataValidator testMetadataValidator;

    public ContextValidator() {

        this.configValidator = new ConfigValidator();
        this.driverContextValidator = new DriverContextValidator();
        this.testMetadataValidator = new TestMetadataValidator();
    }

    /**
     * Validates the complete ExecutionContext.
     *
     * @param context ExecutionContext to validate
     */
    public void validate(ExecutionContext context) {

        if (context == null) {
            throw new IllegalArgumentException(
                    "ExecutionContext cannot be null.");
        }

        configValidator.validate(context.getConfig());

        driverContextValidator.validate(
                context.getDriverContext());

        testMetadataValidator.validate(
                context.getMetadataContext());
    }
}