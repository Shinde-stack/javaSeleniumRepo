package com.framework.core.context;


/**
 * Represents the lifecycle state of a test execution context.
 *
 * Used to:
 * - Prevent invalid operations
 * - Track execution progress
 * - Support future telemetry/reporting
 *
 * Typical flow:
 *
 * CREATED
 *    ↓
 * INITIALIZED
 *    ↓
 * RUNNING
 *    ↓
 * CLEANING_UP
 *    ↓
 * DESTROYED
 */
public enum ContextState {

    CREATED,

    INITIALIZED,

    RUNNING,

    CLEANING_UP,

    DESTROYED
}
