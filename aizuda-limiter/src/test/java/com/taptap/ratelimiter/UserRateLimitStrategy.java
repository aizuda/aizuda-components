package com.taptap.ratelimiter;

import com.aizuda.limiter.strategy.IRateLimitStrategy;
import org.springframework.stereotype.Component;

@Component
public class UserRateLimitStrategy implements IRateLimitStrategy {

    @Override
    public String getType() {
        // 请保证唯一性
        return "user";
    }

    @Override
    public String getKey() {
        // 返回当前登录用户 admin
        return "admin";
    }
}
