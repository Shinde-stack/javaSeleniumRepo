package com.framework.core.config;

import com.framework.core.api.ApiContext;
import com.framework.core.context.ContextState;
import com.framework.core.context.DriverContext;
import com.framework.core.db.DbContext;
import com.framework.core.logging.TestLogger;
import com.framework.core.metadata.TestMetadataContext;

/**
 * ============================================================================
 * Class Name : ExecutionContext
 * ============================================================================
 *
 * Purpose: -------- Root execution context for a single test execution.
 *
 * Acts as a container that groups all execution-scoped contexts required during
 * framework execution.
 *
 * Responsibilities: ----------------- - Hold DriverContext - Hold ApiContext -
 * Hold DbContext - Hold TestMetadataContext - Hold current ContextState
 *
 * Non-Responsibilities: --------------------- This class MUST NOT:
 *
 * - Perform validation - Create drivers - Manage lifecycle transitions - Open
 * database connections - Execute API requests - Contain business logic
 *
 * Those responsibilities belong elsewhere:
 *
 * - ContextValidator - ContextLifecycleManager - DriverFactory -
 * ResourceManager
 *
 * Thread Safety: -------------- One ExecutionContext instance exists per test
 * thread through ExecutionContextHolder (ThreadLocal).
 *
 * Example:
 *
 * LoginTest ├── DriverContext ├── ApiContext ├── DbContext └──
 * TestMetadataContext
 *
 * ============================================================================
 */
public class ExecutionContext {

	/**
	 * Current lifecycle state of this execution context.
	 *
	 * Expected lifecycle:
	 *
	 * CREATED ↓ INITIALIZED ↓ RUNNING ↓ CLEANING_UP ↓ DESTROYED
	 */
	private ContextState state;

	/**
	 * Holds browser/mobile driver state.
	 */
	private final DriverContext driverContext;

	/**
	 * Holds API execution state.
	 *
	 * Examples: - Access Token - Refresh Token - Session Data
	 */
	private final ApiContext apiContext;

	/**
	 * Holds database execution state.
	 *
	 * Examples: - JDBC Connection - Transaction Data - Query Metadata
	 */
	private final DbContext dbContext;

	/**
	 * Holds execution metadata.
	 *
	 * Examples: - Correlation ID - Execution ID - Environment - Test Name
	 */
	private final TestMetadataContext metadataContext;

	/**
	 * Creates a new execution context.
	 *
	 * All sub-contexts are initialized immediately to avoid repetitive null checks
	 * throughout the framework.
	 *
	 * Default state = CREATED
	 *
	 * @param environment Execution environment Example: QA, STAGE, PROD
	 */
	
	
	/**
	 * Runtime framework configuration.
	 *
	 * Loaded once during startup and
	 * shared across framework components.
	 */
	private EnvConfig config;
	
	public ExecutionContext() {

		this.state = ContextState.CREATED;

		this.driverContext = new DriverContext();
		this.apiContext = new ApiContext();
		this.dbContext = new DbContext();
		this.metadataContext = new TestMetadataContext();
	}

	// -------------------------------------------------------------------------
	// Fluent Accessors
	// -------------------------------------------------------------------------

	/**
	 * Returns DriverContext.
	 *
	 * Usage: context.driver().getDriver();
	 */
	public DriverContext driver() {
		return driverContext;
	}

	/**
	 * Returns ApiContext.
	 *
	 * Usage: context.api().getAccessToken();
	 */
	public ApiContext api() {
		return apiContext;
	}

	/**
	 * Returns DbContext.
	 *
	 * Usage: context.db().getConnection();
	 */
	public DbContext db() {
		return dbContext;
	}

	/**
	 * Returns MetadataContext.
	 *
	 * Usage: context.metadata().getCorrelationId();
	 */
	public TestMetadataContext metadata() {
		return metadataContext;
	}

	// -------------------------------------------------------------------------
	// Standard Getters
	// -------------------------------------------------------------------------

	/**
	 * Standard getter for DriverContext.
	 */
	public DriverContext getDriverContext() {
		return driverContext;
	}

	/**
	 * Standard getter for ApiContext.
	 */
	public ApiContext getApiContext() {
		return apiContext;
	}

	/**
	 * Standard getter for DbContext.
	 */
	public DbContext getDbContext() {
		return dbContext;
	}

	/**
	 * Standard getter for MetadataContext.
	 */
	public TestMetadataContext getMetadataContext() {
		return metadataContext;
	}

	/**
	 * Returns current lifecycle state.
	 */
	public ContextState getState() {
		return state;
	}

	/**
	 * Updates execution state.
	 *
	 * NOTE: State transitions should ideally be performed through
	 * ContextLifecycleManager only.
	 */
	public void setState(ContextState state) {
		TestLogger.logStep("Execution Context ->set state method");

		this.state = state;
	}

	// -------------------------------------------------------------------------
	// Convenience Methods
	// -------------------------------------------------------------------------

	/**
	 * Returns true if driver has been initialized.
	 */
	public boolean hasDriver() {
		return driverContext.getDriver() != null;
	}

//    /**
//     * Returns true if API session exists.
//     */
//    public boolean hasApiSession() {
//
//        return apiContext != null
//                && apiContext.getAccessToken() != null;
//    }

	/**
	 * Returns true if database connection exists.
	 */
	public boolean hasDatabaseConnection() {

		return dbContext != null && dbContext.getConnection() != null;
	}

	public EnvConfig getConfig() {
	    return config;
	}

	public void setConfig(EnvConfig config) {
	    this.config = config;
	}
	
	// -------------------------------------------------------------------------
	// Debugging
	// -------------------------------------------------------------------------

	/**
	 * Useful for logging and troubleshooting.
	 */
	@Override
	public String toString() {

		return "ExecutionContext{" + "state=" + state + ", correlationId=" + metadataContext.getCorrelationId()
				+ ", testName='" + metadataContext.getTestName() + '\'' + '}';
	}

}
