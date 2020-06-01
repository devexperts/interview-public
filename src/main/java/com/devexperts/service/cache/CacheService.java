package com.devexperts.service.cache;

public interface CacheService<Key, Obj> {

    /**
     * Clears cache
     */
    void clear();

    /**
     * Put a new obj
     *
     * @param key key of the obj entity to add
     * @param obj obj to add
     * @return Already contained object if we try to add it again
     */
    Obj put(Key key, Obj obj);

    /**
     * Get obj from the cache
     *
     * @param key identification of an obj to search for
     * @return obj associated with given key or {@code null} if obj is not found in the cache
     */
    Obj get(Key key);


    /**
     * Cache clear save execute callable
     */
    <T> T cacheLockExecute(LockCallable<T> callable);


    interface LockCallable<T> {
        T call();
    }
}
