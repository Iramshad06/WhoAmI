package com.whoami.util;

import com.whoami.dao.TaskDAO;
import com.whoami.model.Task;

import java.sql.Date;
import java.util.List;

public class RoutineTaskResetter {

    private TaskDAO taskDAO;

    public RoutineTaskResetter() {
        this.taskDAO = new TaskDAO();
    }

    public void checkAndResetRoutineTasks(int userId) {
        List<Task> routineTasks = taskDAO.getRoutineTasks(userId);
        Date today = new Date(System.currentTimeMillis());
        
        for (Task task : routineTasks) {
            if (task.getTaskDate() != null && !task.getTaskDate().equals(today)) {
                Task updatedTask = new Task();
                updatedTask.setTaskId(task.getTaskId());
                updatedTask.setUserId(task.getUserId());
                updatedTask.setCategoryId(task.getCategoryId());
                updatedTask.setTitle(task.getTitle());
                updatedTask.setDescription(task.getDescription());
                updatedTask.setTaskType("ROUTINE");
                updatedTask.setTaskDate(today);
                updatedTask.setTaskTime(task.getTaskTime());
                
                taskDAO.updateTask(updatedTask);
                taskDAO.uncompleteTask(task.getTaskId(), userId);
            }
        }
    }

    public void ensureRoutineTasksExistForToday(int userId) {
        List<Task> routineTasks = taskDAO.getRoutineTasks(userId);
        Date today = new Date(System.currentTimeMillis());
        
        for (Task task : routineTasks) {
            if (task.getTaskDate() == null || task.getTaskDate().before(today)) {
                task.setTaskDate(today);
                task.setCompleted(false);
                taskDAO.updateTask(task);
            }
        }
    }

    public void performDailyReset(int userId) {
        checkAndResetRoutineTasks(userId);
        ensureRoutineTasksExistForToday(userId);
    }
}
