package com.framework.orchestrator.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.framework.core.config.ConfigLoader;
import com.framework.core.config.EnvConfig;
import com.framework.core.config.ExecutionContext;
import com.framework.core.context.ContextState;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.driver.DriverManager;
import com.framework.core.logging.TestLogger;
import com.framework.core.reporting.ReportManager;
import com.framework.core.validation.ContextValidator;

/**
 * ============================================================================
 * Class Name : BaseTest
 * ============================================================================
 *
 * Purpose: -------- Parent class for all test classes.
 *
 * Responsibilities: ----------------- 1. Create ExecutionContext for current
 * test 2. Store context in ThreadLocal 3. Load framework configuration 4.
 * Initialize browser driver 5. Open application URL 6. Cleanup resources after
 * execution
 *
 * Why this class exists: ---------------------- Without BaseTest:
 *
 * Every test would need to repeat:
 *
 * - Create context - Create driver - Load configuration - Open URL - Quit
 * browser
 *
 * BaseTest centralizes this logic.
 *
 * Example:
 *
 * LoginTest SearchTest CheckoutTest
 *
 * All inherit BaseTest.
 *
 * ============================================================================
 */
public abstract class BaseTest {

	/**
	 * Current test execution context.
	 *
	 * Contains: - DriverContext - ApiContext - DbContext - MetadataContext
	 */
	protected ExecutionContext context;

	/**
	 * Browser driver for current test.
	 *
	 * Convenience reference.
	 *
	 * Actual ownership remains inside: context.getDriverContext()
	 */
	protected WebDriver driver;

	/**
	 * Handles driver lifecycle.
	 *
	 * Responsibilities: - Create driver - Return driver - Quit driver
	 */
	private DriverManager driverManager;

	/**
	 * Executes before every test method.
	 *
	 * Example:
	 *
	 * LoginTest.testA() LoginTest.testB()
	 *
	 * setUp() runs before BOTH tests.
	 */
	@BeforeMethod(alwaysRun = true)
	public void setUp() {

		TestLogger.logStep("Base Test ->setUp method");

		/*
		 * ========================================================= STEP 1 Load
		 * framework configuration
		 * =========================================================
		 *
		 * Reads: - browser - baseUrl - headless - environment
		 *
		 * Example:
		 *
		 * browser=CHROME headless=false baseUrl=https://demo.com
		 */
		EnvConfig config = new ConfigLoader().load();
		TestLogger.logStep("Base Test - config -"+config);

		/*
		 * ========================================================= STEP 2 Create
		 * execution context =========================================================
		 *
		 * One context per test execution.
		 *
		 * Contains all runtime information required by framework.
		 */
		context = new ExecutionContext();
		TestLogger.logStep("Base Test - execution context -"+context);

        context.setState(ContextState.CREATED);
        
		/*
		 * ========================================================= STEP 3 Store
		 * context in ThreadLocal attach to ThreadLocal immediately
		 * =========================================================
		 *
		 * Makes context available globally within current test thread.
		 *
		 * Later:
		 *
		 * BasePage ElementActions Listeners
		 *
		 * can access:
		 *
		 * ExecutionContextHolder.getContext()
		 */
		ExecutionContextHolder.setContext(context);
		TestLogger.logStep("Base Test -------init--------setContext");

		// 3b. MARK STATE
		context.setState(ContextState.INITIALIZED);

	    // -----------------------------------------------------
	    // PRE-FLIGHT VALIDATION (NEW STEP)
	    // -----------------------------------------------------
	    ContextValidator.validate(context);
		TestLogger.logStep("Base Test ->validate context");

		/*
		 * ========================================================= STEP 4 Create
		 * DriverManager =========================================================
		 *
		 * DriverManager controls driver lifecycle.
		 */
		driverManager = new DriverManager();
		TestLogger.logStep("Base Test - driverManager -"+driverManager);

		/*
		 * ========================================================= STEP 5 Initialize
		 * browser =========================================================
		 *
		 * Internally:
		 *
		 * DriverFactory ↓ ChromeDriver ↓ Stored inside DriverContext
		 */
		driverManager.initializeDriver(context, config.getBrowserType(), config.isHeadless());
		TestLogger.logStep("Base Test - driverManager INITILIZED driver");

		/*
		 * ========================================================= STEP 6 Get driver
		 * reference =========================================================
		 *
		 *	 
		 *
		 */
		
//		 * Driver already exists inside context.
//		 *
//		 * This variable is only a shortcut.
//	
//		driver = driverManager.getDriver(context);

	    driver = context.getDriverContext().getDriver();
		TestLogger.logStep("Base Test - driver -"+driver);

	    context.setState(ContextState.RUNNING);
	    
		/*
		 * ========================================================= STEP 7 Open
		 * application =========================================================
		 */
		driver.get(config.getBaseUrl());
		TestLogger.logStep("Base Test - get base url from config ---"+config.getBaseUrl());

	}

