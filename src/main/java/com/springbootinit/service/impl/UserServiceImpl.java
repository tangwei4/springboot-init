package com.springbootinit.service.impl;

import com.springbootinit.entity.User;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.mapper.UserMapper;
import com.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // ==================== 新增 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addUser(User user) {
        log.info("新增用户: {}", user);
        if (user == null) {
            throw new BusinessException(500, "用户信息不能为空");
        }
        if (user.getStudentNo() == null || user.getStudentNo().isEmpty()) {
            throw new BusinessException(500, "学号不能为空");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new BusinessException(500, "姓名不能为空");
        }
        return userMapper.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchAddUsers(List<User> userList) {
        log.info("批量新增用户, 数量: {}", userList == null ? 0 : userList.size());
        if (userList == null || userList.isEmpty()) {
            throw new BusinessException(500, "用户列表不能为空");
        }
        for (User user : userList) {
            if (user.getStudentNo() == null || user.getStudentNo().isEmpty()) {
                throw new BusinessException(500, "学号不能为空");
            }
            if (user.getName() == null || user.getName().isEmpty()) {
                throw new BusinessException(500, "姓名不能为空");
            }
        }
        return userMapper.batchInsert(userList);
    }

    // ==================== 删除 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserById(Long id) {
        log.info("删除用户, ID: {}", id);
        if (id == null) {
            throw new BusinessException(500, "用户ID不能为空");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(500, "用户不存在");
        }
        return userMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByStudentNo(String studentNo) {
        log.info("删除用户, 学号: {}", studentNo);
        if (studentNo == null || studentNo.isEmpty()) {
            throw new BusinessException(500, "学号不能为空");
        }
        User user = userMapper.selectByStudentNo(studentNo);
        if (user == null) {
            throw new BusinessException(500, "用户不存在");
        }
        return userMapper.deleteByStudentNo(studentNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUsersByIds(List<Long> idList) {
        log.info("批量删除用户, ID列表: {}", idList);
        if (idList == null || idList.isEmpty()) {
            throw new BusinessException(500, "用户ID列表不能为空");
        }
        return userMapper.deleteByIds(idList);
    }

    // ==================== 更新 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(User user) {
        log.info("更新用户: {}", user);
        if (user == null) {
            throw new BusinessException(500, "用户信息不能为空");
        }
        if (user.getId() == null) {
            throw new BusinessException(500, "用户ID不能为空");
        }
        User existUser = userMapper.selectById(user.getId());
        if (existUser == null) {
            throw new BusinessException(500, "用户不存在");
        }
        return userMapper.updateById(user);
    }

    // ==================== 查询 ====================

    @Override
    public User getUserById(Long id) {
        log.info("查询用户, ID: {}", id);
        if (id == null) {
            throw new BusinessException(500, "用户ID不能为空");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(500, "用户不存在");
        }
        return user;
    }

    @Override
    public User getUserByStudentNo(String studentNo) {
        log.info("查询用户, 学号: {}", studentNo);
        if (studentNo == null || studentNo.isEmpty()) {
            throw new BusinessException(500, "学号不能为空");
        }
        User user = userMapper.selectByStudentNo(studentNo);
        if (user == null) {
            throw new BusinessException(500, "用户不存在");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("查询所有用户");
        List<User> users = userMapper.selectAll();
        log.info("查询到 {} 条用户数据", users.size());
        return users;
    }

    @Override
    public List<User> getUsersByClass(String clazz) {
        log.info("查询用户, 班级: {}", clazz);
        if (clazz == null || clazz.isEmpty()) {
            throw new BusinessException(500, "班级不能为空");
        }
        return userMapper.selectByClass(clazz);
    }

    @Override
    public List<User> getUsersByNameLike(String name) {
        log.info("模糊查询用户, 姓名: {}", name);
        if (name == null || name.isEmpty()) {
            throw new BusinessException(500, "姓名不能为空");
        }
        return userMapper.selectByNameLike(name);
    }

    @Override
    public List<User> getUsersBySex(Integer sex) {
        log.info("查询用户, 性别: {}", sex);
        if (sex == null) {
            throw new BusinessException(500, "性别不能为空");
        }
        return userMapper.selectBySex(sex);
    }

    @Override
    public List<User> getUsersByAgeRange(Integer minAge, Integer maxAge) {
        log.info("查询用户, 年龄范围: {} - {}", minAge, maxAge);
        return userMapper.selectByAgeRange(minAge, maxAge);
    }

    @Override
    public List<User> getUsersByCondition(User user) {
        log.info("条件查询用户: {}", user);
        return userMapper.selectByCondition(user);
    }

    @Override
    public List<User> getUsersPage(int pageNum, int pageSize) {
        log.info("分页查询, 页码: {}, 每页大小: {}", pageNum, pageSize);
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }
        int offset = (pageNum - 1) * pageSize;
        return userMapper.selectPage(offset, pageSize);
    }

    // ==================== 统计 ====================

    @Override
    public long countAll() {
        log.info("统计用户总数");
        Long count = userMapper.countAll();
        return count != null ? count : 0L;
    }

    @Override
    public long countByCondition(User user) {
        log.info("条件统计用户: {}", user);
        Long count = userMapper.countByCondition(user);
        return count != null ? count : 0L;
    }
}