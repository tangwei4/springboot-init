package com.springbootinit.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户实体类
 * 字段名与数据库字段对应（全大写映射）
 */
@Data
@TableName("user")
public class User {
    /**
     * 主键ID
     */
    @TableId("ID")
    private Long id;

    /**
     * 学号
     */
    @TableField("STUDENT_NO")
    private String studentNo;

    /**
     * 姓名
     */
    @TableField("NAME")
    private String name;

    /**
     * 班级
     */
    @TableField("CLASS")
    private String clazz;

    /**
     * 性别：1-男，2-女
     */
    @TableField("SEX")
    private Integer sex;

    /**
     * 年龄
     */
    @TableField("AGE")
    private Integer age;

    /**
     * 学生信息（大文本字段，对应 CLOB）
     * 使用 @Lob 注解标识为大字段
     */
    @TableField("STUDENT_INFO")
    private String studentInfo;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;
}
