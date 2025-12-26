package com.whoami.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Streak {
    private int streakId;
    private int userId;
    private String streakType;
    private int currentCount;
    private int bestCount;
    private Date lastActivityDate;
    private Timestamp updatedAt;
    
    public Streak() {}
    
    public Streak(int userId, String streakType) {
        this.userId = userId;
        this.streakType = streakType;
        this.currentCount = 0;
        this.bestCount = 0;
    }
    
    public int getStreakId() {
        return streakId;
    }
    
    public void setStreakId(int streakId) {
        this.streakId = streakId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getStreakType() {
        return streakType;
    }
    
    public void setStreakType(String streakType) {
        this.streakType = streakType;
    }
    
    public int getCurrentCount() {
        return currentCount;
    }
    
    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }
    
    public int getBestCount() {
        return bestCount;
    }
    
    public void setBestCount(int bestCount) {
        this.bestCount = bestCount;
    }
    
    public Date getLastActivityDate() {
        return lastActivityDate;
    }
    
    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
