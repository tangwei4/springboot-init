package com.springbootinit.service.impl;

import com.springbootinit.entity.GenerateProgress;
import com.springbootinit.entity.User;
import com.springbootinit.exception.BusinessException;
import com.springbootinit.mapper.UserMapper;
import com.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

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

    // ==================== 新增批量生成方法 =========================================================================================

    @Autowired
    @Qualifier("dataGenerateExecutor")
    private Executor dataGenerateExecutor;

    @Autowired
    @Qualifier("dataInsertExecutor")
    private Executor dataInsertExecutor;
    // 存储所有任务进度
    private final Map<String, GenerateProgress> progressMap = new ConcurrentHashMap<>();

    // 姓名数据源
    private static final String[] SURNAMES = {"张", "王", "李", "刘", "陈", "杨", "赵", "黄", "周", "吴",
            "徐", "孙", "胡", "朱", "高", "林", "何", "郭", "马", "罗", "梁", "宋", "郑", "谢", "韩",
            "唐", "冯", "于", "董", "萧", "程", "曹", "袁", "邓", "许", "傅", "沈", "曾", "彭", "吕",
            "苏", "卢", "蒋", "蔡", "贾", "丁", "魏", "薛", "叶", "阎", "余", "潘", "杜", "戴", "夏",
            "钟", "汪", "田", "任", "姜", "范", "方", "石", "姚", "谭", "廖", "邹", "熊", "金", "陆"};

    private static final String[] GIVEN_NAMES = {"伟", "强", "丽", "敏", "静", "涛", "军", "勇", "杰", "倩",
            "鹏", "辉", "玲", "桂", "英", "华", "慧", "建", "文", "平", "明", "秀", "兰", "玉", "龙",
            "凤", "洁", "梅", "海", "红", "春", "峰", "刚", "毅", "萍", "飞", "丹", "晶", "鑫", "浩"};

    private static final String[] CLASSES = {"一班", "二班", "三班", "四班", "五班", "六班", "七班", "八班"};

    private static final String[] INFOS = {
            "计算机科学与技术专业，成绩优异，多次获得奖学金",
            "软件工程专业，擅长Java开发，有丰富的项目经验",
            "网络工程专业，热爱网络安全，持有CCNA证书",
            "数据科学与大数据技术专业，擅长数据分析",
            "人工智能专业，参与过多个AI项目，有实际落地经验",
            "计算机科学与技术专业，成绩良好，热爱编程",
            "软件工程专业，擅长前端开发，精通Vue和React",
            "网络工程专业，有CCNP证书，擅长网络架构设计",
            "热爱编程，有丰富的项目经验，善于团队协作",
            "学习能力强，善于解决问题，有创新思维"
    };

    // ==================== 批量生成方法 ====================
    @Override
    public CompletableFuture<Integer> generateUsersAsync(int totalCount, int batchSize) {
        // 生成任务ID
        String taskId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        log.info("开始异步生成数据, taskId: {}, totalCount: {}, batchSize: {}", taskId, totalCount, batchSize);

        // 初始化进度
        GenerateProgress progress = new GenerateProgress(taskId, totalCount);
        progressMap.put(taskId, progress);

        // 异步执行
        return CompletableFuture.supplyAsync(() -> {
            try {
                int inserted = doGenerateAndInsert(totalCount, batchSize, progress);
                progress.complete();
                log.info("数据生成完成, taskId: {}, 共插入: {} 条, 耗时: {}ms, 速度: {}条/秒",
                        taskId, inserted, progress.getDuration(), progress.getRecordsPerSecond());
                return inserted;
            } catch (Exception e) {
                log.error("数据生成失败, taskId: {}", taskId, e);
                progress.fail(e.getMessage());
                throw new BusinessException(500, "数据生成失败: " + e.getMessage());
            }
        }, dataGenerateExecutor);
    }

    @Override
    public int generateUsersSync(int totalCount, int batchSize) {
        log.info("开始同步生成数据, totalCount: {}, batchSize: {}", totalCount, batchSize);
        String taskId = "sync-" + System.currentTimeMillis();

        GenerateProgress progress = new GenerateProgress(taskId, totalCount);
        progressMap.put(taskId, progress);

        try {
            int inserted = doGenerateAndInsert(totalCount, batchSize, progress);
            progress.complete();
            log.info("同步生成完成, 共插入: {} 条, 耗时: {}ms", inserted, progress.getDuration());
            return inserted;
        } catch (Exception e) {
            log.error("同步生成失败", e);
            progress.fail(e.getMessage());
            throw new BusinessException(500, "数据生成失败: " + e.getMessage());
        }
    }

    @Override
    public GenerateProgress getGenerateProgress(String taskId) {
        return progressMap.get(taskId);
    }

    @Override
    public Map<String, GenerateProgress> getAllProgress() {
        return new HashMap<>(progressMap);
    }

    // ==================== 核心生成逻辑 ====================

    /**
     * 实际生成和插入数据的核心方法
     */
    private int doGenerateAndInsert(int totalCount, int batchSize, GenerateProgress progress) {
        if (batchSize <= 0) {
            batchSize = 1000;
        }
        if (batchSize > 20000) {
            batchSize = 20000;
        }

        int totalBatches = (int) Math.ceil((double) totalCount / batchSize);
        log.info("总批次: {}, 每批: {} 条", totalBatches, batchSize);

        AtomicLong totalInserted = new AtomicLong(0);

        // 使用 CompletableFuture 并行插入
        List<CompletableFuture<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < totalBatches; i++) {
            int currentBatch = i;
            int startIndex = i * batchSize;
            int endIndex = Math.min(startIndex + batchSize, totalCount);
            int currentBatchSize = endIndex - startIndex;

            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                try {
                    // 生成一批数据
                    List<User> userList = generateUserBatch(startIndex, currentBatchSize);
                    // 插入数据库
                    int inserted = userMapper.batchInsert(userList);
                    // 更新进度
                    long completed = totalInserted.addAndGet(inserted);
                    progress.updateProgress(completed);
                    log.info("批次 {}/{} 完成, 插入: {} 条, 总进度: {}%",
                            currentBatch + 1, totalBatches, inserted, progress.getPercentage());
                    return inserted;
                } catch (Exception e) {
                    log.error("批次 {} 插入失败", currentBatch + 1, e);
                    throw new RuntimeException("批次插入失败", e);
                }
            }, dataInsertExecutor);

            futures.add(future);
        }

        // 等待所有任务完成
        int totalInsertedResult = 0;
        for (CompletableFuture<Integer> future : futures) {
            try {
                totalInsertedResult += future.join();
            } catch (Exception e) {
                log.error("等待插入任务完成时出错", e);
                // 继续执行，不中断
            }
        }

        return totalInsertedResult;
    }

    /**
     * 生成一批用户数据
     */
    private List<User> generateUserBatch(int startIndex, int batchSize) {
        List<User> userList = new ArrayList<>(batchSize);
        Random random = new Random();

        for (int i = 0; i < batchSize; i++) {
            int index = startIndex + i;
            User user = new User();

            // 学号：2024 + 8位序号（带前缀）
            user.setStudentNo(String.format("S%010d", index + 1));

            // 姓名：随机组合
            String surname = SURNAMES[random.nextInt(SURNAMES.length)];
            String givenName = GIVEN_NAMES[random.nextInt(GIVEN_NAMES.length)];
            // 偶尔生成双字名
            if (random.nextDouble() > 0.7) {
                givenName += GIVEN_NAMES[random.nextInt(GIVEN_NAMES.length)];
            }
            user.setName(surname + givenName);

            // 班级：随机选择
            user.setClazz(CLASSES[random.nextInt(CLASSES.length)]);

            // 性别：1-男，2-女
            user.setSex(random.nextInt(2) + 1);

            // 年龄：18-35岁
            user.setAge(random.nextInt(18) + 18);

            // 学生信息：随机选择或拼接
            String info = INFOS[random.nextInt(INFOS.length)];
            if (random.nextDouble() > 0.8) {
                info += "，有" + (random.nextInt(5) + 1) + "个项目经验";
            }
            if (random.nextDouble() > 0.9) {
                info += "，GPA: " + String.format("%.2f", 3.0 + random.nextDouble() * 1.0);
            }
            user.setStudentInfo(info);

            userList.add(user);
        }

        return userList;
    }
}