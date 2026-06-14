package com.framework.core.validation;

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