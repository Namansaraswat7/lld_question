package org.lld.taskscheduler.service;

import org.lld.taskscheduler.domain.ScheduledTask;
import org.lld.taskscheduler.domain.Task;
import org.lld.taskscheduler.domain.TaskStatus;
import org.lld.taskscheduler.repository.InMemoryTaskRepository;
import org.lld.taskscheduler.repository.TaskRepository;
import org.lld.taskscheduler.strategy.ScheduleStrategy;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskSchedulerService {

    private static final int MIN_WORKERS = 1;

    private static final TaskSchedulerService INSTANCE = new TaskSchedulerService();
    private static final AtomicInteger WORKER_SEQUENCE = new AtomicInteger(0);

    private final DelayQueue<ScheduledTask> delayQueue = new DelayQueue<>();
    private final TaskRepository taskRepository;
    private final AtomicLong sequenceCounter = new AtomicLong(0);

    private ThreadPoolExecutor workers;
    private Thread dispatcherThread;
    private volatile boolean running;

    private TaskSchedulerService() {
        this.taskRepository = new InMemoryTaskRepository();
    }

    public static TaskSchedulerService getInstance() {
        return INSTANCE;
    }

    public void initialize(int workerCount) {
        if (workerCount < MIN_WORKERS) {
            throw new IllegalArgumentException("Worker count must be >= " + MIN_WORKERS);
        }
        if (running) {
            throw new IllegalStateException("Scheduler is already running");
        }

        running = true;
        workers = createWorkerPool(workerCount);
        //workers.prestartAllCoreThreads();

        dispatcherThread = new Thread(this::dispatchLoop, "Scheduler-Dispatcher");
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();
        System.out.printf("Started dispatcher + %d worker threads%n", workerCount);
    }

    public String schedule(Task task, ScheduleStrategy strategy) {
        Objects.requireNonNull(task, "task");
        Objects.requireNonNull(strategy, "strategy");
        if (!running) {
            throw new IllegalStateException("Scheduler is not running. Call initialize() first.");
        }

        ScheduledTask scheduledTask = new ScheduledTask(task, strategy, sequenceCounter.getAndIncrement());
        taskRepository.save(scheduledTask);
        delayQueue.offer(scheduledTask);
        return scheduledTask.getId();
    }

    public boolean cancelTask(String taskId) {
        Objects.requireNonNull(taskId, "taskId");
        return taskRepository.findById(taskId)
                .map(scheduledTask -> {
                    if (scheduledTask.cancel()) {
                        taskRepository.save(scheduledTask);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    public Optional<TaskStatus> getTaskStatus(String taskId) {
        Objects.requireNonNull(taskId, "taskId");
        return taskRepository.findStatusById(taskId);
    }

    public synchronized int addWorkers(int count) {
        ensureRunning();
        if (count <= 0) {
            throw new IllegalArgumentException("count must be positive");
        }
        int newSize = workers.getCorePoolSize() + count;
        resizeWorkerPool(newSize);
        System.out.printf("Added %d worker(s). Pool size is now %d%n", count, newSize);
        return newSize;
    }

    public synchronized int removeWorkers(int count) {
        ensureRunning();
        if (count <= 0) {
            throw new IllegalArgumentException("count must be positive");
        }
        int currentSize = workers.getCorePoolSize();
        int newSize = currentSize - count;
        if (newSize < MIN_WORKERS) {
            throw new IllegalArgumentException(
                    "Cannot remove " + count + " workers. Minimum pool size is " + MIN_WORKERS);
        }
        resizeWorkerPool(newSize);
        System.out.printf("Removed %d worker(s). Pool size is now %d%n", count, newSize);
        return newSize;
    }

    public int getWorkerCount() {
        return workers == null ? 0 : workers.getCorePoolSize();
    }

    public int getActiveWorkerCount() {
        return workers == null ? 0 : workers.getActiveCount();
    }

    public void shutdown() {
        running = false;
        if (dispatcherThread != null) {
            dispatcherThread.interrupt();
        }
        if (workers != null) {
            workers.shutdown();
            try {
                if (!workers.awaitTermination(5, TimeUnit.SECONDS)) {
                    workers.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                workers.shutdownNow();
            }
        }
        System.out.println("Scheduler shut down.");
    }

    private void dispatchLoop() {
        while (running) {
            try {
                ScheduledTask task = delayQueue.take(); // when its time to execute -> this will return the task
                if (task.isCancelled()) {
                    continue;
                }
                workers.submit(() -> execute(task));
            } catch (InterruptedException e) {
                if (!running) {
                    break;
                }
            }
        }
        System.out.println("Scheduler-Dispatcher stopped.");
    }

    private void execute(ScheduledTask task) {
        if (!task.tryMarkRunning()) {
            return;
        }

        taskRepository.save(task);

        try {
            task.getTask().execute();
            task.updateLastExecutionTime();
            handleSuccess(task);
        } catch (Exception e) {
            handleFailure(task, e);
        }
    }

    private void handleSuccess(ScheduledTask task) {
        if (task.isCancelled()) {
            return;
        }

        task.updateNextExecutionTime();
        if (task.hasMoreExecutions()) {
            task.setStatus(TaskStatus.SCHEDULED);
            taskRepository.save(task);
            delayQueue.offer(task);
        } else {
            task.setStatus(TaskStatus.COMPLETED);
            taskRepository.save(task);
        }
    }

    private void handleFailure(ScheduledTask task, Exception e) {
        task.setStatus(TaskStatus.ERROR);
        taskRepository.save(task);
        System.err.printf("Task %s failed with error: %s%n", task.getId(), e.getMessage());
    }

    private ThreadPoolExecutor createWorkerPool(int workerCount) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                workerCount,
                workerCount,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                runnable -> {
                    Thread thread = new Thread(runnable);
                    thread.setName("Scheduler-Worker-" + WORKER_SEQUENCE.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                });
        executor.allowCoreThreadTimeOut(true);

        ///  5 workers
        // 4 workers
        // 5 workers
        return executor;
    }

    private void resizeWorkerPool(int newSize) {
        if (newSize > workers.getCorePoolSize()) {
            workers.setMaximumPoolSize(newSize);
            workers.setCorePoolSize(newSize);
        } else {
            workers.setCorePoolSize(newSize);
            workers.setMaximumPoolSize(newSize);
        }
    }

    private void ensureRunning() {
        if (!running || workers == null) {
            throw new IllegalStateException("Scheduler is not running. Call initialize() first.");
        }
    }
}
