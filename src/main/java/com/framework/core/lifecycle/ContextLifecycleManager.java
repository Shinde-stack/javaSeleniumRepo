package com.framework.core.lifecycle;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.framework.core.config.ConfigLoader;
import com.framework.core.config.EnvConfig;
import com.framework.core.context.ContextState;
import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.driver.DriverManager;
import com.framework.core.logging.TestLogger;
import com.framework.core.reporting.ReportManager;

public class ContextLifecycleManager {

    private final DriverManager driverManager = new DriverManager();

    public void startTest() {

        // 1. LOAD CONFIG
        EnvConfig config = new ConfigLoader().load();

        // 2. CREATE CONTEXT
        ExecutionContext context = new ExecutionContext();
        ExecutionContextHolder.setContext(context);

        // 3. DRIVER INIT
        driverManager.initializeDriver(context,
                config.getBrowserType(),
                config.isHeadless());

        WebDriver driver = context.getDriverContext().getDriver();

        // 4. OPEN APP
        driver.get(config.getBaseUrl());

        // 5. REPORT INIT (CRITICAL ORDER FIX)
        ExtentTest test =
                ReportManager.createTest(Thread.currentThread().getName());

        ReportManager.setTest(test);

        // 6. STATE UPDATE
        context.setState(ContextState.RUNNING);

        TestLogger.logStep("Lifecycle initialized");
    }

    public void endTest() {

        ExecutionContext context =
                ExecutionContextHolder.getContext();

        try {
            // 1. DRIVER CLEANUP
            driverManager.quitDriver(context);

            // 2. STATE UPDATE
            context.setState(ContextState.DESTROYED);

        } finally {

            // 3. REMOVE THREAD DATA
            ReportManager.removeTest();
            ExecutionContextHolder.clear();
        }
    }
}