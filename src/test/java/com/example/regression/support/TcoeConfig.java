package com.example.regression.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 1. Reads tcoe.properties and takes the "env" field (e.g. MAL001_SIT)
 * 2. Loads environment_<env>.properties (e.g. environment_MAL001_SIT.properties)
 * 3. Serves values from that file. A -D system property of the same name wins.
 */
public final class TcoeConfig {
    private static final String ENV_FIELD = "env";
    private static final String ENV_NAME;
    private static final Properties ENV_PROPS = new Properties();

    static {
        Properties tcoe = load("tcoe.properties");
        ENV_NAME = System.getProperty(ENV_FIELD, tcoe.getProperty(ENV_FIELD, "")).trim();
        if (ENV_NAME.isEmpty()) {
            throw new IllegalStateException("tcoe.properties has no value for '" + ENV_FIELD + "'");
        }
        String fileName = "environment_" + ENV_NAME + ".properties";
        try {
            ENV_PROPS.putAll(load(fileName));
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                    "tcoe.properties says env=" + ENV_NAME + " but " + fileName + " was not found", e);
        }
    }

    private TcoeConfig() {
    }

    private static Properties load(String resource) {
        try (InputStream in = TcoeConfig.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException(resource + " not found on the classpath");
            }
            Properties p = new Properties();
            p.load(in);
            return p;
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + resource, e);
        }
    }

    public static String get(String key) {
        String value = System.getProperty(key, ENV_PROPS.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing '" + key + "' in environment_" + ENV_NAME + ".properties");
        }
        return value.trim();
    }

    public static String baseUrl() {
        return get("base.url");
    }

    /** e.g. MAL001_SIT */
    public static String environmentName() {
        return ENV_NAME;
    }
}
