package com.framework.core.context;

import java.util.UUID;

/**
 * TestMetadataContext
 *
 * Immutable and mutable metadata for correlating logs, screenshots, and reports per test run.
 *
 * Flow:
 *   ExecutionContext created → correlationId assigned → (planned) listener sets testName/environment
 *
 * correlationId is generated once per context and intended for cross-layer tracing (UI, API, DB).
 */
public class TestMetadataContext {

    private final String correlationId;
    private final long startTime;
    private String environment;
    private String testName;
    private long threadId;
    private String executionId;

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
