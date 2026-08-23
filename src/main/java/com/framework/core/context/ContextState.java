package com.framework.core.context;


/**
 * ContextState
 *
 * Lifecycle state of a single test's ExecutionContext.
 *
 * Flow:
 *   CREATED → INITIALIZED (config loaded) → RUNNING (driver ready) → DESTROYED (cleanup complete)
 *
 * CLEANING_UP is reserved for multi-step teardown (API/DB sessions) before DESTROYED.
 */
public enum ContextState {

    CREATED,

    INITIALIZED,

    RUNNING,

    CLEANING_UP,

    DESTROYED
}
