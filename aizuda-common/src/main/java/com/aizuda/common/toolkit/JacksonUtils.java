/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.common.toolkit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * JSON 字符与对像转换
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @author L.cm
 * @since 2021-11-09
 */
@Slf4j
public class JacksonUtils {

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
     * 把json字符串转换为JavaBean
     */
    public static <T> T parse(String json, Class<T> tClass) {
        try {
            return parseThrows(json, tClass);
        } catch (Exception e) {
            log.error("JSONString parse error.", e);
        }
        return null;
    }

    /**
     * 把json字符串转换为JavaBean,支持泛型
     */
    public static <T> T parse(String json, TypeReference<T> valueTypeRef) {
        try {
            return parseThrows(json, valueTypeRef);
        } catch (Exception e) {
            log.error("JSONString parse error.", e);
        }
        return null;
    }

    /**
     * 把JavaBean转换为json字符串，抛出异常
     */
    public static String toJsonThrows(Object object) throws Exception {
        return getObjectMapper().writeValueAsString(object);
    }

    /**
     * 把json字符串转换为JavaBean，抛出异常
     */
    public static <T> T parseThrows(String json, Class<T> tClass) throws Exception {
        return getObjectMapper().readValue(json, tClass);
    }

    /**
     * 把json字符串转换为JavaBean，支持泛型，抛出异常
     */
    public static <T> T parseThrows(String json, TypeReference<T> valueTypeRef) throws JsonProcessingException {
        return getObjectMapper().readValue(json, valueTypeRef);
    }


