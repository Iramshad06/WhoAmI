package com.whoami.util;

import com.whoami.model.LoadAnalysis;
import com.whoami.model.StudyLoad;

import java.util.List;

public class LoadBalanceCalculator {

    public static LoadAnalysis calculateLoadAnalysis(int userId, List<StudyLoad> loads, java.time.LocalDate date) {
        LoadAnalysis analysis = new LoadAnalysis(userId, date);
        
        if (loads.isEmpty()) {
            analysis.setTotalEntries(0);
            analysis.setLoadScore(0);
            analysis.setLoadCategory("Balanced");
            analysis.setInsightMessage("No study load recorded for today. Take time to plan your academic work.");
            return analysis;
        }

        int totalEntries = loads.size();
        int difficultyScore = 0;
        int urgencyScore = 0;
        int effortScore = 0;

        for (StudyLoad load : loads) {
            difficultyScore += load.getDifficultyScore();
            urgencyScore += load.getUrgencyScore();
            effortScore += load.getEffortScore();
        }

        int loadScore = calculateLoadScore(totalEntries, difficultyScore, urgencyScore, effortScore);
        String loadCategory = determineLoadCategory(loadScore, totalEntries);
        String insightMessage = generateInsightMessage(loadCategory, totalEntries, difficultyScore, urgencyScore);

        analysis.setTotalEntries(totalEntries);
        analysis.setDifficultyScore(difficultyScore);
        analysis.setUrgencyScore(urgencyScore);
        analysis.setEffortScore(effortScore);
        analysis.setLoadScore(loadScore);
        analysis.setLoadCategory(loadCategory);
        analysis.setInsightMessage(insightMessage);

        return analysis;
    }

    private static int calculateLoadScore(int totalEntries, int difficultyScore, int urgencyScore, int effortScore) {
        double avgDifficulty = (double) difficultyScore / totalEntries;
        double avgUrgency = (double) urgencyScore / totalEntries;
        double avgEffort = (double) effortScore / totalEntries;
        
        double weightedScore = (avgDifficulty * 0.4) + (avgUrgency * 0.3) + (avgEffort * 0.3);
        double entryFactor = Math.min(totalEntries / 3.0, 1.5);
        
        return (int) Math.round(weightedScore * entryFactor * 33.33);
    }

    private static String determineLoadCategory(int loadScore, int totalEntries) {
        if (totalEntries >= 7 || loadScore >= 80) {
            return "Overloaded";
        } else if (totalEntries >= 5 || loadScore >= 60) {
            return "Heavy";
        } else {
            return "Balanced";
        }
    }

    private static String generateInsightMessage(String category, int totalEntries, int difficultyScore, int urgencyScore) {
        switch (category) {
            case "Overloaded":
                if (difficultyScore >= totalEntries * 2.5) {
                    return "Today's workload feels intense with multiple challenging subjects. Consider focusing on just 2-3 priorities and rescheduling less urgent topics.";
                } else if (urgencyScore >= totalEntries * 2.5) {
                    return "Several urgent items are competing for your attention. Tackle the most time-sensitive one first, then reassess.";
                } else {
                    return "You have " + totalEntries + " subjects planned. That's quite a lot. Remember, quality study time matters more than quantity.";
                }
                
            case "Heavy":
                if (difficultyScore >= totalEntries * 2) {
                    return "Today looks academically demanding. Take short breaks between subjects to stay fresh and focused.";
                } else {
                    return "Your study load is significant but manageable. Pace yourself and celebrate small wins as you progress.";
                }
                
            case "Balanced":
            default:
                if (totalEntries == 1) {
                    return "Single-subject focus today. This is great for deep, concentrated work. Make the most of this clarity.";
                } else if (totalEntries <= 3) {
                    return "Your study load is well balanced today. You have the mental space to engage deeply with your subjects.";
                } else {
                    return "Good academic balance today. Your workload feels sustainable and well-distributed.";
                }
        }
    }

    public static double calculateLoadBalanceScore(LoadAnalysis analysis) {
        if (analysis == null || analysis.getTotalEntries() == 0) {
            return 100.0;
        }
        
        int loadScore = analysis.getLoadScore();
        
        if (loadScore <= 50) {
            return 100.0;
        } else if (loadScore <= 70) {
            return 80.0;
        } else if (loadScore <= 90) {
            return 60.0;
        } else {
            return 40.0;
        }
    }
}
