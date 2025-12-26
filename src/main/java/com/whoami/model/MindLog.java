package com.whoami.model;

import java.sql.Date;
import java.sql.Timestamp;

public class MindLog {
    private int logId;
    private int userId;
    private Date logDate;
    private int focusLevel;
    private int energyLevel;
    private int emotionalLoad;
    private int clarityLevel;
    private int stressLevel;
    private double mindScore;
    private String mindState;
    private Timestamp createdAt;
    
    public MindLog() {}
    
    public MindLog(int userId, Date logDate, int focusLevel, int energyLevel, 
                   int emotionalLoad, int clarityLevel, int stressLevel, 
                   double mindScore, String mindState) {
        this.userId = userId;
        this.logDate = logDate;
        this.focusLevel = focusLevel;
        this.energyLevel = energyLevel;
        this.emotionalLoad = emotionalLoad;
        this.clarityLevel = clarityLevel;
        this.stressLevel = stressLevel;
        this.mindScore = mindScore;
        this.mindState = mindState;
    }
    
    public int getLogId() {
        return logId;
    }
    
    public void setLogId(int logId) {
        this.logId = logId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public Date getLogDate() {
        return logDate;
    }
    
    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }
    
    public int getFocusLevel() {
        return focusLevel;
    }
    
    public void setFocusLevel(int focusLevel) {
        this.focusLevel = focusLevel;
    }
    
    public int getEnergyLevel() {
        return energyLevel;
    }
    
    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }
    
    public int getEmotionalLoad() {
        return emotionalLoad;
    }
    
    public void setEmotionalLoad(int emotionalLoad) {
        this.emotionalLoad = emotionalLoad;
    }
    
    public int getClarityLevel() {
        return clarityLevel;
    }
    
    public void setClarityLevel(int clarityLevel) {
        this.clarityLevel = clarityLevel;
    }
    
    public int getStressLevel() {
        return stressLevel;
    }
    
    public void setStressLevel(int stressLevel) {
        this.stressLevel = stressLevel;
    }
    
    public double getMindScore() {
        return mindScore;
    }
    
    public void setMindScore(double mindScore) {
        this.mindScore = mindScore;
    }
    
    public String getMindState() {
        return mindState;
    }
    
    public void setMindState(String mindState) {
        this.mindState = mindState;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
