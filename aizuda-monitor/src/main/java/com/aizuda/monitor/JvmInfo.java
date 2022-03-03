/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.monitor;

import lombok.Getter;
import lombok.Setter;

/**
 * JVM 信息
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-03-02
 */
@Getter
@Setter
public class JvmInfo {
    /**
     * jdk版本
     */
    String jdkVersion;

    /**
     * jdk Home
     */
    String jdkHome;

    /**
     * 总内存
     */
    String jvmTotalMemory;

    /**
     * Java虚拟机将尝试使用的最大内存量
     */
    String maxMemory;

    /**
     * 空闲内存
     */
    String freeMemory;

    /**
     * 已使用内存
     */
    String usedMemory;

    /**
     * 内存使用率
     */
    private double usePercent;

}
