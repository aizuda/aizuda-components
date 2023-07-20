/*
 * 爱组搭，低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明，开发平台不允许做非法网站，后果自负
 */
package com.aizuda.monitor;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统内存信息
 * <p>
 * 尊重知识产权，CV 请保留版权，开发平台不允许做非法网站，后果自负
 *
 * @author 青苗
 * @since 2022-03-02
 */
@Getter
@Setter
public class MemoryInfo {
    /**
     * 总计
     */
    private String total;

    /**
     * 已使用
     */
    private String used;

    /**
     * 未使用
     */
    private String free;

    /**
     * 使用率
     */
    private double usePercent;

}
