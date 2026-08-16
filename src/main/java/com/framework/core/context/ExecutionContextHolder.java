package com.framework.core.context;

import com.framework.core.excepions.FrameworkException;

/**
 * ExecutionContextHolder
 *
 * ThreadLocal registry for the active ExecutionContext on the current test
 * thread.
 *
 * Flow: ContextLifecycleManager.initializeContext() → setContext() → BasePage /
 * TestLogger / ScreenshotService → getContext() → cleanupContext() →
 * removeContext()
 *
 * Throws IllegalStateException if getContext() is called before initialization.
 */
public final class ExecutionContextHolder {

	
	private ExecutionContextHolder() {
	    throw new UnsupportedOperationException(
	        "ExecutionContextHolder - Utility class should not be instantiated.");
	}

	private static final ThreadLocal<ExecutionContext> CONTEXT = new ThreadLocal<>();

	/**
	 * Stores the ExecutionContext for the current thread.
	 */
	public static void set(ExecutionContext context) {

		if (context == null) {
			throw new IllegalArgumentException("ExecutionContext cannot be null");
		}

		CONTEXT.set(context);
	}

	/**
	 * Returns the current thread's ExecutionContext.
	 *
	 * @throws FrameworkException
	 *         if no context has been initialized.
	 */
	public static ExecutionContext get() {

		ExecutionContext context = CONTEXT.get();

		if (context == null) {
			throw new FrameworkException(
					"ExecutionContext not initialized for thread: " + Thread.currentThread().getName());
		}

		return context;
	}

	/**
	 * Removes the current thread's ExecutionContext.
	 *
	 * Must be called in finally blocks.
	 */
	public static void clear() {
		CONTEXT.remove();
	}
}
