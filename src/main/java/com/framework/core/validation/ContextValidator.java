package com.framework.core.validation;

import com.framework.core.config.ExecutionContext;
import com.framework.core.context.ContextState;

/**
 * ============================================================================
 * Class Name : ContextValidator
 * ============================================================================
 *
 * Responsibility:
 * ---------------------------------------------------------------------------
 * Validate ExecutionContext integrity before framework components use it.
 *
 * Why this exists:
 * ---------------------------------------------------------------------------
 * Prevents NullPointerExceptions later in execution by failing fast during
 * framework initialization.
 *
 * Example:
 *
 * BAD: ---- DriverContext is null ↓ Test executes ↓ Random NullPointerException
 * after 5 minutes
 *
 * GOOD: ---- ContextValidator detects issue immediately ↓ Test fails during
 * setup ↓ Faster debugging
 *
 * Validation Scope:
 * --------------------------------------------------------------------------- -
 * ExecutionContext exists - DriverContext exists - ApiContext exists -
 * DbContext exists - MetadataContext exists
 *
 * Future:
 * ---------------------------------------------------------------------------
 * Additional validations can be added:
 *
 * - Driver initialized - Environment configured - Correlation ID generated -
 * Required metadata present
 *
 * ============================================================================
 */
public final class ContextValidator {

	private ContextValidator() {
	}

	/**
	 * Validates execution context.
	 *
	 * Fail-fast approach: Framework stops immediately if required context
	 * components are missing.
	 *
	 * @param context ExecutionContext to validate
	 */
	public static void validate(ExecutionContext context) {

		// ---------------------------------------------------------
		// STEP 1: CONTEXT EXISTS
		// ---------------------------------------------------------
		if (context == null) {
			throw new IllegalStateException("ExecutionContext is not initialized for thread:"
					+ Thread.currentThread().getName() + "ExecutionContext is NULL. " +
                    "BaseTest or Listener did not initialize context.");
		}

	    // ---------------------------------------------------------
        // STEP 2: VALID STATE CHECK
        // ---------------------------------------------------------
        if (context.getState() == null) {
            throw new IllegalStateException(
                    "ExecutionContext state is NULL. Invalid initialization.");
        }
        
        if (!context.getState().equals(ContextState.INITIALIZED)) {
            throw new IllegalStateException(
                    "ExecutionContext is not in INITIALIZED state. Current state: "
                            + context.getState());
        }

        // ---------------------------------------------------------
        // STEP 3: DRIVER SAFETY CHECK (avoid double init)
        // ---------------------------------------------------------
        if (context.getDriverContext() != null
                && context.getDriverContext().getDriver() != null) {

            throw new IllegalStateException(
                    "Driver already initialized in context. Possible duplicate setup.");
        }
        
        
		// ---------------------------------------------------------
		// Validate DriverContext
		// ---------------------------------------------------------
		if (context.getDriverContext() == null) {

			throw new IllegalStateException("DriverContext is not initialized.");
		}

	
        
        
		// ---------------------------------------------------------
		// Validate ApiContext
		// ---------------------------------------------------------
		if (context.getApiContext() == null) {

			throw new IllegalStateException("ApiContext is not initialized.");
		}

		// ---------------------------------------------------------
		// Validate DbContext
		// ---------------------------------------------------------
		if (context.getDbContext() == null) {

			throw new IllegalStateException("DbContext is not initialized.");
		}

		// ---------------------------------------------------------
		// Validate MetadataContext
		// ---------------------------------------------------------
		if (context.getMetadataContext() == null) {

			throw new IllegalStateException("TestMetadataContext is not initialized.");
		}
	}
}