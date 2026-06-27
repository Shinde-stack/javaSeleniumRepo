package com.framework.orchestrator.context;

import org.openqa.selenium.WebDriver;

import com.framework.core.config.ConfigLoader;
import com.framework.core.context.ExecutionContext;
import com.framework.core.context.ExecutionContextHolder;
import com.framework.core.driver.BrowserType;
import com.framework.core.driver.DriverFactory;

/**
 * ============================================================================
 * Class Name : ContextInitializer
 * ============================================================================
 *
 * Responsibility:
 * Creates and initializes ExecutionContext
 * for current test thread.
 *
 * Initialization Flow:
 *
 * ExecutionContext
 *        ↓
 * DriverFactory
 *        ↓
 * DriverContext
 *        ↓
 * ThreadLocal Storage
 *
 * ============================================================================
 */
public final class ContextInitializer {

//    private ContextInitializer() {
//    }
//
//    /**
//     * Creates and initializes context.
//     *
//     * @param environment QA / STAGE / PROD
//     */
//    public static void initialize(String environment) {
//
//        // Create root execution context
//        ExecutionContext context =
//                new ExecutionContext(environment);
//
//        // Create browser driver
//        
//        BrowserType browserType =
//                BrowserType.valueOf(
//                        ConfigLoader.get("browser")
//                                .toUpperCase());
//
//        boolean headless =
//                Boolean.parseBoolean(
//                        ConfigLoader.get("headless"));
//        
//        WebDriver driver =
//                DriverFactory.createDriver(
//                        browserType,
//                        headless);
//        
//        
//        // Store driver in DriverContext
//        context.driver().setDriver(driver);
//
//        // Store context in ThreadLocal
//        ExecutionContextHolder.setContext(context);
//    }
}
