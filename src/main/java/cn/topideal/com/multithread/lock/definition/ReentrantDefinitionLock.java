package cn.topideal.com.multithread.lock.definition;

/**
 * 手写一个简易版的锁
 */
public class ReentrantDefinitionLock implements DefinitionLock {

    private Sync sync;

    public ReentrantDefinitionLock() {
        this.sync = new Sync();
    }

    @Override
    public void lock() {
        sync.lock();
    }

    @Override
    public void unlock() {
        sync.unlock();
    }

    /**
     * 自定义同步器
     */
    static class Sync extends DefinitionAQS {
        void lock() {
            acquire(1);
        }

        void unlock() {
            release(1);
        }

        @Override
        protected boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }

        @Override
        protected boolean tryAcquire(int arg) {
            Thread thread = Thread.currentThread();
            int state = getState();
            if (state == 0) {
                //当前线程获得锁
                if (compareAndSetState(0, arg)) {
                    setExclusiveOwnerThread(thread);
                    return true;
                }
            } else if (thread == getExclusiveOwnerThread()) {
                //判断是否可重入
                int nextState = state + arg;
                if (nextState < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextState);
                return true;
            }
            return false;
        }
    }

}
