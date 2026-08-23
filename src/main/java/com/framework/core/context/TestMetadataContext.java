package com.framework.core.context;

import java.util.UUID;

import com.framework.core.enums.EnvironmentType;

/**
 * ============================================================================
 * Class Name : TestMetadataContext
 * ============================================================================
 *
 * Holds metadata associated with a single test execution.
 *
 * Responsibilities ---------------- - Provide unique identifiers for tracing. -
 * Store execution metadata used by reporting, logging and screenshots. - Remain
 * independent of Selenium and TestNG.
 *
 * Lifecycle --------- ExecutionContext created │ ▼ Constructor generates: -
 * correlationId - executionId - startTime - threadId │ ▼
 * ContextLifecycleManager sets Environment │ ▼ TestListener sets Test Name │ ▼
 * Reporting / Logging / Screenshot consume metadata
 * ============================================================================
 */
public class TestMetadataContext {

	/**
	 * Unique identifier used for correlating logs, screenshots and reports.
	 * Generated once and never changes.
	 */
	private final String correlationId;

	/**
	 * Unique execution identifier. Useful when integrating with CI/CD systems
	 * later.
	 */
	private final String executionId;

	/**
	 * Timestamp when this ExecutionContext was created.
	 */
	private final long startTime;

	/**
	 * Thread executing this test. Automatically captured during construction.
	 */
	private final long threadId;

	/**
	 * Active execution environment.
	 */
	private EnvironmentType environment;

	/**
	 * Current test method name.
	 */
	private String testName;

	public TestMetadataContext() {

		this.correlationId = UUID.randomUUID().toString();

		this.executionId = UUID.randomUUID().toString();

		this.startTime = System.currentTimeMillis();

		this.threadId = Thread.currentThread().threadId();
	}

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

	public EnvironmentType getEnvironment() {
		return environment;
	}

	public void setEnvironment(EnvironmentType environment) {
		this.environment = environment;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	@Override
	public String toString() {

		return "TestMetadataContext{" + "correlationId='" + correlationId + '\'' + ", executionId='" + executionId
				+ '\'' + ", startTime=" + startTime + ", threadId=" + threadId + ", environment=" + environment
				+ ", testName='" + testName + '\'' + '}';
	}
}