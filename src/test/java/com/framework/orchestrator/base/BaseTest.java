package com.framework.orchestrator.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import com.framework.core.config.ConfigLoader;
import com.framework.core.config.EnvConfig;
import com.framework.core.context.*;
import com.framework.core.driver.DriverManager;
import com.framework.core.lifecycle.ContextLifecycleManager;
import com.framework.core.reporting.ReportConstants;
import com.framework.core.reporting.ReportManager;
import com.framework.core.logging.TestLogger;

public abstract class BaseTest {

    protected WebDriver driver;
    protected ExecutionContext context;

    private final ContextLifecycleManager lifecycle =
            new ContextLifecycleManager();

    // =========================================================
    // BEFORE SUITE (RUNS ONCE)
    // =========================================================
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {

        TestLogger.logStep("BEFORE SUITE - Initialization started");

        EnvConfig config = new ConfigLoader().load();

        // IMPORTANT:
        // Initialize ExtentReports ONLY ONCE
        ReportManager.initReport(ReportConstants.REPORT_DIR);

        TestLogger.logStep("BEFORE SUITE - Report initialized");
    }

    // =========================================================
    // BEFORE METHOD (RUNS PER TEST)
    // =========================================================
    @BeforeMethod(alwaysRun = true)
    public void setUp() {

        TestLogger.logStep("BEFORE METHOD - Test setup started");

        lifecycle.startTest();

        context = ExecutionContextHolder.getContext();
        driver = context.getDriverContext().getDriver();
    }

    // =========================================================
    // AFTER METHOD (RUNS PER TEST)
    // =========================================================
    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        TestLogger.logStep("AFTER METHOD - Test teardown started");

        lifecycle.endTest();
    }

    // =========================================================
    // AFTER SUITE (RUN ONCE)
    // =========================================================
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {

        TestLogger.logStep("AFTER SUITE - flushing report");

        ReportManager.flush();
    }
}