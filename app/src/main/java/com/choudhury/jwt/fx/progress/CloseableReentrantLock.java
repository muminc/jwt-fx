package com.choudhury.jwt.fx.progress;

import java.util.concurrent.locks.ReentrantLock;

public class CloseableReentrantLock extends ReentrantLock implements AutoCloseable {
    public CloseableReentrantLock open() {
        this.lock();
        return this;
    }

    public CloseableReentrantLock open(int timeout) {
        this.lock();
        return this;
    }

    @Override
    public void close() {
        this.unlock();
    }
}
