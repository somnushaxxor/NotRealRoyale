package ru.nsu.fit.kolesnik.notrealroyale.thread;

import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
    private final int poolSize;
    private final WorkerThread[] workers;
    private final LinkedBlockingQueue<Runnable> queue;

    public ThreadPool(int poolSize) {
        this.poolSize = poolSize;
        queue = new LinkedBlockingQueue<>();
        workers = new WorkerThread[poolSize];
        for (int i = 0; i < poolSize; i++) {
            workers[i] = new WorkerThread(queue);
            workers[i].start();
        }
    }

    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    public boolean hasFreeThread() {
        synchronized (queue) {
            for (WorkerThread worker : workers) {
                if (worker.isFree()) {
                    return true;
                }
            }
            return false;
        }
    }

    public void shutdown() {
        for (int i = 0; i < poolSize; i++) {
            workers[i] = null;
        }
    }
}
