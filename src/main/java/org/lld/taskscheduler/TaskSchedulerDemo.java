package org.lld.taskscheduler;

import org.lld.taskscheduler.domain.DataBackupTask;
import org.lld.taskscheduler.domain.EmailNotificationTask;
import org.lld.taskscheduler.domain.PrintMessageTask;
import org.lld.taskscheduler.domain.Task;
import org.lld.taskscheduler.domain.TaskStatus;
import org.lld.taskscheduler.service.TaskSchedulerService;
import org.lld.taskscheduler.strategy.OneTimeScheduleStrategy;
import org.lld.taskscheduler.strategy.RecurringSchedulingStrategy;
import org.lld.taskscheduler.strategy.ScheduleStrategy;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskSchedulerDemo {
    public static void main(String[] args) throws InterruptedException {
        TaskSchedulerService scheduler = TaskSchedulerService.getInstance();
        scheduler.initialize(3);

        Task oneTimeTask = new PrintMessageTask("This is a one-time task.");
        ScheduleStrategy oneTimeStrategy = new OneTimeScheduleStrategy(LocalDateTime.now().plusSeconds(1));

        Task recurringTask = new PrintMessageTask("This is a recurring task.");
        ScheduleStrategy recurringStrategy = new RecurringSchedulingStrategy(Duration.ofSeconds(2));

        Task backupTask = new DataBackupTask("/data/source", "/data/backup");
        ScheduleStrategy backupStrategy = new OneTimeScheduleStrategy(LocalDateTime.now().plusSeconds(5));

        Task emailTask = new EmailNotificationTask(
                "naman@gmail.com",
                "Daily Report",
                "Your scheduled report is ready.");
        ScheduleStrategy emailStrategy = new OneTimeScheduleStrategy(LocalDateTime.now().plusSeconds(3));

        System.out.println("Scheduling tasks...");
        String oneTimeId = scheduler.schedule(oneTimeTask, oneTimeStrategy);
        scheduler.schedule(recurringTask, recurringStrategy);
        scheduler.schedule(emailTask, emailStrategy);
        scheduler.schedule(backupTask, backupStrategy);

        Thread.sleep(4_000);
        System.out.println("Active workers before scale up: " + scheduler.getActiveWorkerCount()
                + ", pool size: " + scheduler.getWorkerCount());
        scheduler.addWorkers(2);

        Thread.sleep(4_000);
        System.out.println("Pool size before scale down: " + scheduler.getWorkerCount());
        scheduler.removeWorkers( 2);

        Thread.sleep(4_000);
        System.out.println("One-time task status: " + scheduler.getTaskStatus(oneTimeId).orElse(TaskStatus.ERROR));

        scheduler.shutdown();
    }
}
