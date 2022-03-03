/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.monitor;

import lombok.Getter;
import lombok.Setter;

/**
 * 网络带宽信息
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-03-02
 */
@Getter
@Setter
public class NetIoInfo {
    /**
     * 每秒钟接收的数据包,rxpck/s
     */
    private String rxpck;

    /**
     * 每秒钟发送的数据包,txpck/s
     */
    private String txpck;

    /**
     * 每秒钟接收的KB数,rxkB/s
     */
    private String rxbyt;

    /**
     * 每秒钟发送的KB数,txkB/s
     */
    private String txbyt;

}
