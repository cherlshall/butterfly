package com.cherlshall.butterfly.common.lock;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地锁，用于单节点部署或本地调试
 * Created by htf on 2020/9/24.
 */
@Component
public class LocalLock implements DistributedLock {

    private ConcurrentHashMap<String, LockInfo> lockMap = new ConcurrentHashMap<>();

    @Override
    public boolean tryLock(String resourceKey, CallerKey callerKey, Integer expire) {
        return tryLock(resourceKey, new LockInfo(callerKey), expire);
    }

    private boolean tryLock(String resourceKey, LockInfo lockInfo, Integer expire) {
        long currentTime = System.currentTimeMillis();
        LockInfo beforeLockInfo;
        while (true) {
            lockInfo.expireTime = System.currentTimeMillis() + expire;
            beforeLockInfo = lockMap.putIfAbsent(resourceKey, lockInfo);
            if (beforeLockInfo == null) { // 无锁
                return true;
            } else if (beforeLockInfo.expireTime <= currentTime) { // 已过期，这种情况应该很少
                if (!lockMap.remove(resourceKey, beforeLockInfo)) { // 删除失败说明被抢占了
                    return false;
                }
            } else if (beforeLockInfo.callerKey.equals(lockInfo.callerKey)) { // 可重入
                beforeLockInfo.addLockTime();
                return true;
            } else { // 锁被占用
                return false;
            }
        }
    }

    @Override
    public void lock(String resourceKey, CallerKey callerKey, Integer expire) throws InterruptedException {
        LockInfo lockInfo = new LockInfo(callerKey);
        while (true) {
            if (tryLock(resourceKey, lockInfo, expire)) {
                break;
            } else {
                Thread.sleep(10);
            }
        }
    }

    @Override
    public void lock(String resourceKey, CallerKey callerKey, Integer expire, Integer timeout) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        LockInfo lockInfo = new LockInfo(callerKey);
        while (true) {
            if (tryLock(resourceKey, lockInfo, expire)) {
                break;
            } else {
                if (System.currentTimeMillis() - startTime >= timeout - 10) {
                    throw new InterruptedException("Timeout");
                }
                Thread.sleep(10);
            }
        }
    }

    @Override
    public boolean unlock(String resourceKey, CallerKey callerKey) {
        LockInfo lockInfo = lockMap.get(resourceKey);
        if (lockInfo == null || !lockInfo.callerKey.equals(callerKey)) {
            return false;
        }
        if (lockInfo.subLockTime() == 0) {
            return lockMap.remove(resourceKey, lockInfo);
        }
        return true;
    }

    @Override
    public boolean renewal(String resourceKey, CallerKey callerKey, Integer prolong) {
        LockInfo lockInfo = lockMap.get(resourceKey);
        if (lockInfo != null && lockInfo.callerKey.equals(callerKey)) {
            lockInfo.renewal(prolong);
            return true;
        }
        return false;
    }

    static class LockInfo {
        CallerKey callerKey;
        Long expireTime;
        Integer lockTimes;

        public LockInfo(CallerKey callerKey) {
            this.callerKey = callerKey;
        }

        public void renewal(Integer prolong) {
            this.expireTime += prolong;
        }

        public void addLockTime() {
            if (lockTimes == null) {
                lockTimes = 1;
            } else {
                lockTimes++;
            }
        }

        public int subLockTime() {
            if (lockTimes == null || lockTimes <= 0) {
                return 0;
            }
            return lockTimes--;
        }
    }
}
