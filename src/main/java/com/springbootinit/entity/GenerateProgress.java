package com.springbootinit.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据生成进度
 */
@Data
public class GenerateProgress implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 总数量
     */
    private long totalCount;

    /**
     * 已完成数量
     */
    private long completedCount;

    /**
     * 进度百分比
     */
    private int percentage;

    /**
     * 状态：RUNNING-运行中, COMPLETED-已完成, FAILED-失败
     */
    private String status;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 结束时间
     */
    private long endTime;

    /**
     * 耗时（毫秒）
     */
    private long duration;

    /**
     * 每秒钟插入数量
     */
    private long recordsPerSecond;

    /**
     * 错误信息
     */
    private String errorMessage;

    public GenerateProgress() {
        this.status = "RUNNING";
        this.startTime = System.currentTimeMillis();
    }

    public GenerateProgress(String taskId, long totalCount) {
        this.taskId = taskId;
        this.totalCount = totalCount;
        this.status = "RUNNING";
        this.startTime = System.currentTimeMillis();
        this.completedCount = 0;
        this.percentage = 0;
    }

    /**
     * 更新进度
     */
    public synchronized void updateProgress(long completedCount) {
        this.completedCount = completedCount;
        this.percentage = (int) ((double) completedCount / totalCount * 100);
        long currentTime = System.currentTimeMillis();
        this.duration = currentTime - startTime;
        if (duration > 0) {
            this.recordsPerSecond = (completedCount * 1000) / duration;
        }
    }

    /**
     * 标记完成
     */
    public synchronized void complete() {
        this.status = "COMPLETED";
        this.endTime = System.currentTimeMillis();
        this.duration = endTime - startTime;
        this.percentage = 100;
        this.completedCount = totalCount;
        if (duration > 0) {
            this.recordsPerSecond = (totalCount * 1000) / duration;
        }
    }

    /**
     * 标记失败
     */
    public synchronized void fail(String errorMessage) {
        this.status = "FAILED";
        this.errorMessage = errorMessage;
        this.endTime = System.currentTimeMillis();
        this.duration = endTime - startTime;
    }
}
