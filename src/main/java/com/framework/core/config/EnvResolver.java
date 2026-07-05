package com.framework.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.framework.core.logging.TestLogger;

public class EnvResolver {

    /**
     * Resolves runtime environment.
     *
     * Priority order:
     * 1. System property (-Denv=qa)
     * 2. Default fallback (qa)
     */
	
    public static String resolve() {

    	   // TEMP DEBUG LOG (REMOVE LATER)
        TestLogger.logStep("temp ---EnvResolver.resolve");

        // 1. JVM argument (highest priority)
        String env = System.getProperty("env");

        // 2. OS / CI environment variable
        if (env == null || env.isBlank()) {
            env = System.getenv("ENV");
        }

        // 3. fallback default
        if (env == null || env.isBlank()) {
            env = "qa";
        }
        
        return env.toLowerCase();
    }
}
