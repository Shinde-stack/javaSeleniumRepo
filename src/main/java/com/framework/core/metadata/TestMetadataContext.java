package com.framework.core.metadata;

import java.util.UUID;

/**
 * Stores immutable execution metadata for a single test run.
 * Used to correlate logs across UI, API, and Database layers.
 */
public class TestMetadataContext {

    private final String correlationId;
    private final long startTime;
    private String environment;
    private String testName;
    private long threadId;
    private String executionId;

    /**
     * Initializes a new metadata context for a specific environment.
     *
     * @param environment The target execution environment (e.g., STG, PROD).
     */
    public TestMetadataContext() {
        this.correlationId = UUID.randomUUID().toString();
        this.startTime = System.currentTimeMillis();
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
    

}
