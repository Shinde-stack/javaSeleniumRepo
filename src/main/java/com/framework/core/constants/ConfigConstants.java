package com.framework.core.constants;

import java.util.List;

/**
 * Configuration related constants.
 */
public final class ConfigConstants {

	private ConfigConstants() {
	}

//	public static final String CONFIG_FILE = "config/framework.properties";

    public static final String CONFIG_PATH =
            "config/";

    public static final String CONFIG_EXTENSION =
            ".properties";
    
	/**
	 * Mandatory properties required for framework startup.
	 */
	public static final List<String> MANDATORY_PROPERTIES = List.of("browser", "headless", "baseUrl");
	
	
}