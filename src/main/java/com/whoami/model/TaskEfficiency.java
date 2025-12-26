package com.whoami.model;

public class TaskEfficiency {
    private double dailyEfficiency;
    private double weeklyEfficiency;
    private int totalTasks;
    private int completedTasks;
    private int pendingTasks;
    private String motivationalQuote;
    private String efficiencyLevel;

    public TaskEfficiency() {}

    public TaskEfficiency(double dailyEfficiency, double weeklyEfficiency, int totalTasks, 
                         int completedTasks, int pendingTasks) {
        this.dailyEfficiency = dailyEfficiency;
        this.weeklyEfficiency = weeklyEfficiency;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.pendingTasks = pendingTasks;
        this.efficiencyLevel = calculateEfficiencyLevel(dailyEfficiency);
    }

    public double getDailyEfficiency() {
        return dailyEfficiency;
    }

    public void setDailyEfficiency(double dailyEfficiency) {
        this.dailyEfficiency = dailyEfficiency;
    }

    public double getWeeklyEfficiency() {
        return weeklyEfficiency;
    }

    public void setWeeklyEfficiency(double weeklyEfficiency) {
        this.weeklyEfficiency = weeklyEfficiency;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public int getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(int pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public String getMotivationalQuote() {
        return motivationalQuote;
    }

    public void setMotivationalQuote(String motivationalQuote) {
        this.motivationalQuote = motivationalQuote;
    }

    public String getEfficiencyLevel() {
        return efficiencyLevel;
    }

    public void setEfficiencyLevel(String efficiencyLevel) {
        this.efficiencyLevel = efficiencyLevel;
    }

    private String calculateEfficiencyLevel(double efficiency) {
        if (efficiency >= 80) {
            return "Excellent";
        } else if (efficiency >= 60) {
            return "Great";
        } else if (efficiency >= 40) {
            return "Good";
        } else if (efficiency >= 20) {
            return "Fair";
        } else {
            return "Needs Focus";
        }
    }
}
