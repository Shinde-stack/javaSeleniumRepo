package com.framework.core.context;

import com.framework.core.config.EnvConfig;
import com.framework.core.logging.TestLogger;

/**
 * ExecutionContext
 *
 * Per-thread data container for one test run. Holds driver, config, metadata, and lifecycle state.
 *
 * Flow:
 *   ContextLifecycleManager.initializeContext() → populate config/state → bind via ExecutionContextHolder
 *   → pages and services read from holder → cleanup destroys state
 *
 * Pure data holder: no driver creation, waits, assertions, or business logic.
 */
public class ExecutionContext {

    private ContextState state;

    private final DriverContext driverContext;

//    private final ApiContext apiContext;

//    private final DbContext dbContext;

    private final TestMetadataContext metadataContext;

    private EnvConfig config;

    public ExecutionContext() {

        this.state = ContextState.CREATED;

        this.driverContext = new DriverContext();
//        this.apiContext = new ApiContext();
//        this.dbContext = new DbContext();
        this.metadataContext = new TestMetadataContext();

        TestLogger.logStep("temp --- ExecutionContext ,,, CONSTRUCTOR ,,, created with default state: CREATED");
    }

    public ContextState getState() {
        return state;
    }

    public void setState(ContextState state) {

        TestLogger.logStep("temp --- ExecutionContext state changed -> " + state);

        this.state = state;
    }

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

    public EnvConfig getConfig() {
        return config;
    }

    public void setConfig(EnvConfig config) {

        TestLogger.logStep("temp --- ExecutionContext config set");

        this.config = config;
    }

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

    @Override
    public String toString() {
    	
    	String info = "ExecutionContext{" +
                "state=" + state +
                ", testName=" + metadataContext.getTestName() +
                ", correlationId=" + metadataContext.getCorrelationId() +
                '}';
    	
        TestLogger.logStep("temp --- execution context info =>"+info);
        TestLogger.logStep("temp --- metadataContext =>"+metadataContext);

    	
        return info;
    }
}