    /**
     * 将对象序列化成json字符串
     *
     * @param object javaBean
     * @return jsonString json字符串
     */
    @Nullable
    public static String toJson(@Nullable Object object) {
        if (object == null) {
            return null;
        }
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将对象序列化成 json byte 数组
     *
     * @param object javaBean
     * @return jsonString json字符串
     */
    public static byte[] toJsonAsBytes(@Nullable Object object) {
        if (object == null) {
            return new byte[0];
        }
        try {
            return getObjectMapper().writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonString jsonString
     * @return jsonString json字符串
     */
    public static JsonNode readTree(String jsonString) {
        Objects.requireNonNull(jsonString, "jsonString is null");
        try {
            return getObjectMapper().readTree(jsonString);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param in InputStream
     * @return jsonString json字符串
     */
    public static JsonNode readTree(InputStream in) {
        Objects.requireNonNull(in, "InputStream in is null");
        try {
            return getObjectMapper().readTree(in);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param content content
     * @return jsonString json字符串
     */
    public static JsonNode readTree(byte[] content) {
        Objects.requireNonNull(content, "byte[] content is null");
        try {
            return getObjectMapper().readTree(content);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json字符串转成 JsonNode
     *
     * @param jsonParser JsonParser
     * @return jsonString json字符串
     */
    public static JsonNode readTree(JsonParser jsonParser) {
        Objects.requireNonNull(jsonParser, "jsonParser is null");
        try {
            return getObjectMapper().readTree(jsonParser);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json byte 数组反序列化成对象
     *
     * @param content   json bytes
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable byte[] content, Class<T> valueType) {
        if (ObjectUtils.isEmpty(content)) {
            return null;
        }
        try {
            return getObjectMapper().readValue(content, valueType);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param jsonString jsonString
     * @param valueType  class
     * @param <T>        T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable String jsonString, Class<T> valueType) {
        if (!StringUtils.hasLength(jsonString)) {
            return null;
        }
        try {
            return getObjectMapper().readValue(jsonString, valueType);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param in        InputStream
     * @param valueType class
     * @param <T>       T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable InputStream in, Class<T> valueType) {
        if (in == null) {
            return null;
        }
        try {
            return getObjectMapper().readValue(in, valueType);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param content       bytes
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable byte[] content, TypeReference<T> typeReference) {
        if (ObjectUtils.isEmpty(content)) {
            return null;
        }
        try {
            return getObjectMapper().readValue(content, typeReference);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param jsonString    jsonString
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable String jsonString, TypeReference<T> typeReference) {
        if (!StringUtils.hasLength(jsonString)) {
            return null;
        }
        try {
            return getObjectMapper().readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param in            InputStream
     * @param typeReference 泛型类型
     * @param <T>           T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable InputStream in, TypeReference<T> typeReference) {
        if (in == null) {
            return null;
        }
        try {
            return getObjectMapper().readValue(in, typeReference);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param content  bytes
     * @param javaType JavaType
     * @param <T>      T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable byte[] content, JavaType javaType) {
        if (ObjectUtils.isEmpty(content)) {
            return null;
        }
        try {
            return getObjectMapper().readValue(content, javaType);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param jsonString jsonString
     * @param javaType   JavaType
     * @param <T>        T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable String jsonString, JavaType javaType) {
        if (!StringUtils.hasLength(jsonString)) {
            return null;
        }
        try {
            return getObjectMapper().readValue(jsonString, javaType);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     *
     * @param in       InputStream
     * @param javaType JavaType
     * @param <T>      T 泛型标记
     * @return Bean
     */
    @Nullable
    public static <T> T readValue(@Nullable InputStream in, JavaType javaType) {
        if (in == null) {
            return null;
        }
        try {
            return getObjectMapper().readValue(in, javaType);
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 封装 map type，keyClass String
     *
     * @param valueClass value 类型
     * @return MapType
     */
    public static MapType getMapType(Class<?> valueClass) {
        return getMapType(String.class, valueClass);
    }

    /**
     * 封装 map type
     *
     * @param keyClass   key 类型
     * @param valueClass value 类型
     * @return MapType
     */
    public static MapType getMapType(Class<?> keyClass, Class<?> valueClass) {
        return getObjectMapper().getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
    }

    /**
     * 封装 map type
     *
     * @param elementClass 集合值类型
     * @return CollectionLikeType
     */
    public static CollectionLikeType getListType(Class<?> elementClass) {
        return getObjectMapper().getTypeFactory().constructCollectionLikeType(List.class, elementClass);
    }

    /**
     * 封装参数化类型
     *
     * <p>
     * 例如： Map.class, String.class, String.class 对应 Map[String, String]
     * </p>
     *
     * @param parametrized     泛型参数化
     * @param parameterClasses 泛型参数类型
     * @return JavaType
     */
    public static JavaType getParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return getObjectMapper().getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    /**
     * 读取集合
     *
     * @param content      bytes
     * @param elementClass elementClass
     * @param <T>          泛型
     * @return 集合
     */
    public static <T> List<T> readList(@Nullable byte[] content, Class<T> elementClass) {
        if (ObjectUtils.isEmpty(content)) {
            return Collections.emptyList();
        }
        try {
            return getObjectMapper().readValue(content, getListType(elementClass));
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 读取集合
     *
     * @param content      InputStream
     * @param elementClass elementClass
     * @param <T>          泛型
     * @return 集合
     */
    public static <T> List<T> readList(@Nullable InputStream content, Class<T> elementClass) {
        if (content == null) {
            return Collections.emptyList();
        }
        try {
            return getObjectMapper().readValue(content, getListType(elementClass));
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 读取集合
     *
     * @param content      bytes
     * @param elementClass elementClass
     * @param <T>          泛型
     * @return 集合
     */
    public static <T> List<T> readList(@Nullable String content, Class<T> elementClass) {
        if (!StringUtils.hasLength(content)) {
            return Collections.emptyList();
        }
        try {
            return getObjectMapper().readValue(content, getListType(elementClass));
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 读取集合
     *
     * @param content bytes
     * @return 集合
     */
    public static Map<String, Object> readMap(@Nullable byte[] content) {
        return readMap(content, Object.class);
    }

    /**
     * 读取集合
     *
     * @param content InputStream
     * @return 集合
     */
    public static Map<String, Object> readMap(@Nullable InputStream content) {
        return readMap(content, Object.class);
    }

    /**
     * 读取集合
     *
     * @param content bytes
     * @return 集合
     */
    public static Map<String, Object> readMap(@Nullable String content) {
        return readMap(content, Object.class);
    }

    /**
     * 读取集合
     *
     * @param content    bytes
     * @param valueClass 值类型
     * @param <V>        泛型
     * @return 集合
     */
    public static <V> Map<String, V> readMap(@Nullable byte[] content, Class<?> valueClass) {
        return readMap(content, String.class, valueClass);
    }

    /**
     * 读取集合
     *
     * @param content    InputStream
     * @param valueClass 值类型
     * @param <V>        泛型
     * @return 集合
     */
    public static <V> Map<String, V> readMap(@Nullable InputStream content, Class<?> valueClass) {
        return readMap(content, String.class, valueClass);
    }

    /**
     * 读取集合
     *
     * @param content    bytes
     * @param valueClass 值类型
     * @param <V>        泛型
     * @return 集合
     */
    public static <V> Map<String, V> readMap(@Nullable String content, Class<?> valueClass) {
        return readMap(content, String.class, valueClass);
    }

    /**
     * 读取集合
     *
     * @param content    bytes
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @param <K>        泛型
     * @param <V>        泛型
     * @return 集合
     */
    public static <K, V> Map<K, V> readMap(@Nullable byte[] content, Class<?> keyClass, Class<?> valueClass) {
        if (ObjectUtils.isEmpty(content)) {
            return Collections.emptyMap();
        }
        try {
            return getObjectMapper().readValue(content, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 读取集合
     *
     * @param content    InputStream
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @param <K>        泛型
     * @param <V>        泛型
     * @return 集合
     */
    public static <K, V> Map<K, V> readMap(@Nullable InputStream content, Class<?> keyClass, Class<?> valueClass) {
        if (content == null) {
            return Collections.emptyMap();
        }
        try {
            return getObjectMapper().readValue(content, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 读取集合
     *
     * @param content    bytes
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @param <K>        泛型
     * @param <V>        泛型
     * @return 集合
     */
    public static <K, V> Map<K, V> readMap(@Nullable String content, Class<?> keyClass, Class<?> valueClass) {
        if (!StringUtils.hasLength(content)) {
            return Collections.emptyMap();
        }
        try {
            return getObjectMapper().readValue(content, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * jackson 的类型转换
     *
     * @param fromValue   来源对象
     * @param toValueType 转换的类型
     * @param <T>         泛型标记
     * @return 转换结果
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return getObjectMapper().convertValue(fromValue, toValueType);
    }

    /**
     * jackson 的类型转换
     *
     * @param fromValue   来源对象
     * @param toValueType 转换的类型
     * @param <T>         泛型标记
     * @return 转换结果
     */
    public static <T> T convertValue(Object fromValue, JavaType toValueType) {
        return getObjectMapper().convertValue(fromValue, toValueType);
    }

    /**
     * jackson 的类型转换
     *
     * @param fromValue      来源对象
     * @param toValueTypeRef 泛型类型
     * @param <T>            泛型标记
     * @return 转换结果
     */
    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        return getObjectMapper().convertValue(fromValue, toValueTypeRef);
    }

    /**
     * tree 转对象
     *
     * @param treeNode  TreeNode
     * @param valueType valueType
     * @param <T>       泛型标记
     * @return 转换结果
     */
    public static <T> T treeToValue(TreeNode treeNode, Class<T> valueType) {
        try {
            return getObjectMapper().treeToValue(treeNode, valueType);
        } catch (JsonProcessingException e) {
            throw ThrowableUtils.unchecked(e);
        }
    }

    /**
     * 对象转 tree
     *
     * @param fromValue fromValue
     * @param <T>       泛型标记
     * @return 转换结果
     */
    public static <T extends JsonNode> T valueToTree(@Nullable Object fromValue) {
        return getObjectMapper().valueToTree(fromValue);
    }

    /**
     * 创建 ObjectNode
     *
     * @return ObjectNode
     */
    public static ObjectNode createObjectNode() {
        return getObjectMapper().createObjectNode();
    }

    /**
     * 创建 ArrayNode
     *
     * @return ArrayNode
     */
    public static ArrayNode createArrayNode() {
        return getObjectMapper().createArrayNode();
    }

    /**
     * 获取 ObjectMapper 实例
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {
        return JacksonHolder.INSTANCE;
    }

    private static class JacksonHolder {
        private static final ObjectMapper INSTANCE = new JacksonObjectMapper();
    }

    private static class JacksonObjectMapper extends ObjectMapper {
        private static final long serialVersionUID = 4288193147502386170L;

        JacksonObjectMapper() {
            super(jsonFactory());
            super.setLocale(Locale.CHINA);
            super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            super.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            // 忽略 transient 关键词属性
            super.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
            // Long 转为 String 防止 js 丢失精度
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            super.registerModule(simpleModule);
            super.registerModule(new JavaTimeModule());
            super.findAndRegisterModules();
        }

        JacksonObjectMapper(ObjectMapper src) {
            super(src);
        }

        private static JsonFactory jsonFactory() {
            return JsonFactory.builder()
                    // 可解析反斜杠引用的所有字符
                    .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
                    // 允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
                    .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true)
                    .build();
        }

        @Override
        public ObjectMapper copy() {
            return new JacksonObjectMapper(this);
        }
    }

}
