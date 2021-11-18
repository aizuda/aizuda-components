package com.taptap.ratelimiter;

import com.aizuda.limiter.annotation.RateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    /**
     *
     * 测试多次访问观察浏览器及控制台输出日志
     *
     * http://localhost:8080/test?name=abc
     *
     */
    @GetMapping("/test")
    @RateLimit(
            // 唯一标示，支持SpEL表达式（可无），#name 为获取当前访问参数 name 内容
            key = "#name",
            // 限定阈值，时间间隔 interval 范围内超过该数量会触发锁
            count = 2,
            // 限制间隔时长（可无，默认 3 分钟）例如 5s 五秒，6m 六分钟，7h 七小时，8d 八天
            interval = "100s",
            // 策略（可无） ip 为获取当前访问IP地址（内置策略），自定义策略 user 为获取当前用户
            strategy = { "ip", "user" },
            // 提示消息（可无）
            message = "请勿频繁操作"
    )
    public String test(String name) {
        return "test" + name;
    }
}
