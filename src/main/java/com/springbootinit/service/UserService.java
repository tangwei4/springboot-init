package com.springbootinit.service;

import com.springbootinit.entity.User;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {

    // ==================== 新增 ====================

    /**
     * 新增用户
     */
    int addUser(User user);

    /**
     * 批量新增用户
     */
    int batchAddUsers(List<User> userList);

    // ==================== 删除 ====================

    /**
     * 根据ID删除用户
     */
    int deleteUserById(Long id);

    /**
     * 根据学号删除用户
     */
    int deleteUserByStudentNo(String studentNo);

    /**
     * 批量删除用户
     */
    int deleteUsersByIds(List<Long> idList);

    // ==================== 更新 ====================

    /**
     * 更新用户（只更新非空字段）
     */
    int updateUser(User user);

    // ==================== 查询 ====================

    /**
     * 根据ID查询用户
     */
    User getUserById(Long id);

    /**
     * 根据学号查询用户
     */
    User getUserByStudentNo(String studentNo);

    /**
     * 查询所有用户
     */
    List<User> getAllUsers();

    /**
     * 根据班级查询用户列表
     */
    List<User> getUsersByClass(String clazz);

    /**
     * 根据姓名模糊查询
     */
    List<User> getUsersByNameLike(String name);

    /**
     * 根据性别查询
     */
    List<User> getUsersBySex(Integer sex);

    /**
     * 根据年龄范围查询
     */
    List<User> getUsersByAgeRange(Integer minAge, Integer maxAge);

    /**
     * 条件查询
     */
    List<User> getUsersByCondition(User user);

    /**
     * 分页查询
     */
    List<User> getUsersPage(int pageNum, int pageSize);

    // ==================== 统计 ====================

    /**
     * 统计总数
     */
    long countAll();

    /**
     * 根据条件统计总数
     */
    long countByCondition(User user);
}