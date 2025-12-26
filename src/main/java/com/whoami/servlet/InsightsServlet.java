package com.whoami.servlet;

import com.whoami.dao.*;
import com.whoami.model.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/insights")
public class InsightsServlet extends HttpServlet {
    private MindLogDAO mindLogDAO;
    // private StudySessionDAO studySessionDAO; // Not used
    
    @Override
    public void init() {
        mindLogDAO = new MindLogDAO();
        // studySessionDAO = new StudySessionDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        
        Date endDate = Date.valueOf(LocalDate.now());
        Date weekStartDate = Date.valueOf(LocalDate.now().minusDays(7));
        Date monthStartDate = Date.valueOf(LocalDate.now().minusDays(30));
        
        List<MindLog> weekLogs = mindLogDAO.getLogsBetweenDates(userId, weekStartDate, endDate);
        List<MindLog> monthLogs = mindLogDAO.getLogsBetweenDates(userId, monthStartDate, endDate);
        
        Map<String, Object> weeklyInsights = generateInsights(weekLogs, "week");
        Map<String, Object> monthlyInsights = generateInsights(monthLogs, "month");
        
        request.setAttribute("weeklyInsights", weeklyInsights);
        request.setAttribute("monthlyInsights", monthlyInsights);
        request.setAttribute("weekLogs", weekLogs);
        request.setAttribute("monthLogs", monthLogs);
        
        request.getRequestDispatcher("insights.jsp").forward(request, response);
    }
    
    private Map<String, Object> generateInsights(List<MindLog> logs, String period) {
        Map<String, Object> insights = new HashMap<>();
        
        if (logs.isEmpty()) {
            insights.put("message", "Not enough data yet. Keep checking in daily!");
            return insights;
        }
        
        double avgFocus = logs.stream().mapToInt(MindLog::getFocusLevel).average().orElse(0);
        double avgEnergy = logs.stream().mapToInt(MindLog::getEnergyLevel).average().orElse(0);
        double avgStress = logs.stream().mapToInt(MindLog::getStressLevel).average().orElse(0);
        double avgScore = logs.stream().mapToDouble(MindLog::getMindScore).average().orElse(0);
        
        Map<String, Integer> dayOfWeekScores = new HashMap<>();
        for (MindLog log : logs) {
            String day = log.getLogDate().toLocalDate().getDayOfWeek().toString();
            dayOfWeekScores.put(day, dayOfWeekScores.getOrDefault(day, 0) + (int)log.getMindScore());
        }
        
        String bestDay = dayOfWeekScores.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Unknown");
        
        StringBuilder narrative = new StringBuilder();
        narrative.append("Over the past ").append(period).append(", ");
        
        if (avgScore >= 7.0) {
            narrative.append("you've been performing exceptionally well. ");
        } else if (avgScore >= 5.5) {
            narrative.append("you've maintained a balanced state. ");
        } else {
            narrative.append("you've experienced some challenges. ");
        }
        
        if (avgFocus >= 7.0) {
            narrative.append("Your focus has been strong. ");
        } else if (avgFocus < 5.0) {
            narrative.append("Focus could use improvement - try the Pomodoro technique. ");
        }
        
        if (avgStress > 6.0) {
            narrative.append("Stress levels are elevated - consider meditation or breaks. ");
        } else if (avgStress < 4.0) {
            narrative.append("You're managing stress well. ");
        }
        
        narrative.append("Your best days tend to be ").append(formatDay(bestDay)).append("s.");
        
        insights.put("avgFocus", Math.round(avgFocus * 10) / 10.0);
        insights.put("avgEnergy", Math.round(avgEnergy * 10) / 10.0);
        insights.put("avgStress", Math.round(avgStress * 10) / 10.0);
        insights.put("avgScore", Math.round(avgScore * 10) / 10.0);
        insights.put("bestDay", formatDay(bestDay));
        insights.put("narrative", narrative.toString());
        insights.put("totalCheckins", logs.size());
        
        return insights;
    }
    
    private String formatDay(String day) {
        return day.substring(0, 1) + day.substring(1).toLowerCase();
    }
}
