package com.framework.core.assertion;

/**
 * Defines assertion execution behavior.
 *
 * HARD → Stops test immediately
 * SOFT → Collects failure and continues execution
 */
public enum Severity {
    HARD,
    SOFT
}