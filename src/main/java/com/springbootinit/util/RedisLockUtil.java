package com.springbootinit.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis 分布式锁工具类
 * 基于 Redis 的 SET NX EX 和 Lua 脚本实现
 */
@Component
public class RedisLockUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 尝试获取锁（默认超时时间 30 秒）
     *
     * @param lockKey 锁的键
     * @return 锁的唯一标识（用于释放锁时验证），获取失败返回 null
     */
    public String tryLock(String lockKey) {
        return tryLock(lockKey, 30, TimeUnit.SECONDS);
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey  锁的键
     * @param timeout  超时时间
     * @param timeUnit 时间单位
     * @return 锁的唯一标识（用于释放锁时验证），获取失败返回 null
     */
    public String tryLock(String lockKey, long timeout, TimeUnit timeUnit) {
        // 生成唯一标识，用于释放锁时验证是否是当前线程持有的锁
        String lockValue = UUID.randomUUID() + ":" + Thread.currentThread().getId();
        // 使用 SET NX EX 命令：如果 key 不存在则设置，并设置过期时间
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockValue, timeout, timeUnit);
        return Boolean.TRUE.equals(success) ? lockValue : null;
    }

    /**
     * 尝试获取锁（指定过期时间，单位：秒）
     *
     * @param lockKey    锁的键
     * @param expireTime 过期时间（秒）
     * @return 锁的唯一标识，获取失败返回 null
     */
    public String tryLock(String lockKey, long expireTime) {
        return tryLock(lockKey, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 释放锁（使用 Lua 脚本保证原子性）
     *
     * @param lockKey   锁的键
     * @param lockValue 锁的唯一标识（由 tryLock 返回）
     * @return 是否释放成功
     */
    public boolean unlock(String lockKey, String lockValue) {
        if (lockKey == null || lockValue == null) {
            return false;
        }
        // Lua 脚本：先判断锁的值是否匹配，匹配才删除
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = redisTemplate.execute(redisScript, Arrays.asList(lockKey), lockValue);
        return Long.valueOf(1).equals(result);
    }

    /**
     * 判断锁是否被持有
     *
     * @param lockKey 锁的键
     * @return 是否被持有
     */
    public boolean isLocked(String lockKey) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(lockKey));
    }

    /**
     * 获取锁的持有者标识
     *
     * @param lockKey 锁的键
     * @return 持有者标识，不存在返回 null
     */
    public String getLockHolder(String lockKey) {
        Object value = redisTemplate.opsForValue().get(lockKey);
        return value != null ? value.toString() : null;
    }

    /**
     * 强制释放锁（谨慎使用，会覆盖其他线程的锁）
     *
     * @param lockKey 锁的键
     */
    public void forceUnlock(String lockKey) {
        redisTemplate.delete(lockKey);
    }

    /**
     * 获取锁的剩余过期时间
     *
     * @param lockKey 锁的键
     * @return 剩余时间（毫秒），锁不存在返回 -2
     */
    public Long getLockTtl(String lockKey) {
        return redisTemplate.getExpire(lockKey, TimeUnit.MILLISECONDS);
    }
}