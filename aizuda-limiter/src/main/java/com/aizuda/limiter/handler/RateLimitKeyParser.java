/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.handler;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

/**
 * 速率限制唯一标示 key 解析器
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author hubin
 * @since 2021-11-16
 */
public class RateLimitKeyParser implements BeanFactoryAware {
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    private final StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
    private final ExpressionParser parser = new SpelExpressionParser();
    private BeanFactory beanFactory;

    public String parser(String key, Method method, Object[] args) {
        EvaluationContext context = new MethodBasedEvaluationContext(null, method, args, nameDiscoverer);
        Object objKey = parser.parseExpression(key).getValue(context);
        return ObjectUtils.nullSafeToString(objKey);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
    }

    private String resolve(String value) {
        return ((ConfigurableBeanFactory) this.beanFactory).resolveEmbeddedValue(value);
    }
}
