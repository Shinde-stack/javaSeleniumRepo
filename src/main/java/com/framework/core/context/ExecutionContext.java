package com.framework.core.context;

import com.framework.core.config.EnvConfig;
import com.framework.core.logging.TestLogger;

/**
 * ============================================================================
 * Class Name : ExecutionContext
 * ============================================================================
 *
 * ROLE:
 * -----
 * Runtime container for a single test execution thread.
 *
 * It holds ALL execution-scoped objects:
 * - DriverContext (Web/Mobile driver state)
 * - ApiContext (API tokens, sessions)
 * - DbContext (DB connection/session)
 * - TestMetadataContext (test identity, correlation id)
 * - EnvConfig (runtime configuration snapshot)
 * - ContextState (lifecycle state)
 *
 * IMPORTANT RULE:
 * --------------
 * This class MUST NOT contain:
 * - Driver creation logic
 * - Wait logic
 * - Business logic
 * - Validation logic
 *
 * It is ONLY a DATA HOLDER.
 *
 * ============================================================================
 */
public class ExecutionContext {

    /**
     * Lifecycle state of execution.
     */
    private ContextState state;

    /**
     * Browser / mobile driver state container.
     */
    private final DriverContext driverContext;

    /**
     * API execution state container.
     */
//    private final ApiContext apiContext;

    /**
     * Database execution state container.
     */
//    private final DbContext dbContext;

    /**
     * Test metadata (test name, correlation id, etc.)
     */
    private final TestMetadataContext metadataContext;

    /**
     * Runtime configuration (browser, env, url, etc.)
     */
    private EnvConfig config;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    public ExecutionContext() {

        this.state = ContextState.CREATED;

        this.driverContext = new DriverContext();
//        this.apiContext = new ApiContext();
//        this.dbContext = new DbContext();
        this.metadataContext = new TestMetadataContext();

        // TEMP DEBUG LOG (REMOVE LATER)
        TestLogger.logStep("ExecutionContext created with default state: CREATED");
    }

    // =========================================================================
    // STATE
    // =========================================================================

    public ContextState getState() {
        return state;
    }

    public void setState(ContextState state) {

        // TEMP DEBUG LOG (REMOVE LATER)
        TestLogger.logStep("ExecutionContext state changed -> " + state);

        this.state = state;
    }

    // =========================================================================
    // CONTEXT OBJECTS
    // =========================================================================

    public DriverContext getDriverContext() {
        return driverContext;
    }

//    public ApiContext getApiContext() {
//        return apiContext;
//    }
//
//    public DbContext getDbContext() {
//        return dbContext;
//    }

    public TestMetadataContext getMetadataContext() {
        return metadataContext;
    }

    // =========================================================================
    // CONFIG
    // =========================================================================

    public EnvConfig getConfig() {
        return config;
    }

    public void setConfig(EnvConfig config) {

        // TEMP DEBUG LOG (REMOVE LATER)
        TestLogger.logStep("ExecutionContext config set");

        this.config = config;
    }

    // =========================================================================
    // HEALTH CHECK HELPERS
    // =========================================================================

    public boolean hasDriver() {
        return driverContext != null && driverContext.getDriver() != null;
    }

//    public boolean hasDatabase() {
//        return dbContext != null && dbContext.getConnection() != null;
//    }
//
//    public boolean hasApiSession() {
//        return apiContext != null && apiContext.getAccessToken() != null;
//    }

    // =========================================================================
    // DEBUG
    // =========================================================================

    @Override
    public String toString() {
    	
    	String info = "ExecutionContext{" +
                "state=" + state +
                ", testName=" + metadataContext.getTestName() +
                ", correlationId=" + metadataContext.getCorrelationId() +
                '}';
    	
    	   // TEMP DEBUG LOG (REMOVE LATER)
        TestLogger.logStep("execution context info =>"+info);
        TestLogger.logStep("metadataContext =>"+metadataContext);

    	
        return info;
    }
}