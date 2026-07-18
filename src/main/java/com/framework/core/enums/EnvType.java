package com.framework.core.enums;

/**
 * EnvType
 *
 * Named deployment environments for config and metadata.
 *
 * Flow (planned):
 *   EnvResolver → maps to config/{env}.properties → stored in TestMetadataContext
 *
 * Currently resolved as a plain string; this enum is reserved for typed env handling.
 */
public enum EnvType {
    QA, STAGE, PROD
}
