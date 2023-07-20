/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.limiter.context;

import com.aizuda.limiter.distributedlock.IDistributedLockTemplate;
import com.aizuda.limiter.extend.IAcquireLockTimeoutHandler;
import com.aizuda.limiter.extend.IDistributedLockListener;
import com.aizuda.limiter.metadata.DistributedLockMethodMetaData;
import com.aizuda.limiter.metadata.MethodMetadata;

import java.util.List;

/**
 * 分布式锁容器
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author zhongjiahua
 * @since 2021-12-05
 */
public interface DistributedContext {

    /**
     * 获取分布式锁模板，有可能是用户自定义的
     *
     * @return {@link IDistributedLockTemplate}
     */
    IDistributedLockTemplate getDistributedLockTemplate();

    /**
     * 获取分布式锁超时处理策略，默认为空
     *
     * @return {@link IAcquireLockTimeoutHandler}
     */
    List<IAcquireLockTimeoutHandler> getAcquireLockTimeoutHandlers();

    /**
     * 通过{@link MethodMetadata}获取超时处理策略
     *
     * @param methodMetadata {@link DistributedLockMethodMetaData}
     * @return {@link IAcquireLockTimeoutHandler}
     */
    IAcquireLockTimeoutHandler getAcquireLockTimeoutHandler(MethodMetadata methodMetadata);

    /**
     * 获取分布式锁监听器，默认为空
     *
     * @return {@link IDistributedLockListener}
     */
    List<IDistributedLockListener> getDistributedLimitListeners();

    /**
     * 获取分布式锁监听器，默认为空
     *
     * @param methodMetadata {@link DistributedLockMethodMetaData}
     * @return {@link IDistributedLockListener}
     */
    List<IDistributedLockListener> getDistributedLimitListeners(MethodMetadata methodMetadata);

}
