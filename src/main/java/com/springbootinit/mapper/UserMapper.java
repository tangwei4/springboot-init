package com.springbootinit.mapper;

import com.springbootinit.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 新增用户
     */
    int insert(User user);

    /**
     * 批量新增用户
     */
    int batchInsert(@Param("list") List<User> userList);

    /**
     * 根据ID删除用户
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据学号删除用户
     */
    int deleteByStudentNo(@Param("studentNo") String studentNo);

    /**
     * 根据ID更新用户（只更新非空字段）
     */
    int updateById(User user);

    /**
     * 根据ID更新用户（全量更新）
     */
    int updateByIdFull(User user);

    /**
     * 根据ID查询用户
     */
    User selectById(@Param("id") Long id);

    /**
     * 根据学号查询用户
     */
    User selectByStudentNo(@Param("studentNo") String studentNo);

    /**
     * 查询所有用户
     */
    List<User> selectAll();

    /**
     * 根据班级查询用户列表
     */
    List<User> selectByClass(@Param("clazz") String clazz);

    /**
     * 根据姓名模糊查询
     */
    List<User> selectByNameLike(@Param("name") String name);

    /**
     * 根据性别查询
     */
    List<User> selectBySex(@Param("sex") Integer sex);

    /**
     * 根据年龄范围查询
     */
    List<User> selectByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);

    /**
     * 条件查询（动态SQL）
     */
    List<User> selectByCondition(User user);

    /**
     * 分页查询
     */
    List<User> selectPage(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 统计总数
     */
    Long countAll();

    /**
     * 根据条件统计总数
     */
    Long countByCondition(User user);

    /**
     * 批量删除（根据ID列表）
     */
    int deleteByIds(@Param("idList") List<Long> idList);
}