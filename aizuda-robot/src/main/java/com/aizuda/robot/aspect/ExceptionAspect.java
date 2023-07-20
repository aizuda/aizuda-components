/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.robot.aspect;

import com.aizuda.robot.exception.ISendException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 异常捕获切面处理类
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 2021-11-21
 */
@Slf4j
@AllArgsConstructor
@Aspect
public class ExceptionAspect {

    private final ISendException sendException;

    /**
     * 切入点配置
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllerPointCut() {
        // 切入控制器内部方法
    }

    /**
     * 切点方法执行异常调用
     *
     * @param joinPoint {@link JoinPoint}
     * @param e         {@link Exception}
     */
    @AfterThrowing(value = "restControllerPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        if (!sendException.send(joinPoint, e)) {
            log.error("exception message sending failed", e);
        }
    }
}
