package com.analiticafantasy.fwk.api.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class JsonUtil {

    private final static ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String serialize(T source) {
        if (source == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        return deserialize(json, j -> OBJECT_MAPPER.readValue(j, clazz));
    }

    public static <T> T deserialize(String json, TypeReference<T> type) {
        return deserialize(json, j -> OBJECT_MAPPER.readValue(j, type));
    }

    @FunctionalInterface
    private static interface JacksonFunction<T> {
        public T apply(String str) throws IOException;
    }

    private static <T> T deserialize(String json, JacksonFunction<T> fn) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return fn.apply(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SimpleModule getModule() {
        // Register custom serializers
        SimpleModule module = new SimpleModule("DefaultModule", new Version(0, 0, 1, null, null, null));
        return module;
    }

    public static boolean isValidJson(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
