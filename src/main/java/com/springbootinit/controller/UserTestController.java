package com.springbootinit.controller;

import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.PageResult;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.entity.User;
import com.springbootinit.service.UserService;
import com.springbootinit.util.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理测试 Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/test/user")
public class UserTestController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ==================== 基础增删改查 ====================

    @PostMapping("/add")
    public BaseResponse<Long> addUser(@Validated @RequestBody User user) {
        log.info("新增用户: {}", user);
        userService.addUser(user);
        return ResultUtils.success(user.getId());
    }

    @PostMapping("/batch")
    public BaseResponse<String> batchAddUsers(@RequestBody List<User> userList) {
        log.info("批量新增用户, 数量: {}", userList.size());
        int count = userService.batchAddUsers(userList);
        return ResultUtils.success("成功插入 " + count + " 条");
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Integer> deleteUser(@PathVariable @NotNull Long id) {
        log.info("删除用户, ID: {}", id);
        int count = userService.deleteUserById(id);
        return ResultUtils.success(count);
    }

    @DeleteMapping("/byStudentNo")
    public BaseResponse<Integer> deleteUserByStudentNo(@RequestParam String studentNo) {
        log.info("删除用户, 学号: {}", studentNo);
        int count = userService.deleteUserByStudentNo(studentNo);
        return ResultUtils.success(count);
    }

    @DeleteMapping("/batch")
    public BaseResponse<Integer> deleteUsers(@RequestBody List<Long> idList) {
        log.info("批量删除用户, ID列表: {}", idList);
        int count = userService.deleteUsersByIds(idList);
        return ResultUtils.success(count);
    }

    @PutMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user) {
        log.info("更新用户: {}", user);
        int count = userService.updateUser(user);
        return ResultUtils.success(count);
    }

    @GetMapping("/{id}")
    public BaseResponse<User> getUser(@PathVariable Long id) {
        log.info("查询用户, ID: {}", id);
        User user = userService.getUserById(id);
        return ResultUtils.success(user);
    }

    @GetMapping("/byStudentNo")
    public BaseResponse<User> getUserByStudentNo(@RequestParam String studentNo) {
        log.info("查询用户, 学号: {}", studentNo);
        User user = userService.getUserByStudentNo(studentNo);
        return ResultUtils.success(user);
    }

    @GetMapping("/all")
    public BaseResponse<List<User>> getAllUsers() {
        log.info("查询所有用户");
        List<User> users = userService.getAllUsers();
        return ResultUtils.success(users);
    }

    @GetMapping("/byClass")
    public BaseResponse<List<User>> getUsersByClass(@RequestParam String clazz) {
        log.info("查询用户, 班级: {}", clazz);
        List<User> users = userService.getUsersByClass(clazz);
        return ResultUtils.success(users);
    }

    @GetMapping("/byNameLike")
    public BaseResponse<List<User>> getUsersByNameLike(@RequestParam String name) {
        log.info("模糊查询用户, 姓名: {}", name);
        List<User> users = userService.getUsersByNameLike(name);
        return ResultUtils.success(users);
    }

    @GetMapping("/bySex")
    public BaseResponse<List<User>> getUsersBySex(@RequestParam Integer sex) {
        log.info("查询用户, 性别: {}", sex);
        List<User> users = userService.getUsersBySex(sex);
        return ResultUtils.success(users);
    }

    @GetMapping("/byAgeRange")
    public BaseResponse<List<User>> getUsersByAgeRange(@RequestParam(required = false) Integer minAge, @RequestParam(required = false) Integer maxAge) {
        log.info("查询用户, 年龄范围: {} - {}", minAge, maxAge);
        List<User> users = userService.getUsersByAgeRange(minAge, maxAge);
        return ResultUtils.success(users);
    }

    @PostMapping("/condition")
    public BaseResponse<List<User>> getUsersByCondition(@RequestBody User user) {
        log.info("条件查询用户: {}", user);
        List<User> users = userService.getUsersByCondition(user);
        return ResultUtils.success(users);
    }

    @GetMapping("/page")
    public BaseResponse<PageResult<User>> getUsersPage(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询, 页码: {}, 每页大小: {}", pageNum, pageSize);
        List<User> users = userService.getUsersPage(pageNum, pageSize);
        long total = userService.countAll();
        PageResult<User> pageResult = new PageResult<>(pageNum, pageSize, total, users);
        return ResultUtils.success(pageResult);
    }

    @GetMapping("/count")
    public BaseResponse<Long> countAll() {
        long count = userService.countAll();
        return ResultUtils.success(count);
    }

    @PostMapping("/count/condition")
    public BaseResponse<Long> countByCondition(@RequestBody User user) {
        long count = userService.countByCondition(user);
        return ResultUtils.success(count);
    }

    // ==================== Redis 缓存测试 ====================

    @GetMapping("/cache/{id}")
    public BaseResponse<User> getUserWithCache(@PathVariable Long id) {
        String cacheKey = "user:cache:" + id;
        log.info("查询用户, ID: {}, 缓存Key: {}", id, cacheKey);

        // 1. 先从缓存获取
        User user = (User) redisTemplate.opsForValue().get(cacheKey);
        if (user != null) {
            log.info("从缓存获取用户成功: {}", user);
            return ResultUtils.success(user);
        }
        // 2. 缓存未命中，从数据库查询
        user = userService.getUserById(id);
        if (user != null) {
            // 3. 写入缓存，过期时间 10 分钟
            redisTemplate.opsForValue().set(cacheKey, user, 10, TimeUnit.MINUTES);
            log.info("从数据库获取用户并写入缓存: {}", user);
            return ResultUtils.success(user);
        }

        return ResultUtils.error(500, "用户不存在");
    }

    @DeleteMapping("/cache/{id}")
    public BaseResponse<String> clearUserCache(@PathVariable Long id) {
        String cacheKey = "user:cache:" + id;
        Boolean deleted = redisTemplate.delete(cacheKey);
        log.info("清除缓存: {}, 结果: {}", cacheKey, deleted);
        return ResultUtils.success(deleted ? "缓存清除成功" : "缓存不存在或已清除");
    }

    // ==================== 多数据源测试 ====================

    @GetMapping("/source/{id}")
    public BaseResponse<User> getFromSource(@PathVariable Long id) {
        log.info("从 source 数据源查询用户, ID: {}", id);
        // 使用 @DS("source") 注解在 Service 方法上，或者用 DataSourceContext 手动切换
        User user = userService.getUserById(id);
        return ResultUtils.success(user);
    }

    @GetMapping("/target/{id}")
    public BaseResponse<User> getFromTarget(@PathVariable Long id) {
        log.info("从 target 数据源查询用户, ID: {}", id);
        User user = userService.getUserById(id);
        return ResultUtils.success(user);
    }

    // ==================== 简单测试接口 ====================

    @GetMapping("/ping")
    public BaseResponse<String> ping() {
        return ResultUtils.success("pong");
    }

    @GetMapping("/redis/ping")
    public BaseResponse<String> redisPing() {
        try {
            String result = stringRedisTemplate.getConnectionFactory().getConnection().ping();
            return ResultUtils.success("Redis 连接成功: " + result);
        } catch (Exception e) {
            log.error("Redis 连接失败", e);
            return ResultUtils.error(500, "Redis 连接失败: " + e.getMessage());
        }
    }
}