	/**
	 * ============================================================================
	 * TEST CLEANUP PHASE
	 * ============================================================================
	 *
	 * Executes after every test method.
	 *
	 * Responsibilities:
	 * -----------------
	 * 1. Update execution state
	 * 2. Close browser session
	 * 3. Release framework resources
	 * 4. Clear ThreadLocal storage
	 *
	 * Why important?
	 * --------------
	 * Prevents:
	 * - Browser leaks
	 * - Thread contamination
	 * - Memory leaks
	 * - Parallel execution issues
	 *
	 * Execution Flow:
	 * ---------------
	 *
	 * RUNNING
	 *    ↓
	 * CLEANING_UP
	 *    ↓
	 * Driver Quit
	 *    ↓
	 * DESTROYED
	 *    ↓
	 * ThreadLocal Clear
	 *
	 * ============================================================================
	 */
	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		TestLogger.logStep("Base Test ->tearDown method");

	    try {

	        // ---------------------------------------------------------
	        // STEP 1
	        // Mark execution entering cleanup phase
	        //
	        // Useful for:
	        // - Debugging
	        // - Reporting
	        // - Future listeners
	        // ---------------------------------------------------------
	        if (context != null) {
				TestLogger.logStep("Base Test ->tear down - context not null");
	            context.setState(ContextState.CLEANING_UP);
	        }

	        // ---------------------------------------------------------
	        // STEP 2
	        // Close browser session
	        //
	        // Internally:
	        //
	        // DriverManager
	        //      ↓
	        // DriverContext
	        //      ↓
	        // WebDriver.quit()
	        //
	        // Safe even if driver initialization failed.
	        // ---------------------------------------------------------
	        if (driverManager != null && context != null) {
				TestLogger.logStep("Base Test -tear down - driver manager and context not null");

	            driverManager.quitDriver(context);
	        }

	        // ---------------------------------------------------------
	        // STEP 3
	        // Mark execution fully completed
	        //
	        // Indicates framework resources
	        // have been successfully released.
	        // ---------------------------------------------------------
	        if (context != null) {
	    		TestLogger.logStep("Base Test - tear own - context not null before destroy");

	            context.setState(ContextState.DESTROYED);

	        }

	    } finally {
			TestLogger.logStep("Base Test - tear down finally");

	        // ---------------------------------------------------------
	        // STEP 4
	        // Remove ExecutionContext from ThreadLocal
	        //
	        // CRITICAL:
	        //
	        // Prevents:
	        // - Memory leaks
	        // - Context reuse between tests
	        // - Parallel execution contamination
	        //
	        // Always executed even if browser quit fails.
	        // ---------------------------------------------------------
	        ExecutionContextHolder.clear();
			TestLogger.logStep("Base Test - finally - execution context holder CLEAR");

	        // ---------------------------------------------------------
	        // STEP 5 (Future)
	        //
			
	 		TestLogger.logStep("Base Test - ReportManager.removeTest()");

	         ReportManager.removeTest();

	        //
	        // When reporting is fully integrated,
	        // ThreadLocal ExtentTest cleanup should happen here
	        // or in Listener.
	        // ---------------------------------------------------------
	    }
	}
	
	
	
	
//	1. High-level lifecycle (mental model)
//
//	Each test thread must follow this exact sequence:
//
//	1. Create Context (pure)
//	2. Bind ThreadLocal
//	3. Load Config
//	4. Create Driver
//	5. Attach Driver to Context
//	6. Set state = INITIALIZED → RUNNING
//	7. Navigate
//	8. Execute test
//	9. Cleanup

}
