/*
 * 爱组搭 http://aizuda.com 低代码组件化开发平台
 * ------------------------------------------
 * 受知识产权保护，请勿删除版权申明
 */
package com.aizuda.monitor;

import lombok.AllArgsConstructor;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.software.os.windows.WindowsOperatingSystem;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 基于 OShi 服务器信息收集监控
 * <p>
 * 尊重知识产权，CV 请保留版权，爱组搭 http://aizuda.com 出品
 *
 * @author 青苗
 * @since 2022-03-02
 */
@AllArgsConstructor
public class OshiMonitor {
    private SystemInfo systemInfo;

    /**
     * 获取系统信息
     *
     * @return {@link SystemInfo}
     */
    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    /**
     * 获取硬件抽象层信息
     *
     * @return {@link HardwareAbstractionLayer}
     */
    public HardwareAbstractionLayer getHardwareAbstractionLayer() {
        return getSystemInfo().getHardware();
    }

    /**
     * 操作系统
     *
     * @return {@link OperatingSystem}
     */
    public OperatingSystem getOperatingSystem() {
        return getSystemInfo().getOperatingSystem();
    }

    /**
     * 中央处理器
     *
     * @return {@link CentralProcessor}
     */
    public CentralProcessor getCentralProcessor() {
        return getHardwareAbstractionLayer().getProcessor();
    }

    /**
     * 获取操作系统信息
     * <code>
     * System.out.println("manufacturer: " + computerSystem.getManufacturer());
     * System.out.println("model: " + computerSystem.getModel());
     * System.out.println("serialnumber: " + computerSystem.getSerialNumber());
     * final Firmware firmware = computerSystem.getFirmware();
     * System.out.println("firmware:");
     * System.out.println("  manufacturer: " + firmware.getManufacturer());
     * System.out.println("  name: " + firmware.getName());
     * System.out.println("  description: " + firmware.getDescription());
     * System.out.println("  version: " + firmware.getVersion());
     * final Baseboard baseboard = computerSystem.getBaseboard();
     * System.out.println("baseboard:");
     * System.out.println("  manufacturer: " + baseboard.getManufacturer());
     * System.out.println("  model: " + baseboard.getModel());
     * System.out.println("  version: " + baseboard.getVersion());
     * System.out.println("  serialnumber: " + baseboard.getSerialNumber());
     * </code>
     *
     * @return {@link ComputerSystem}
     */
    public ComputerSystem getComputerSystem() {
        return getHardwareAbstractionLayer().getComputerSystem();
    }

    public WindowsOperatingSystem getWindowsOperatingSystem() {
        return new WindowsOperatingSystem();
    }

    public InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    public Properties getSystemProperties() {
        return System.getProperties();
    }

    /**
     * 获取系统信息
     *
     * @return {@link SysInfo}
     */
    public SysInfo getSysInfo() {
        Properties props = getSystemProperties();
        SysInfo sysInfo = new SysInfo();
        InetAddress inetAddress = null;
        try {
            inetAddress = getInetAddress();
            sysInfo.setName(inetAddress.getHostName());
            sysInfo.setIp(inetAddress.getHostAddress());
        } catch (UnknownHostException e) {
            sysInfo.setName("unknown");
            sysInfo.setIp("unknown");
        }
        sysInfo.setOsName(props.getProperty("os.name"));
        sysInfo.setOsArch(props.getProperty("os.arch"));
        sysInfo.setUserDir(props.getProperty("user.dir"));
        return sysInfo;
    }

    /**
     * 获取 cpu 信息
     *
     * @return {@link CpuInfo}
     */
    public CpuInfo getCpuInfo() {
        CentralProcessor centralProcessor = getCentralProcessor();
        long[] prevTicks = centralProcessor.getSystemCpuLoadTicks();
        Util.sleep(600);
        long[] ticks = centralProcessor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + sys + idle + ioWait + irq + softirq + steal;
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.setPhysicalProcessorCount(centralProcessor.getPhysicalProcessorCount());
        cpuInfo.setLogicalProcessorCount(centralProcessor.getLogicalProcessorCount());
        cpuInfo.setSystemPercent(formatDouble(sys * 1.0 / totalCpu));
        cpuInfo.setUserPercent(formatDouble(user * 1.0 / totalCpu));
        cpuInfo.setWaitPercent(formatDouble(ioWait * 1.0 / totalCpu));
        cpuInfo.setUsePercent(formatDouble(1.0 - (idle * 1.0 / totalCpu)));
        return cpuInfo;
    }


    /**
     * 获取内存使用信息
     *
     * @return {@link MemoryInfo}
     */
    public MemoryInfo getMemoryInfo() {
        GlobalMemory globalMemory = getHardwareAbstractionLayer().getMemory();
        long totalByte = globalMemory.getTotal();
        long availableByte = globalMemory.getAvailable();
        MemoryInfo memoryInfo = new MemoryInfo();
        memoryInfo.setTotal(formatByte(totalByte));
        memoryInfo.setUsed(formatByte(totalByte - availableByte));
        memoryInfo.setFree(formatByte(availableByte));
        memoryInfo.setUsePercent(formatDouble((totalByte - availableByte) * 1.0 / totalByte));
        return memoryInfo;
    }

