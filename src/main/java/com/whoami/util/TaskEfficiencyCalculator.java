package com.whoami.util;

import com.whoami.dao.TaskDAO;
import com.whoami.model.TaskEfficiency;

public class TaskEfficiencyCalculator {

    private TaskDAO taskDAO;

    public TaskEfficiencyCalculator() {
        this.taskDAO = new TaskDAO();
    }

    public TaskEfficiency calculateEfficiency(int userId) {
        int todayTotal = taskDAO.getTodayTaskCount(userId);
        int todayCompleted = taskDAO.getTodayCompletedCount(userId);
        int weeklyTotal = taskDAO.getWeeklyTaskCount(userId);
        int weeklyCompleted = taskDAO.getWeeklyCompletedCount(userId);
        
        double dailyEfficiency = 0.0;
        double weeklyEfficiency = 0.0;
        
        if (todayTotal > 0) {
            dailyEfficiency = (todayCompleted * 100.0) / todayTotal;
        }
        
        if (weeklyTotal > 0) {
            weeklyEfficiency = (weeklyCompleted * 100.0) / weeklyTotal;
        }
        
        int pendingTasks = todayTotal - todayCompleted;
        
        TaskEfficiency efficiency = new TaskEfficiency(
            dailyEfficiency, 
            weeklyEfficiency, 
            todayTotal, 
            todayCompleted, 
            pendingTasks
        );
        
        String quote = MotivationalQuotes.getQuoteForEfficiency(dailyEfficiency);
        efficiency.setMotivationalQuote(quote);
        
        return efficiency;
    }

    public String getEfficiencyInsight(double efficiency) {
        if (efficiency >= 80) {
            return "Outstanding performance! You're mastering your tasks.";
        } else if (efficiency >= 60) {
            return "Great progress! Keep up the excellent work.";
        } else if (efficiency >= 40) {
            return "Good effort! A few more tasks to complete.";
        } else if (efficiency >= 20) {
            return "You're making progress. Focus on one task at a time.";
        } else if (efficiency > 0) {
            return "Every journey starts with a single step. You've got this.";
        } else {
            return "Ready to begin? Your tasks are waiting.";
        }
    }
}
