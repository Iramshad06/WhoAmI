package com.whoami.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GrowthMetric {
    private int metricId;
    private int userId;
    private LocalDate metricDate;
    private double mindScore;
    private double taskEfficiency;
    private double studyConsistency;
    private double loadBalance;
    private double overallGrowth;
    private LocalDateTime createdAt;

    public GrowthMetric() {
    }

    public GrowthMetric(int userId, LocalDate metricDate) {
        this.userId = userId;
        this.metricDate = metricDate;
    }

    public int getMetricId() {
        return metricId;
    }

    public void setMetricId(int metricId) {
        this.metricId = metricId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getMetricDate() {
        return metricDate;
    }

    public void setMetricDate(LocalDate metricDate) {
        this.metricDate = metricDate;
    }

    public double getMindScore() {
        return mindScore;
    }

    public void setMindScore(double mindScore) {
        this.mindScore = mindScore;
    }

    public double getTaskEfficiency() {
        return taskEfficiency;
    }

    public void setTaskEfficiency(double taskEfficiency) {
        this.taskEfficiency = taskEfficiency;
    }

    public double getStudyConsistency() {
        return studyConsistency;
    }

    public void setStudyConsistency(double studyConsistency) {
        this.studyConsistency = studyConsistency;
    }

    public double getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(double loadBalance) {
        this.loadBalance = loadBalance;
    }

    public double getOverallGrowth() {
        return overallGrowth;
    }

    public void setOverallGrowth(double overallGrowth) {
        this.overallGrowth = overallGrowth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getGrowthTrend() {
        if (overallGrowth >= 75.0) return "Excellent";
        if (overallGrowth >= 60.0) return "Strong";
        if (overallGrowth >= 40.0) return "Steady";
        if (overallGrowth >= 25.0) return "Building";
        return "Starting";
    }
}
