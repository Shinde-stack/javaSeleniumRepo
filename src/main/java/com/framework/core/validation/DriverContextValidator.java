package com.framework.core.validation;

import com.framework.core.context.DriverContext;
import com.framework.core.excepions.FrameworkException;

/**
 * ============================================================================
 * Class Name : DriverContextValidator
 * ============================================================================
 *
 * Validates DriverContext.
 *
 * Driver is expected to be null during initialization.
 *
 * ============================================================================
 */
public class DriverContextValidator {

    public void validate(DriverContext driverContext) {

        if (driverContext == null) {
            throw new FrameworkException(
                    "DriverContext cannot be null.");
        }
    }
}