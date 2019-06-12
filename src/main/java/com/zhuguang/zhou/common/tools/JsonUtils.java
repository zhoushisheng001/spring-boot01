//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuguang.zhou.common.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public JsonUtils() {
    }

    public static <T> T serializable(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        } else {
            try {
                return mapper.readValue(json, clazz);
            } catch (IOException var3) {
                LOGGER.error(var3.getMessage());
                return null;
            }
        }
    }

    public static <T> T serializable(String json, TypeReference reference) {
        if (StringUtils.isEmpty(json)) {
            return null;
        } else {
            try {
                return mapper.readValue(json, reference);
            } catch (IOException var3) {
                LOGGER.error(var3.getMessage());
                return null;
            }
        }
    }

    public static String deserializer(Object json) {
        if (json == null) {
            return null;
        } else {
            try {
                return mapper.writeValueAsString(json);
            } catch (JsonProcessingException var2) {
                LOGGER.error(var2.getMessage());
                return null;
            }
        }
    }

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
