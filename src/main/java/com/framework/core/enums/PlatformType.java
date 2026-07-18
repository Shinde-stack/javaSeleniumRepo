package com.framework.core.enums;

/**
 * PlatformType
 *
 * Identifies which automation layer a test or context targets.
 *
 * Flow (planned):
 *   ExecutionContext selects platform → lifecycle initializes WEB (driver) / API (client) / MOBILE (Appium)
 *
 * Only WEB is implemented today.
 */
public enum PlatformType {
    WEB, API, MOBILE

}
