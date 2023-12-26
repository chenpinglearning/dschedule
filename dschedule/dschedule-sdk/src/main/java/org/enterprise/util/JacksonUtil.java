package org.enterprise.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JacksonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //ObjectMapper忽略多余字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public static String obj2String(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("obj2String error", e);
        }

        return null;
    }


    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (str == null || "".equals(str) || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            logger.error("string2Obj error", e);
        }

        return null;
    }


    /**
     * @param str
     * @param collectionClass
     * @param elementClass
     * @param <T>
     * @return
     */
    public static <T> T string2Collection(String str, Class<?> collectionClass, Class<?> elementClass) {
        if(str == null || "".equals(str) || collectionClass == null || elementClass == null){
            return null;
        }

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            logger.error("Parse String to Object error", e);
        }

        return null;
    }
}