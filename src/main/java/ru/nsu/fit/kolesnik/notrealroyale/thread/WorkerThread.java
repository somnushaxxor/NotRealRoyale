package ru.nsu.fit.kolesnik.notrealroyale.thread;

import java.util.concurrent.LinkedBlockingQueue;

public class WorkerThread extends Thread {
    private final LinkedBlockingQueue<Runnable> queue;
    private boolean isFree;

    public WorkerThread(LinkedBlockingQueue<Runnable> queue) {
        this.queue = queue;
        isFree = true;
    }

    public void run() {
        Runnable task;
        while (true) {
            synchronized (queue) {
                isFree = true;
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isFree = false;
                task = queue.poll();
            }
            task.run();
        }
    }

    public boolean isFree() {
        return isFree;
    }
}