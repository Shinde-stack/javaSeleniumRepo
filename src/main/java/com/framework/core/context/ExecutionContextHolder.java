package com.framework.core.context;

import com.framework.core.config.ExecutionContext;
import com.framework.core.validation.ContextValidator;

/**
 * Thread-safe ExecutionContext holder.
 *
 * Key design principle: Context MUST be explicitly initialized by framework
 * lifecycle. NOT auto-created silently.
 */
public final class ExecutionContextHolder {

	private ExecutionContextHolder() {
	}

	private static final ThreadLocal<ExecutionContext> CONTEXT = new ThreadLocal<>();

	/**
	 * Explicitly set context for current thread.
	 *
	 * Called during FrameworkBootstrap / BaseTest setup.
	 */
	public static void setContext(ExecutionContext context) {
		if (context == null) {
			throw new IllegalArgumentException("ExecutionContext cannot be null");
		}
		CONTEXT.set(context);
	}

	/**
	 * Get current thread context.
	 *
	 * Throws if not initialized → prevents hidden failures.
	 */
	public static ExecutionContext getContext() {

		ExecutionContext context = CONTEXT.get();
		
		ContextValidator.validate(context);
		
		return context;
	}

	/**
	 * Clear context after test execution.
	 */
	public static void clear() {
		CONTEXT.remove();// CRITICAL: prevents thread reuse leakage
	}
}
