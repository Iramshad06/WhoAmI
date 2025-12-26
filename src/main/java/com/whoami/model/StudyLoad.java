package com.whoami.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudyLoad {
    private int entryId;
    private int userId;
    private String subjectName;
    private String difficulty;
    private String urgency;
    private String effort;
    private LocalDate entryDate;
    private LocalDateTime createdAt;

    public StudyLoad() {
    }

    public StudyLoad(int userId, String subjectName, String difficulty, String urgency, 
                     String effort, LocalDate entryDate) {
        this.userId = userId;
        this.subjectName = subjectName;
        this.difficulty = difficulty;
        this.urgency = urgency;
        this.effort = effort;
        this.entryDate = entryDate;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getEffort() {
        return effort;
    }

    public void setEffort(String effort) {
        this.effort = effort;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getDifficultyScore() {
        switch (difficulty) {
            case "Hard": return 3;
            case "Medium": return 2;
            case "Easy": return 1;
            default: return 1;
        }
    }

    public int getUrgencyScore() {
        switch (urgency) {
            case "High": return 3;
            case "Medium": return 2;
            case "Low": return 1;
            default: return 1;
        }
    }

    public int getEffortScore() {
        switch (effort) {
            case "Heavy": return 3;
            case "Moderate": return 2;
            case "Light": return 1;
            default: return 1;
        }
    }
}
