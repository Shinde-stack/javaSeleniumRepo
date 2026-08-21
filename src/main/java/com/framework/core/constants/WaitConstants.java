package com.framework.core.constants;

/**
 * WaitConstants
 *
 * Default timeout values for explicit waits across the web layer.
 *
 * Flow:
 *   WaitManager.createWait() reads EXPLICIT_WAIT_SECONDS → applied to all ExpectedConditions calls
 *
 * Future: override per environment via EnvConfig instead of hardcoded constants.
 */
public final class WaitConstants {

	private WaitConstants() {
	    throw new UnsupportedOperationException(
	            "Utility class should not be instantiated.");
	}
	
    public static final int EXPLICIT_WAIT_SECONDS = 30;

    public static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;

    public static final int POLLING_INTERVAL_MILLIS = 100;
}
