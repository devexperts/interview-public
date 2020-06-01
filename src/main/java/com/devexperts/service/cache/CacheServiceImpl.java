package com.devexperts.service.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * In-memory cache
 */
public class CacheServiceImpl<Key, Obj> implements CacheService<Key, Obj> {
    protected final ReadWriteLock clearLock = new ReentrantReadWriteLock();
    protected final Map<Key, Obj> cache = new ConcurrentHashMap<>();

    public void clear() {
        clearLock.writeLock().lock();
        try {
            cache.clear();
        } finally {
            clearLock.writeLock().unlock();
        }
    }

    public Obj put(Key key, Obj obj) {
        return cacheLockExecute(() -> cache.putIfAbsent(key, obj));
    }

    public Obj get(Key key) {
        return cacheLockExecute(() -> cache.get(key));
    }

    @Override
    public <T> T cacheLockExecute(LockCallable<T> runnable) {
        clearLock.readLock().lock();
        try {
            return runnable.call();
        } finally {
            clearLock.readLock().unlock();
        }
    }

}
