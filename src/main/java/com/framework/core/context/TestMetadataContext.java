package com.framework.core.context;

import java.util.UUID;

import com.framework.core.enums.EnvironmentType;

/**
 * ============================================================================
 * Class Name : TestMetadataContext
 * ============================================================================
 *
 * Holds metadata for ONE test-method execution.
 *
 * Consumed by:
 * - Reporting
 * - Logging
 * - Screenshots
 * - CI/CD diagnostics
 *
 * This class contains NO:
 * - TestNG objects
 * - Selenium objects
 * - Reporting objects
 *
 * ============================================================================
 */
public class TestMetadataContext {

    /**
     * Unique identifier for correlating all artifacts
     * belonging to this execution.
     */
    private final String correlationId;

    /**
     * Unique identifier for this execution instance.
     */
    private final String executionId;

    /**
     * Timestamp when this execution context was created.
     */
    private final long startTime;

    /**
     * JVM thread ID executing this test.
     */
    private final long threadId;

    /**
     * JVM thread name executing this test.
     *
     * Useful for parallel execution diagnostics.
     */
    private final String threadName;

    /**
     * Active execution environment.
     */
    private EnvironmentType environment;

    /**
     * TestNG <test> name from testng.xml.
     *
     * Example:
     *
     * <test name="Web Tests">
     */
    private String testngTestName;

    /**
     * Java test class name.
     *
     * Example:
     * LoginTest
     */
    private String className;

    /**
     * Test method name.
     *
     * Example:
     * verifyValidLogin
     */
    private String methodName;

    public TestMetadataContext() {

        this.correlationId =
                UUID.randomUUID().toString();

        this.executionId =
                UUID.randomUUID().toString();

        this.startTime =
                System.currentTimeMillis();

        Thread currentThread =
                Thread.currentThread();

        this.threadId =
                currentThread.threadId();

        this.threadName =
                currentThread.getName();
    }

    // ========================================================================
    // GETTERS
    // ========================================================================

    public String getCorrelationId() {
        return correlationId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getThreadId() {
        return threadId;
    }

    public String getThreadName() {
        return threadName;
    }

    public EnvironmentType getEnvironment() {
        return environment;
    }

    public String getTestngTestName() {
        return testngTestName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    // ========================================================================
    // SETTERS
    // ========================================================================

    public void setEnvironment(
            EnvironmentType environment) {

        this.environment = environment;
    }

    public void setTestngTestName(
            String testngTestName) {

        this.testngTestName = testngTestName;
    }

    public void setClassName(
            String className) {

        this.className = className;
    }

    public void setMethodName(
            String methodName) {

        this.methodName = methodName;
    }

    // ========================================================================
    // DEBUG / LOGGING
    // ========================================================================

    @Override
    public String toString() {

        return "TestMetadataContext{" +
                "correlationId='" + correlationId + '\'' +
                ", executionId='" + executionId + '\'' +
                ", startTime=" + startTime +
                ", threadId=" + threadId +
                ", threadName='" + threadName + '\'' +
                ", environment=" + environment +
                ", testngTestName='" + testngTestName + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                '}';
    }
}