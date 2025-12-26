package com.whoami.util;

import com.whoami.model.*;
import com.whoami.dao.*;

import java.time.LocalDate;
import java.util.List;

public class GrowthAnalyzer {

    public static GrowthMetric calculateDailyGrowth(int userId, LocalDate date) {
        GrowthMetric metric = new GrowthMetric(userId, date);

        double mindScore = calculateMindScore(userId, date);
        double taskEfficiency = calculateTaskEfficiency(userId, date);
        double studyConsistency = calculateStudyConsistency(userId, date);
        double loadBalance = calculateLoadBalance(userId, date);

        double overallGrowth = (mindScore * 0.3) + (taskEfficiency * 0.3) + 
                               (studyConsistency * 0.2) + (loadBalance * 0.2);

        metric.setMindScore(mindScore);
        metric.setTaskEfficiency(taskEfficiency);
        metric.setStudyConsistency(studyConsistency);
        metric.setLoadBalance(loadBalance);
        metric.setOverallGrowth(overallGrowth);

        return metric;
    }

    private static double calculateMindScore(int userId, LocalDate date) {
        return 50.0;
    }

    private static double calculateTaskEfficiency(int userId, LocalDate date) {
        TaskDAO taskDAO = new TaskDAO();
        List<Task> todayTasks = taskDAO.getTodayTasks(userId);
        
        if (todayTasks.isEmpty()) {
            return 50.0;
        }
        
        long completedTasks = todayTasks.stream().filter(Task::isCompleted).count();
        return ((double) completedTasks / todayTasks.size()) * 100.0;
    }

    private static double calculateStudyConsistency(int userId, LocalDate date) {
        return 50.0;
    }

    private static double calculateLoadBalance(int userId, LocalDate date) {
        LoadAnalysisDAO analysisDAO = new LoadAnalysisDAO();
        LoadAnalysis analysis = analysisDAO.getAnalysisByDate(userId, date);
        
        return LoadBalanceCalculator.calculateLoadBalanceScore(analysis);
    }

    public static String generateWeeklyInsight(List<GrowthMetric> weeklyMetrics) {
        if (weeklyMetrics.isEmpty()) {
            return "Start tracking your daily progress to see meaningful growth insights here.";
        }

        double avgGrowth = weeklyMetrics.stream()
            .mapToDouble(GrowthMetric::getOverallGrowth)
            .average()
            .orElse(0.0);

        if (weeklyMetrics.size() >= 2) {
            GrowthMetric recent = weeklyMetrics.get(weeklyMetrics.size() - 1);
            GrowthMetric previous = weeklyMetrics.get(0);
            double improvement = recent.getOverallGrowth() - previous.getOverallGrowth();

            if (improvement > 15) {
                return "Remarkable progress this week. Your overall growth has improved by " + 
                       String.format("%.1f", improvement) + " points. Keep this momentum going.";
            } else if (improvement > 5) {
                return "Steady improvement this week. You're building positive habits consistently.";
            } else if (improvement > -5) {
                return "You're maintaining stability this week. Small, consistent efforts matter.";
            } else {
                return "This week had its challenges. Remember, growth isn't always linear. What you learned matters.";
            }
        }

        if (avgGrowth >= 70) {
            return "Excellent week overall. Your consistency and effort are clearly paying off.";
        } else if (avgGrowth >= 50) {
            return "Solid week. You're making meaningful progress in multiple areas.";
        } else {
            return "Every week is a learning experience. Focus on one area to improve this coming week.";
        }
    }

    public static String generateMonthlyInsight(List<GrowthMetric> monthlyMetrics) {
        if (monthlyMetrics.isEmpty()) {
            return "Begin your growth journey. Track daily to unlock meaningful monthly insights.";
        }

        // double avgGrowth = monthlyMetrics.stream()
        //     .mapToDouble(GrowthMetric::getOverallGrowth)
        //     .average()
        //     .orElse(0.0);

        long daysTracked = monthlyMetrics.size();
        double consistencyRate = (daysTracked / 30.0) * 100.0;

        if (monthlyMetrics.size() >= 10) {
            List<GrowthMetric> firstWeek = monthlyMetrics.subList(0, Math.min(7, monthlyMetrics.size()));
            List<GrowthMetric> lastWeek = monthlyMetrics.subList(
                Math.max(0, monthlyMetrics.size() - 7), monthlyMetrics.size()
            );

            double firstWeekAvg = firstWeek.stream().mapToDouble(GrowthMetric::getOverallGrowth).average().orElse(0.0);
            double lastWeekAvg = lastWeek.stream().mapToDouble(GrowthMetric::getOverallGrowth).average().orElse(0.0);
            double monthlyImprovement = lastWeekAvg - firstWeekAvg;

            if (monthlyImprovement > 20) {
                return "Transformative month. You've grown significantly across all areas. This is the kind of progress that compounds over time.";
            } else if (monthlyImprovement > 10) {
                return "Strong monthly growth. You're building the foundation for long-term success.";
            } else if (monthlyImprovement > 0) {
                return "Positive trajectory this month. Small gains add up to meaningful change.";
            } else if (monthlyImprovement > -10) {
                return "This month had ups and downs, which is completely normal. You're learning what works for you.";
            } else {
                return "Challenging month. Remember, setbacks are part of growth. Use this as insight, not judgment.";
            }
        }

        if (consistencyRate >= 80) {
            return "Outstanding consistency this month. You showed up " + daysTracked + " days. That dedication matters.";
        } else if (consistencyRate >= 50) {
            return "Good tracking consistency. The more data you gather, the clearer your growth patterns become.";
        } else {
            return "Try tracking more regularly next month. Consistency in tracking leads to consistency in growth.";
        }
    }

    public static String analyzeTrend(List<GrowthMetric> metrics) {
        if (metrics.size() < 3) {
            return "Building";
        }

        int improvements = 0;
        int declines = 0;

        for (int i = 1; i < metrics.size(); i++) {
            double diff = metrics.get(i).getOverallGrowth() - metrics.get(i - 1).getOverallGrowth();
            if (diff > 2) improvements++;
            if (diff < -2) declines++;
        }

        if (improvements > declines * 2) {
            return "Ascending";
        } else if (declines > improvements * 2) {
            return "Fluctuating";
        } else {
            return "Steady";
        }
    }
}
