package com.cherlshall.butterfly.common.lock;

/**
 * 分布式锁顶层接口
 * Created by htf on 2020/9/24.
 */
public interface DistributedLock {

    /**
     * 尝试获取锁
     * @param resourceKey 需要锁的资源的标识
     * @param callerKey 获取者的标识，以此获得可重入的特性
     * @param expire 锁的过期时间
     * @return 成功获得返回 true
     */
    boolean tryLock(String resourceKey, CallerKey callerKey, Integer expire);

    void lock(String resourceKey, CallerKey callerKey, Integer expire) throws InterruptedException;

    void lock(String resourceKey, CallerKey callerKey, Integer expire, Integer timeout) throws InterruptedException;

    /**
     * 解除锁
     * @return 锁的持有者为 callerKey 时返回 true
     */
    boolean unlock(String resourceKey, CallerKey callerKey);

    /**
     * 续约
     * @param prolong 续约时长
     * @return 续约成功返回 true
     */
    boolean renewal(String resourceKey, CallerKey callerKey, Integer prolong);
}
