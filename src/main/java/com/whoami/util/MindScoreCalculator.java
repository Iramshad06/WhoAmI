package com.whoami.util;

public class MindScoreCalculator {
    
    public static double calculateScore(int focus, int energy, int emotionalLoad, int clarity, int stress) {
        double focusWeight = 0.25;
        double energyWeight = 0.20;
        double emotionalWeight = 0.15;
        double clarityWeight = 0.25;
        double stressWeight = 0.15;
        
        double normalizedStress = 10 - stress;
        double normalizedEmotional = 10 - emotionalLoad;
        
        double score = (focus * focusWeight) + 
                      (energy * energyWeight) + 
                      (normalizedEmotional * emotionalWeight) + 
                      (clarity * clarityWeight) + 
                      (normalizedStress * stressWeight);
        
        return Math.round(score * 100.0) / 100.0;
    }
    
    public static String getMindState(double score) {
        if (score >= 8.5) {
            return "Peak Performance";
        } else if (score >= 7.0) {
            return "High Focus";
        } else if (score >= 5.5) {
            return "Balanced";
        } else if (score >= 4.0) {
            return "Low Energy";
        } else {
            return "Recovery Needed";
        }
    }
    
    public static String getStateColor(String state) {
        switch(state) {
            case "Peak Performance": return "#10b981";
            case "High Focus": return "#3b82f6";
            case "Balanced": return "#8b5cf6";
            case "Low Energy": return "#f59e0b";
            case "Recovery Needed": return "#ef4444";
            default: return "#6b7280";
        }
    }
    
    public static String getStateIcon(String state) {
        switch(state) {
            case "Peak Performance": return "⚡";
            case "High Focus": return "🎯";
            case "Balanced": return "Balanced";
            case "Low Energy": return "🔋";
            case "Recovery Needed": return "🌙";
            default: return "📊";
        }
    }
}
