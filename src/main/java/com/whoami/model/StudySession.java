package com.whoami.model;

import java.sql.Date;
import java.sql.Timestamp;

public class StudySession {
    private int sessionId;
    private int userId;
    private int logId;
    private Date sessionDate;
    private String suggestedActivity;
    private String suggestedIntensity;
    private boolean followed;
    private Timestamp createdAt;
    
    public StudySession() {}
    
    public StudySession(int userId, int logId, Date sessionDate, 
                       String suggestedActivity, String suggestedIntensity) {
        this.userId = userId;
        this.logId = logId;
        this.sessionDate = sessionDate;
        this.suggestedActivity = suggestedActivity;
        this.suggestedIntensity = suggestedIntensity;
        this.followed = false;
    }
    
    public int getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getLogId() {
        return logId;
    }
    
    public void setLogId(int logId) {
        this.logId = logId;
    }
    
    public Date getSessionDate() {
        return sessionDate;
    }
    
    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }
    
    public String getSuggestedActivity() {
        return suggestedActivity;
    }
    
    public void setSuggestedActivity(String suggestedActivity) {
        this.suggestedActivity = suggestedActivity;
    }
    
    public String getSuggestedIntensity() {
        return suggestedIntensity;
    }
    
    public void setSuggestedIntensity(String suggestedIntensity) {
        this.suggestedIntensity = suggestedIntensity;
    }
    
    public boolean isFollowed() {
        return followed;
    }
    
    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
