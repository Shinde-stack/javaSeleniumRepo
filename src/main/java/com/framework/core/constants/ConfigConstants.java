package com.framework.core.constants;

import java.io.File;
import java.util.List;

/**
 * ConfigConstants
 *
 * Immutable paths and required property keys for environment configuration.
 *
 * Flow:
 *   EnvResolver.resolve() → CONFIG_DIRECTORY + env + CONFIG_EXTENSION → ConfigLoader validates MANDATORY_PROPERTIES
 */
public final class ConfigConstants {


	private ConfigConstants() {
        throw new UnsupportedOperationException("Utility class SHOULD NOT BE INITIATED");
    }
    
    
    public static final String CONFIG_DIRECTORY = "config"+File.separator;

    public static final String CONFIG_EXTENSION = ".properties";

    public static final String default_fallback_env = "QA";
    public static final Boolean isDefaultFallbackEnvExpected = true;

    
    public static final List<String> MANDATORY_PROPERTIES = List.of(
            "browser",
            "headless",
            "baseUrl"
    );
}
