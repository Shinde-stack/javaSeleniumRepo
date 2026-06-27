package com.framework.core.validation;

import com.framework.core.context.ContextState;
import com.framework.core.context.ExecutionContext;

/**
 * ============================================================================
 * Class Name : ContextValidator
 * ============================================================================
 *
 * Purpose:
 * --------
 * Pre-flight validation guard for ExecutionContext.
 *
 * Responsibilities:
 * -----------------
 * 1. Validate ExecutionContext exists
 * 2. Validate mandatory sub-contexts exist
 * 3. Validate lifecycle state
 * 4. Detect duplicate driver initialization
 * 5. Fail fast before test execution starts
 *
 * Why this class exists:
 * ----------------------
 * Prevent framework execution from reaching runtime
 * NullPointerExceptions caused by invalid setup.
 *
 * Example:
 *
 * BAD
 * ----
 * DriverContext missing
 *      ↓
 * Test starts
 *      ↓
 * Page Object executes
 *      ↓
 * NullPointerException
 *
 * GOOD
 * ----
 * ContextValidator
 *      ↓
 * Immediate failure during setup
 *      ↓
 * Faster debugging
 *
 * ============================================================================
 */
public final class ContextValidator {

    private ContextValidator() {
    }

    /**
     * Validates ExecutionContext before driver creation.
     *
     * Expected State:
     *
     * CREATED
     *      ↓
     * validate()
     *      ↓
     * Driver Initialization
     *      ↓
     * INITIALIZED
     *      ↓
     * RUNNING
     * 
     * ===========================
     * 
ExecutionContext created
      ↓
State = CREATED
      ↓
ContextValidator.validate()
      ↓
Driver initialization
      ↓
State = INITIALIZED
      ↓
Open URL
      ↓
State = RUNNING

=================================
     */
    public static void validate(ExecutionContext context) {

        // ==========================================================
        // STEP 1 : Context existence
        // ==========================================================
        if (context == null) {

            throw new IllegalStateException(
                    "ExecutionContext is NULL. " +
                    "Framework setup failed before context creation.");
        }

        // ==========================================================
        // STEP 2 : Lifecycle state existence
        // ==========================================================
        if (context.getState() == null) {

            throw new IllegalStateException(
                    "ExecutionContext state is NULL.");
        }

        // ==========================================================
        // STEP 3 : Expected lifecycle state
        // ==========================================================
        if (context.getState() != ContextState.CREATED) {

            throw new IllegalStateException(
                    "Expected context state CREATED but found: "
                            + context.getState());
        }

        // ==========================================================
        // STEP 4 : DriverContext existence
        // ==========================================================
        if (context.getDriverContext() == null) {

            throw new IllegalStateException(
                    "DriverContext is not initialized.");
        }

        // ==========================================================
        // STEP 5 : Config existence
        // ==========================================================
        if (context.getConfig() == null) {

            throw new IllegalStateException(
                    "EnvConfig is not attached to ExecutionContext.");
        }

        // ==========================================================
        // STEP 6 : Prevent duplicate driver initialization
        // ==========================================================
        if (context.hasDriver()) {

            throw new IllegalStateException(
                    "Driver already exists in context. " +
                    "Possible duplicate setup execution.");
        }
    }
    
//    BaseTest integration
//    context = new ExecutionContext();
//
//    context.setConfig(config);
//
//    ExecutionContextHolder.setContext(context);
//
//    ContextValidator.validate(context);
//
//    driverManager.initializeDriver(...);
//
//    context.setState(ContextState.INITIALIZED);
//
//    driver = context.getDriverContext().getDriver();
//
//    context.setState(ContextState.RUNNING);  
    
    
    
    
    
    
    
    
}