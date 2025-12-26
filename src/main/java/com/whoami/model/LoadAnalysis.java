package com.whoami.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LoadAnalysis {
    private int analysisId;
    private int userId;
    private LocalDate analysisDate;
    private int totalEntries;
    private int difficultyScore;
    private int urgencyScore;
    private int effortScore;
    private int loadScore;
    private String loadCategory;
    private String insightMessage;
    private LocalDateTime createdAt;

    public LoadAnalysis() {
    }

    public LoadAnalysis(int userId, LocalDate analysisDate) {
        this.userId = userId;
        this.analysisDate = analysisDate;
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(LocalDate analysisDate) {
        this.analysisDate = analysisDate;
    }

    public int getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(int totalEntries) {
        this.totalEntries = totalEntries;
    }

    public int getDifficultyScore() {
        return difficultyScore;
    }

    public void setDifficultyScore(int difficultyScore) {
        this.difficultyScore = difficultyScore;
    }

    public int getUrgencyScore() {
        return urgencyScore;
    }

    public void setUrgencyScore(int urgencyScore) {
        this.urgencyScore = urgencyScore;
    }

    public int getEffortScore() {
        return effortScore;
    }

    public void setEffortScore(int effortScore) {
        this.effortScore = effortScore;
    }

    public int getLoadScore() {
        return loadScore;
    }

    public void setLoadScore(int loadScore) {
        this.loadScore = loadScore;
    }

    public String getLoadCategory() {
        return loadCategory;
    }

    public void setLoadCategory(String loadCategory) {
        this.loadCategory = loadCategory;
    }

    public String getInsightMessage() {
        return insightMessage;
    }

    public void setInsightMessage(String insightMessage) {
        this.insightMessage = insightMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
