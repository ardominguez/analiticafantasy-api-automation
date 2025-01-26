package com.analiticafantasy.fwk.api.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;


public class ConfigurationsLoader {

    public static Map<String, Object> config;

    static {
        String configFileName = String.format("config/configurations-%s.yml", getEnvironment());

        Yaml yaml = new Yaml();
        try (InputStream in = ConfigurationsLoader.class.getClassLoader().getResourceAsStream(configFileName)) {
            if (Objects.isNull(in)) {
                throw new RuntimeException("Configuration file not found: " + configFileName);
            }
            config = yaml.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Configuration file not found: " + configFileName, e);
        }
    }

    public static String getConfigValue(String key) {
        String[] keys = key.split("\\.");
        Map<String, Object> currentMap = config;

        for (int i = 0; i < keys.length - 1; i++) {
            currentMap = (Map<String, Object>) currentMap.get(keys[i]);
            if (Objects.isNull(currentMap)) {
                return null;
            }
        }
        return (String) currentMap.get(keys[keys.length - 1]);
    }

    private static String getEnvironment() {
        return System.getProperty("env", "production");
    }
}
