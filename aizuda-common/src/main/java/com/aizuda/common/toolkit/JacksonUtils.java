package com.aizuda.common.toolkit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

/**
 * JSON 字符与对像转换
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-09
 */
@Slf4j
public class JacksonUtils {
    private static ObjectMapper OBJECT_MAPPER;

    /**
     * 获取 Jackson ObjectMapper 为空则初始化
     *
     * @return {@link ObjectMapper}
     */
    public static ObjectMapper getObjectMapper() {
        if (null == OBJECT_MAPPER) {
            OBJECT_MAPPER = new ObjectMapper();
            OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            // 忽略 transient 关键词属性
            OBJECT_MAPPER.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
            // Long 转为 String 防止 js 丢失精度
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            OBJECT_MAPPER.registerModule(simpleModule);
            OBJECT_MAPPER.registerModule(new JavaTimeModule());
        }
        return OBJECT_MAPPER;
    }

    /**
     * 把JavaBean转换为json字符串
     */
    public static String toJSONString(Object object) {
        try {
            return toJsonThrows(object);
        } catch (Exception e) {
            log.error("toJSONString error.", e);
        }
        return null;
    }

    /**
     * 把JavaBean转换为json字符串，抛出异常
     */
    public static String toJsonThrows(Object object) throws Exception {
        return getObjectMapper().writeValueAsString(object);
    }
}