    /**
     * 获取 JVM 信息
     *
     * @return {@link JvmInfo}
     */
    public JvmInfo getJvmInfo() {
        Properties props = getSystemProperties();
        Runtime runtime = Runtime.getRuntime();
        long jvmTotalMemoryByte = runtime.totalMemory();
        long freeMemoryByte = runtime.freeMemory();
        JvmInfo jvmInfo = new JvmInfo();
        jvmInfo.setJdkVersion(props.getProperty("java.version"));
        jvmInfo.setJdkHome(props.getProperty("java.home"));
        jvmInfo.setJvmTotalMemory(formatByte(jvmTotalMemoryByte));
        jvmInfo.setMaxMemory(formatByte(runtime.maxMemory()));
        jvmInfo.setUsedMemory(formatByte(jvmTotalMemoryByte - freeMemoryByte));
        jvmInfo.setFreeMemory(formatByte(freeMemoryByte));
        jvmInfo.setUsePercent(formatDouble((jvmTotalMemoryByte - freeMemoryByte) * 1.0 / jvmTotalMemoryByte));
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        jvmInfo.setJdkName(runtimeMXBean.getVmName());
        jvmInfo.setStartTime(runtimeMXBean.getStartTime());
        jvmInfo.setUptime(runtimeMXBean.getUptime());
        return jvmInfo;
    }

    /**
     * 获取磁盘使用信息
     */
    public List<DeskInfo> getDeskInfos() {
        OperatingSystem operatingSystem = getOperatingSystem();
        FileSystem fileSystem = operatingSystem.getFileSystem();
        List<DeskInfo> deskInfos = new ArrayList<>();
        Iterable<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            DeskInfo deskInfo = new DeskInfo();
            deskInfo.setName(fs.getName());
            deskInfo.setVolume(fs.getVolume());
            deskInfo.setLabel(fs.getLabel());
            deskInfo.setLogicalVolume(fs.getLogicalVolume());
            deskInfo.setMount(fs.getMount());
            deskInfo.setDescription(fs.getDescription());
            deskInfo.setOptions(fs.getOptions());
            deskInfo.setType(fs.getType());
            deskInfo.setUUID(fs.getUUID());
            long usable = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            deskInfo.setSize(formatByte(total));
            deskInfo.setAvail(formatByte(usable));
            deskInfo.setUsed(formatByte(total - usable));
            double usedSize = (total - usable);
            double usePercent = 0;
            if (total > 0) {
                usePercent = formatDouble(usedSize / total * 100);
            }
            deskInfo.setUsePercent(usePercent);
            deskInfos.add(deskInfo);
        }
        return deskInfos;
    }

    /**
     * 获取网络带宽信息
     *
     * @return {@link NetIoInfo}
     * @throws Exception
     */
    public NetIoInfo getNetIoInfo() {
        long rxBytesBegin = 0;
        long txBytesBegin = 0;
        long rxPacketsBegin = 0;
        long txPacketsBegin = 0;
        long rxBytesEnd = 0;
        long txBytesEnd = 0;
        long rxPacketsEnd = 0;
        long txPacketsEnd = 0;
        HardwareAbstractionLayer hal = getHardwareAbstractionLayer();
        List<NetworkIF> listBegin = hal.getNetworkIFs();
        for (NetworkIF net : listBegin) {
            rxBytesBegin += net.getBytesRecv();
            txBytesBegin += net.getBytesSent();
            rxPacketsBegin += net.getPacketsRecv();
            txPacketsBegin += net.getPacketsSent();
        }

        //暂停3秒
        Util.sleep(3000);

        List<NetworkIF> listEnd = hal.getNetworkIFs();
        for (NetworkIF net : listEnd) {
            rxBytesEnd += net.getBytesRecv();
            txBytesEnd += net.getBytesSent();
            rxPacketsEnd += net.getPacketsRecv();
            txPacketsEnd += net.getPacketsSent();
        }

        long rxBytesAvg = (rxBytesEnd - rxBytesBegin) / 3 / 1024;
        long txBytesAvg = (txBytesEnd - txBytesBegin) / 3 / 1024;
        long rxPacketsAvg = (rxPacketsEnd - rxPacketsBegin) / 3 / 1024;
        long txPacketsAvg = (txPacketsEnd - txPacketsBegin) / 3 / 1024;
        NetIoInfo netIoInfo = new NetIoInfo();
        netIoInfo.setRxbyt(rxBytesAvg + "");
        netIoInfo.setTxbyt(txBytesAvg + "");
        netIoInfo.setRxpck(rxPacketsAvg + "");
        netIoInfo.setTxpck(txPacketsAvg + "");
        return netIoInfo;
    }

    public String formatByte(long byteNumber) {
        //换算单位
        double FORMAT = 1024.0;
        double kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) {
            return decimalFormat("#.##KB", kbNumber);
        }
        double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) {
            return decimalFormat("#.##MB", mbNumber);
        }
        double gbNumber = mbNumber / FORMAT;
        if (gbNumber < FORMAT) {
            return decimalFormat("#.##GB", gbNumber);
        }
        return decimalFormat("#.##TB", gbNumber / FORMAT);
    }

    private String decimalFormat(String pattern, double number) {
        return new DecimalFormat(pattern).format(number);
    }

    private double formatDouble(double str) {
        return new BigDecimal(str).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
