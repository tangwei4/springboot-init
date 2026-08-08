package com.springbootinit.util;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;

/**
 * 数据源切换工具类
 * 封装 DynamicDataSourceContextHolder 的调用
 */
public class DataSourceContextUtil {

    /**
     * 切换到 source 数据源
     */
    public static void useSource() {
        DynamicDataSourceContextHolder.push("source");
    }

    /**
     * 切换到 target 数据源
     */
    public static void useTarget() {
        DynamicDataSourceContextHolder.push("target");
    }

    /**
     * 切换到指定数据源
     */
    public static void use(String dsName) {
        DynamicDataSourceContextHolder.push(dsName);
    }

    /**
     * 清除当前数据源（恢复默认）
     */
    public static void clear() {
        DynamicDataSourceContextHolder.poll();
    }

    /**
     * 执行带数据源切换的业务逻辑（自动清理）
     */
    public static <T> T executeWithDataSource(String dsName, DataSourceAction<T> action) {
        try {
            use(dsName);
            return action.execute();
        } finally {
            clear();
        }
    }

    /**
     * 函数式接口
     */
    @FunctionalInterface
    public interface DataSourceAction<T> {
        T execute();
    }
}
