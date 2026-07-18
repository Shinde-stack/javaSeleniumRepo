package com.framework.core.assertion;

/**
 * Severity
 *
 * Controls how AssertionEngine handles a failed check.
 *
 * Flow:
 *   HARD → log failure → throw AssertionError immediately (test stops)
 *   SOFT → log failure → collect message → continue → assertAll() throws if any soft failures exist
 */
public enum Severity {
    HARD,
    SOFT
}
