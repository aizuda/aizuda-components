/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.handler;

import com.aizuda.common.toolkit.StringUtils;
import com.aizuda.limiter.metadata.MethodMetadata;
import com.aizuda.limiter.strategy.DefaultKeyGenerateStrategy;
import com.aizuda.limiter.strategy.IKeyGenerateStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 速率限制唯一标示 key 解析器
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2021-11-16
 */
@Slf4j
public class RateLimitKeyParser {
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    private final ExpressionParser parser = new SpelExpressionParser();
    private final List<IKeyGenerateStrategy> keyGenerateStrategyList;
    private final IKeyGenerateStrategy defaultKeyGenerateStrategy;

    public RateLimitKeyParser(List<IKeyGenerateStrategy> keyGenerateStrategyList) {
        this.keyGenerateStrategyList = keyGenerateStrategyList;
        // 默认key生成策略
        defaultKeyGenerateStrategy = new DefaultKeyGenerateStrategy();
    }

    /**
     * 构建唯一标示 KEY
     *
     * @param useDefaultStrategy 是否使用默认的key策略
     */
    public String buildKey(MethodMetadata methodMetadata, String spELKey, String[] strategy, boolean useDefaultStrategy) {
        StringBuffer key = new StringBuffer();

        // SpEL Key 解析
        String parseKey = Optional.ofNullable(spELKey)
                .filter(StringUtils::hasLength)
                .map(str -> {
                    Method method = methodMetadata.getMethod();
                    Object[] args = methodMetadata.getArgs();
                    return this.parser(spELKey, method, args);
                }).orElse("");

        if (useDefaultStrategy) {
            key.append(defaultKeyGenerateStrategy.getKey(methodMetadata, parseKey));
        }
        // 组装自定义策略
        if (strategy.length > 0) {
            for (String str : strategy) {
                keyGenerateStrategyList.stream()
                        .filter(t -> Objects.equals(t.getType(), str))
                        .findFirst()
                        .ifPresent(rateLimitStrategy -> key.append(rateLimitStrategy.getKey(methodMetadata, parseKey)));
            }
        }
        if (key.length() == 0) {
            log.warn("The generated key is empty, then will use the default strategy");
            key.append(defaultKeyGenerateStrategy.getKey(methodMetadata, parseKey));
        }

        return key.toString();
    }


    public String parser(String key, Method method, Object[] args) {
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, args, nameDiscoverer);
        Object objKey = parser.parseExpression(key).getValue(context);
        return ObjectUtils.nullSafeToString(objKey);
    }
}
