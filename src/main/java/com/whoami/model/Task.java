package com.whoami.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Task {
    private int taskId;
    private int userId;
    private int categoryId;
    private String title;
    private String description;
    private String taskType;
    private Date taskDate;
    private Time taskTime;
    private boolean isCompleted;
    private Timestamp createdAt;
    private Timestamp completedAt;
    private String categoryName;

    public Task() {}

    public Task(int userId, int categoryId, String title, String description, String taskType, 
                Date taskDate, Time taskTime) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.isCompleted = false;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public Time getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Time taskTime) {
        this.taskTime = taskTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isOverdue() {
        if (taskDate == null || isCompleted) {
            return false;
        }
        Date today = new Date(System.currentTimeMillis());
        return taskDate.before(today);
    }

    public boolean isToday() {
        if (taskDate == null) {
            return false;
        }
        Date today = new Date(System.currentTimeMillis());
        return taskDate.equals(today);
    }

    public boolean isUpcoming() {
        if (taskDate == null || isCompleted) {
            return false;
        }
        Date today = new Date(System.currentTimeMillis());
        return taskDate.after(today);
    }
}
