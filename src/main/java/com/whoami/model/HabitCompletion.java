package com.whoami.model;

import java.sql.Date;
import java.sql.Timestamp;

public class HabitCompletion {
    private int completionId;
    private int habitId;
    private int userId;
    private Date completionDate;
    private Timestamp completionTime;
    
    public HabitCompletion() {}
    
    public HabitCompletion(int habitId, int userId, Date completionDate) {
        this.habitId = habitId;
        this.userId = userId;
        this.completionDate = completionDate;
    }
    
    public int getCompletionId() {
        return completionId;
    }
    
    public void setCompletionId(int completionId) {
        this.completionId = completionId;
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
    
    public Date getCompletionDate() {
        return completionDate;
    }
    
    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }
    
    public Timestamp getCompletionTime() {
        return completionTime;
    }
    
    public void setCompletionTime(Timestamp completionTime) {
        this.completionTime = completionTime;
    }
}
