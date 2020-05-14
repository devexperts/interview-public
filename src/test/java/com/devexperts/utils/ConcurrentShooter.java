package com.devexperts.utils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class ConcurrentShooter {

    private final int availableThreads;
    private final int repeatCount;

    public ConcurrentShooter(int availableThreads, int repeatCount) {
        this.availableThreads = availableThreads;
        this.repeatCount = repeatCount;
    }

    public void shootConcurrently(Runnable action) throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(availableThreads);
        CountDownLatch countDownLatch = new CountDownLatch(availableThreads);
        for (int i = 0; i < availableThreads; i++) {
            CyclicBarrierRunnable cyclicBarrierRunnable = new CyclicBarrierRunnable(barrier, countDownLatch, action);
            cyclicBarrierRunnable.start();
        }
        countDownLatch.await();
    }

    private class CyclicBarrierRunnable extends Thread {

        private final CyclicBarrier cyclicBarrier;
        private final CountDownLatch finishCounter;
        private final Runnable action;

        private CyclicBarrierRunnable(CyclicBarrier startBarrier, CountDownLatch finishCounter, Runnable action) {
            this.cyclicBarrier = startBarrier;
            this.finishCounter = finishCounter;
            this.action = action;
        }

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
                repeat(action);
                finishCounter.countDown();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }


    }

    private void repeat(Runnable action) {
        for (int i = 0; i < this.repeatCount; i++) {
            action.run();
        }
    }

}
