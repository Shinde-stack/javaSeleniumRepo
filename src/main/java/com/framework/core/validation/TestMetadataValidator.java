package com.framework.core.validation;

import com.framework.core.context.TestMetadataContext;
import com.framework.core.excepions.FrameworkException;

/**
 * ============================================================================
 * Class Name : TestMetadataValidator
 * ============================================================================
 *
 * Validates test execution metadata.
 *
 * ============================================================================
 */
public class TestMetadataValidator {

    public void validate(TestMetadataContext metadata) {

        if (metadata == null) {
            throw new FrameworkException(
                    "TestMetadataContext cannot be null.");
        }

        if (metadata.getCorrelationId() == null
                || metadata.getCorrelationId().isBlank()) {

            throw new FrameworkException(
                    "CorrelationId cannot be null.");
        }

        if (metadata.getStartTime() <= 0) {
            throw new FrameworkException(
                    "Invalid execution start time.");
        }
    }
}