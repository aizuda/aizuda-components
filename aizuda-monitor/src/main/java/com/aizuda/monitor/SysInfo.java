/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.monitor;

import lombok.Getter;
import lombok.Setter;

/**
 * 操作系统信息
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-03-02
 */
@Getter
@Setter
public class SysInfo {

    /**
     * 系统名称
     */
    private String name;
    /**
     * 系统架构
     */
    private String arch;

}
