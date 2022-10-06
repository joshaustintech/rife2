/*
 * Copyright 2001-2022 Geert Bevin <gbevin[remove] at uwyn dot com>
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.scheduler.taskmanagers;

import org.junit.jupiter.api.Test;
import rife.scheduler.Task;
import rife.scheduler.TaskManager;
import rife.scheduler.TestTasktypes;
import rife.scheduler.exceptions.FrequencyException;
import rife.scheduler.exceptions.TaskManagerException;
import rife.tools.ExceptionUtils;

import java.util.Calendar;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class TestMemoryTasks {
    @Test
    public void testInstantiateTaskManager() {
        TaskManager manager = null;
        manager = new MemoryTasks();
        assertNotNull(manager);
    }

    @Test
    public void testAddTask() {
        int task_id = -1;

        String type = TestTasktypes.UPLOAD_GROUPS;
        Calendar cal = Calendar.getInstance();
        cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);
        long planned = cal.getTime().getTime();
        String frequency = "* * * * *";
        boolean busy = false;

        Task task = new Task();
        try {
            task.setType(type);
            task.setPlanned(planned);
            task.setFrequency(frequency);
            task.setBusy(busy);
        } catch (FrequencyException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }

        TaskManager manager = new MemoryTasks();
        try {
            task_id = manager.addTask(task);
            assertTrue(task_id >= 0);
        } catch (TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    public void testGetTask() {
        int task_id = -1;
        Task task = null;
        TaskManager manager = new MemoryTasks();
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);

            task = new Task();
            task.setType(TestTasktypes.UPLOAD_GROUPS);
            task.setPlanned(cal.getTime().getTime());
            task.setFrequency("* * * * *");
            task.setBusy(false);
            task_id = manager.addTask(task);

            task = manager.getTask(task_id);
            assertNotNull(task);

            assertEquals(task.getId(), task_id);
            assertEquals(task.getType(), TestTasktypes.UPLOAD_GROUPS);
            assertTrue(task.getPlanned() <= cal.getTime().getTime());
            assertEquals(task.getFrequency(), "* * * * *");
            assertFalse(task.isBusy());
            assertSame(task.getTaskManager(), manager);
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    public void testUpdateTask() {
        int task_id = -1;
        Task task = null;
        TaskManager manager = new MemoryTasks();
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);

            task = new Task();
            task.setType(TestTasktypes.UPLOAD_GROUPS);
            task.setPlanned(cal.getTime().getTime());
            task.setFrequency("* * * * *");
            task.setBusy(false);
            task_id = manager.addTask(task);

            cal.set(2002, Calendar.MARCH, 12, 0, 0, 0);
            String type = TestTasktypes.SEND_RANKING;
            long planned = cal.getTime().getTime();
            String frequency = "20 */3 * * *";
            boolean busy = true;

            task = new Task();
            task.setId(task_id);
            task.setType(type);
            task.setPlanned(planned);
            task.setFrequency(frequency);
            task.setBusy(busy);

            assertTrue(manager.updateTask(task));

            task = manager.getTask(task_id);
            assertNotNull(task);

            assertEquals(task.getId(), task_id);
            assertEquals(task.getType(), type);
            assertTrue(task.getPlanned() <= planned);
            assertEquals(task.getFrequency(), frequency);
            assertEquals(task.isBusy(), busy);
            assertSame(task.getTaskManager(), manager);
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    public void testRemoveTask() {
        int task_id = -1;
        Task task = null;
        TaskManager manager = new MemoryTasks();
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(2001, Calendar.NOVEMBER, 24, 0, 0, 0);

            task = new Task();
            task.setType(TestTasktypes.UPLOAD_GROUPS);
            task.setPlanned(cal.getTime().getTime());
            task.setFrequency("* * * * *");
            task.setBusy(false);
            task_id = manager.addTask(task);

            assertTrue(manager.removeTask(task_id));
            task_id = -1;
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    public void testGetNonExistingTask() {
        TaskManager manager = new MemoryTasks();
        int task_nonexisting_id = 0;
        try {
            assertNull(manager.getTask(task_nonexisting_id));
        } catch (TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    public void testRemoveNonExistingTask() {
        TaskManager manager = new MemoryTasks();
        int task_nonexisting_id = 0;
        try {
            assertFalse(manager.removeTask(task_nonexisting_id));
        } catch (TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    public void testGetTasksToProcess() {
        int one_hour = 1000 * 60 * 60;

        TaskManager manager = new MemoryTasks();
        try {
            Task task1 = new Task();
            task1.setType(TestTasktypes.UPLOAD_GROUPS);
            task1.setPlanned(System.currentTimeMillis() - one_hour);
            task1.setFrequency(null);
            task1.setBusy(false);

            Task task2 = new Task();
            task2.setType(TestTasktypes.UPLOAD_GROUPS);
            task2.setPlanned(System.currentTimeMillis() - one_hour);
            task2.setFrequency(null);
            task2.setBusy(false);

            Task task3 = new Task();
            task3.setType(TestTasktypes.UPLOAD_GROUPS);
            task3.setPlanned(System.currentTimeMillis() - one_hour);
            task3.setFrequency(null);
            task3.setBusy(true);

            Task task4 = new Task();
            task4.setType(TestTasktypes.UPLOAD_GROUPS);
            task4.setPlanned(System.currentTimeMillis() + one_hour);
            task4.setFrequency(null);
            task4.setBusy(false);

            task1.setId(manager.addTask(task1));
            task2.setId(manager.addTask(task2));
            task3.setId(manager.addTask(task3));
            task4.setId(manager.addTask(task4));

            Collection<Task> tasks_to_process = manager.getTasksToProcess();

            manager.removeTask(task1.getId());
            manager.removeTask(task2.getId());
            manager.removeTask(task3.getId());
            manager.removeTask(task4.getId());

            assertEquals(2, tasks_to_process.size());
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    public void testGetScheduledTasks() {
        int one_hour = 1000 * 60 * 60;

        TaskManager manager = new MemoryTasks();
        try {
            Task task1 = new Task();
            task1.setType(TestTasktypes.UPLOAD_GROUPS);
            task1.setPlanned(System.currentTimeMillis() - one_hour);
            task1.setFrequency(null);
            task1.setBusy(false);

            Task task2 = new Task();
            task2.setType(TestTasktypes.UPLOAD_GROUPS);
            task2.setPlanned(System.currentTimeMillis() + one_hour);
            task2.setFrequency(null);
            task2.setBusy(true);

            Task task3 = new Task();
            task3.setType(TestTasktypes.UPLOAD_GROUPS);
            task3.setPlanned(System.currentTimeMillis() + one_hour);
            task3.setFrequency(null);
            task3.setBusy(false);

            Task task4 = new Task();
            task4.setType(TestTasktypes.UPLOAD_GROUPS);
            task4.setPlanned(System.currentTimeMillis() + one_hour);
            task4.setFrequency(null);
            task4.setBusy(false);

            task1.setId(manager.addTask(task1));
            task2.setId(manager.addTask(task2));
            task3.setId(manager.addTask(task3));
            task4.setId(manager.addTask(task4));

            Collection<Task> scheduled_tasks = manager.getScheduledTasks();

            manager.removeTask(task1.getId());
            manager.removeTask(task2.getId());
            manager.removeTask(task3.getId());
            manager.removeTask(task4.getId());

            assertEquals(2, scheduled_tasks.size());
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    public void testTaskConclusion() {
        int one_hour = 1000 * 60 * 60;

        TaskManager manager = new MemoryTasks();
        try {
            Task task1 = new Task();
            task1.setType(TestTasktypes.UPLOAD_GROUPS);
            task1.setPlanned(System.currentTimeMillis() - one_hour);
            task1.setFrequency(null);
            task1.setBusy(false);

            Task task2 = new Task();
            task2.setType(TestTasktypes.UPLOAD_GROUPS);
            task2.setPlanned(System.currentTimeMillis() - one_hour);
            task2.setFrequency("0 * * * *");
            task2.setBusy(false);

            Task task3 = new Task();
            task3.setType(TestTasktypes.UPLOAD_GROUPS);
            task3.setPlanned(System.currentTimeMillis() + one_hour);
            task3.setFrequency(null);
            task3.setBusy(false);

            task1.setId(manager.addTask(task1));
            task2.setId(manager.addTask(task2));
            task3.setId(manager.addTask(task3));
            task1 = manager.getTask(task1.getId());
            task2 = manager.getTask(task2.getId());
            task3 = manager.getTask(task3.getId());

            boolean was_task1_concluded = manager.concludeTask(task1);
            boolean was_task2_concluded = manager.concludeTask(task2);
            boolean was_task3_concluded = manager.concludeTask(task3);

            Task task1_new = manager.getTask(task1.getId());
            Task task2_new = manager.getTask(task2.getId());
            Task task3_new = manager.getTask(task3.getId());

            manager.removeTask(task2.getId());
            manager.removeTask(task3.getId());

            assertTrue(was_task1_concluded);
            assertTrue(was_task2_concluded);
            assertFalse(was_task3_concluded);
            assertNull(task1_new);
            assertNotNull(task2_new);
            assertNotNull(task3_new);
            assertSame(task2_new.getTaskManager(), manager);
            assertSame(task3_new.getTaskManager(), manager);
            assertEquals(task2_new.getId(), task2.getId());
            assertEquals(task2_new.getType(), task2.getType());
            assertTrue(task2_new.getPlanned() >= task2.getPlanned());
            assertEquals(task2_new.getFrequency(), task2.getFrequency());
            assertEquals(task2_new.isBusy(), task2.isBusy());
            assertEquals(task3_new.getId(), task3.getId());
            assertEquals(task3_new.getType(), task3.getType());
            assertTrue(task3_new.getPlanned() <= task3.getPlanned());
            assertEquals(task3_new.getFrequency(), task3.getFrequency());
            assertEquals(task3_new.isBusy(), task3_new.isBusy());
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }

    @Test
    public void testTaskActivation() {
        TaskManager manager = new MemoryTasks();
        try {
            Task task = new Task();
            task.setType(TestTasktypes.UPLOAD_GROUPS);
            task.setPlanned(System.currentTimeMillis());
            task.setFrequency(null);
            task.setBusy(false);

            int taskid = manager.addTask(task);

            manager.activateTask(taskid);
            task = manager.getTask(taskid);
            assertSame(task.getTaskManager(), manager);
            assertTrue(task.isBusy());
            manager.deactivateTask(taskid);
            task = manager.getTask(taskid);
            assertSame(task.getTaskManager(), manager);
            assertFalse(task.isBusy());

            manager.removeTask(task.getId());
        } catch (FrequencyException | TaskManagerException e) {
            fail(ExceptionUtils.getExceptionStackTrace(e));
        }
    }
}