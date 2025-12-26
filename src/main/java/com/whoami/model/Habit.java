package com.whoami.model;

import java.sql.Timestamp;

public class Habit {
    private int habitId;
    private int userId;
    private int categoryId;
    private String title;
    private String description;
    private String frequency;
    private int currentStreak;
    private int bestStreak;
    private int totalCompletions;
    private Timestamp createdAt;
    private boolean isActive;
    
    private Category category;
    
    public Habit() {}
    
    public Habit(int userId, int categoryId, String title, String description, String frequency) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.frequency = frequency;
    }
    
    public int getHabitId() {
        return habitId;
    }
    
    public void setHabitId(int habitId) {
        this.habitId = habitId;
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
    
    public String getFrequency() {
        return frequency;
    }
    
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    
    public int getCurrentStreak() {
        return currentStreak;
    }
    
    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }
    
    public int getBestStreak() {
        return bestStreak;
    }
    
    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }
    
    public int getTotalCompletions() {
        return totalCompletions;
    }
    
    public void setTotalCompletions(int totalCompletions) {
        this.totalCompletions = totalCompletions;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public int getCompletionPercentage(int totalDays) {
        if (totalDays == 0) return 0;
        return (totalCompletions * 100) / totalDays;
    }
}
