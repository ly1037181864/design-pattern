package cn.topideal.com.multithread.lock.definition;

public interface DefinitionLock {
    /**
     * 加锁
     */
    void lock();

    /**
     * 解锁
     */
    void unlock();
}
