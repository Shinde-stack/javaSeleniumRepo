package com.framework.core.config;

public class EnvResolver {
	
	
    /**
     * Resolves runtime environment.
     *
     * Priority order:
     * 1. System property (-Denv=qa)
     * 2. Default fallback (qa)
     */
	
    public static String resolve() {

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
