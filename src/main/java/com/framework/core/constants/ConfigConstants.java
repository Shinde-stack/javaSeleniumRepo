package com.framework.core.constants;

import java.io.File;
import java.util.List;

/**
 * ============================================================================
 * Class Name : ConfigConstants
 * ============================================================================
 *
 * Purpose:
 * --------
 * Centralized constants related to framework configuration.
 *
 * Contains:
 * - Configuration file locations
 * - File extensions
 * - Required property names
 *
 * Note:
 * -----
 * Only immutable framework constants belong here.
 * Runtime configuration values belong in EnvConfig.
 * ============================================================================
 */
public final class ConfigConstants {


	private ConfigConstants() {
    	//temp...remove below line later as class is final so no one can create object anyway
        throw new UnsupportedOperationException("Utility class SHOULD NOT BE INITIATED");
    }
    
    
    /**
     * Root configuration directory.
     */
    public static final String CONFIG_DIRECTORY = "config"+File.separator;

    /**
     * Configuration file extension.
     */
    public static final String CONFIG_EXTENSION = ".properties";

//    /**
//     * Default framework configuration file.
//     */
//    public static final String FRAMEWORK_CONFIG_FILE =
//            CONFIG_DIRECTORY + "/framework" + CONFIG_EXTENSION;

    /**
     * Required configuration properties.
     */
    public static final List<String> MANDATORY_PROPERTIES = List.of(
            "browser",
            "headless",
            "baseUrl"
    );
}