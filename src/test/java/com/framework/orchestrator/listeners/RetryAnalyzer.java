package com.framework.orchestrator.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.framework.core.logging.TestLogger;

/**
 * Simple retry mechanism for flaky tests.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int count = 0;
    private static final int MAX_RETRY = 1;

    @Override
    public boolean retry(ITestResult result) {
 		TestLogger.logStep("RetryAnalyzer --->>> retry =>"+count);

        if (count < MAX_RETRY) {
            count++;
            return true;
        }

        return false;
    }
    
}
