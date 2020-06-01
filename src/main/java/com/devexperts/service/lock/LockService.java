package com.devexperts.service.lock;

public interface LockService<K, D> {

    /**
     * Lock for key
     *
     * @return result of callable
     * @throws LockNotFoundException lock not found for key
     */
    <R> R lock(K key, LockCallable<R, D> callable) throws LockNotFoundException;

    /**
     * Lock for keys
     *
     * @return result of callable
     * @throws LockNotFoundException lock not found for key
     */
    <R> R lock(K key1, K key2, LocksCallable<R, D> callable) throws LockNotFoundException;


    @FunctionalInterface
    interface LockCallable<R, D> {
        R call(D data);
    }

    @FunctionalInterface
    interface LocksCallable<R, D> {
        R call(D data1, D data2);
    }

    class LockNotFoundException extends RuntimeException {
        private final Object lock;

        public LockNotFoundException(Object lock) {
            super(String.format("Not found lock for [%s]", lock));
            this.lock = lock;
        }

        public Object getLock() {
            return lock;
        }
    }
}
