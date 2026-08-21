package com.framework.core.validation;

import com.framework.core.context.DriverContext;
import com.framework.core.excepions.FrameworkException;

public class DriverContextValidator {

    public void validate(DriverContext driverContext) {

        if (driverContext == null) {
            throw new FrameworkException(
                    "DriverContext cannot be null.");
        }

        /*
         * Driver is intentionally NOT validated.
         *
         * During context initialization:
         * driver == null
         *
         * After DriverManager:
         * driver != null
         *
         * Driver lifecycle owns this validation.
         */
    }
}