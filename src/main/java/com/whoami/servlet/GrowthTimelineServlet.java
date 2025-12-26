package com.whoami.servlet;

import com.whoami.dao.GrowthMetricDAO;
import com.whoami.model.GrowthMetric;
import com.whoami.util.GrowthAnalyzer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/growth-timeline")
public class GrowthTimelineServlet extends HttpServlet {

    private GrowthMetricDAO metricDAO = new GrowthMetricDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String viewParam = request.getParameter("view");
        String view = (viewParam != null) ? viewParam : "week";

        List<GrowthMetric> metrics;
        String insightMessage;
        String trendAnalysis;

        if ("month".equals(view)) {
            metrics = metricDAO.getMonthlyMetrics(userId);
            insightMessage = GrowthAnalyzer.generateMonthlyInsight(metrics);
            trendAnalysis = GrowthAnalyzer.analyzeTrend(metrics);
        } else {
            metrics = metricDAO.getWeeklyMetrics(userId);
            insightMessage = GrowthAnalyzer.generateWeeklyInsight(metrics);
            trendAnalysis = GrowthAnalyzer.analyzeTrend(metrics);
        }

        double avgGrowth = metrics.isEmpty() ? 0.0 : 
            metrics.stream().mapToDouble(GrowthMetric::getOverallGrowth).average().orElse(0.0);

        LocalDate today = LocalDate.now();
        GrowthMetric todayMetric = metricDAO.getMetricByDate(userId, today);
        if (todayMetric == null) {
            todayMetric = GrowthAnalyzer.calculateDailyGrowth(userId, today);
            metricDAO.saveMetric(todayMetric);
        }

        request.setAttribute("metrics", metrics);
        request.setAttribute("insightMessage", insightMessage);
        request.setAttribute("trendAnalysis", trendAnalysis);
        request.setAttribute("avgGrowth", String.format("%.1f", avgGrowth));
        request.setAttribute("currentView", view);
        request.setAttribute("todayDate", today.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

        request.getRequestDispatcher("/growth-timeline.jsp").forward(request, response);
    }
}
