/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.monitor;

import lombok.Getter;
import lombok.Setter;

/**
 * 磁盘信息
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-03-02
 */
@Getter
@Setter
public class DeskInfo {
    /**
     * 名称
     */
    private String name;

    /**
     * 文件系统的卷名
     */
    private String volume;

    /**
     * 标签
     */
    private String label;

    /**
     * 文件系统的逻辑卷名
     */
    private String logicalVolume;

    /**
     * 文件系统的挂载点
     */
    private String mount;

    /**
     * 文件系统的描述
     */
    private String description;

    /**
     * 文件系统的选项
     */
    private String options;

    /**
     * 文件系统的类型（FAT、NTFS、etx2、ext4等）
     */
    private String type;

    /**
     * UUID/GUID
     */
    private String UUID;

    /**
     * 分区大小
     */
    private String size;

    /**
     * 已使用
     */
    private String used;

    /**
     * 可用
     */
    private String avail;

    /**
     * 已使用百分比
     */
    private double usePercent;

}
