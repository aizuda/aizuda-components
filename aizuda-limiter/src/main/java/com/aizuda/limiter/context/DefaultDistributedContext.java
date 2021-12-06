/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.limiter.context;

import com.aizuda.common.toolkit.CollectionUtils;
import com.aizuda.limiter.distributedlock.IDistributedLockTemplate;
import com.aizuda.limiter.extend.IAcquireLockTimeoutHandler;
import com.aizuda.limiter.extend.IDistributedLockListener;
import com.aizuda.limiter.metadata.MethodMetadata;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分布式锁容器
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author zhongjiahua
 * @since 2021-12-05
 */
@AllArgsConstructor
public class DefaultDistributedContext implements DistributedContext {
    private IDistributedLockTemplate distributedLockTemplate;
    private List<IAcquireLockTimeoutHandler> acquireLockTimeoutHandlers;
    private List<IDistributedLockListener> distributedLimitListeners;


    @Override
    public IDistributedLockTemplate getDistributedLockTemplate() {
        return distributedLockTemplate;
    }

    @Override
    public List<IAcquireLockTimeoutHandler> getAcquireLockTimeoutHandlers() {
        List<IAcquireLockTimeoutHandler> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(acquireLockTimeoutHandlers)) {
            result.addAll(acquireLockTimeoutHandlers);
        }
        return result;
    }

    @Override
    public IAcquireLockTimeoutHandler getAcquireLockTimeoutHandler(MethodMetadata methodMetadata) {
        Assert.notNull(methodMetadata,"'methodMetadata' cannot be null");
        if (CollectionUtils.isNotEmpty(acquireLockTimeoutHandlers)) {
            return acquireLockTimeoutHandlers.stream()
                    .filter(acquireLockTimeoutHandler -> acquireLockTimeoutHandler.supports(methodMetadata))
                    .findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public List<IDistributedLockListener> getDistributedLimitListeners() {
        List<IDistributedLockListener> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(distributedLimitListeners)) {
            result.addAll(distributedLimitListeners);
        }
        return result;
    }

    @Override
    public List<IDistributedLockListener> getDistributedLimitListeners(MethodMetadata methodMetadata) {
        Assert.notNull(methodMetadata,"'methodMetadata' cannot be null");
        List<IDistributedLockListener> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(distributedLimitListeners)) {
            distributedLimitListeners.forEach(distributedLimitListener->{
                if (distributedLimitListener.supports(methodMetadata)) {
                    result.add(distributedLimitListener);
                }
            });
        }
        return result;
    }


    @PostConstruct
    public void init() {
        if (CollectionUtils.isNotEmpty(acquireLockTimeoutHandlers)) {
            this.acquireLockTimeoutHandlers = Collections.unmodifiableList(acquireLockTimeoutHandlers);
        }
        if (CollectionUtils.isNotEmpty(distributedLimitListeners)) {
            this.distributedLimitListeners = Collections.unmodifiableList(distributedLimitListeners);
        }
    }
}
