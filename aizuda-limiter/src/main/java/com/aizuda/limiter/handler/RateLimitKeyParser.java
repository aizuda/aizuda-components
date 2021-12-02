/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.handler;

import com.aizuda.limiter.strategy.IRateLimitStrategy;
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
import java.util.function.Supplier;

/**
 * 速率限制唯一标示 key 解析器
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-16
 */
public class RateLimitKeyParser {
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    private final ExpressionParser parser = new SpelExpressionParser();
    private final List<IRateLimitStrategy> rateLimitStrategyList;

    public RateLimitKeyParser(List<IRateLimitStrategy> rateLimitStrategyList) {
        this.rateLimitStrategyList = rateLimitStrategyList;
    }

    /**
     * 构建唯一标示 KEY
     */
    public String buildKey(Method method, Supplier<Object[]> args, String classMethodName, String spELKey, String[] strategy) {
        StringBuffer key = new StringBuffer();
        key.append(classMethodName).append(":");

        // SpEL Key 解析
        if (!"".equals(spELKey)) {
            key.append(this.parser(spELKey, method, args.get()));
        }

        // 组装自定义策略
        if (strategy.length > 0) {
            for (String str : strategy) {
                rateLimitStrategyList.stream()
                        .filter(t -> Objects.equals(t.getType(), str))
                        .findFirst().ifPresent(rateLimitStrategy -> key.append(rateLimitStrategy.getKey()));
            }
        }
        return key.toString();
    }

    public String parser(String key, Method method, Object[] args) {
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, args, nameDiscoverer);
        Object objKey = parser.parseExpression(key).getValue(context);
        return ObjectUtils.nullSafeToString(objKey);
    }
}
