package com.whoami.servlet;

import com.whoami.dao.*;
import com.whoami.model.*;
import com.whoami.util.LoadBalanceCalculator;
import com.whoami.util.GrowthAnalyzer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/load-balancer")
public class LoadBalancerServlet extends HttpServlet {

    private StudyLoadDAO loadDAO = new StudyLoadDAO();
    private LoadAnalysisDAO analysisDAO = new LoadAnalysisDAO();
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
        String view = (viewParam != null) ? viewParam : "today";

        LocalDate today = LocalDate.now();
        List<StudyLoad> loads;
        LoadAnalysis analysis;

        if ("week".equals(view)) {
            loads = loadDAO.getWeekLoads(userId);
            List<LoadAnalysis> weekAnalyses = analysisDAO.getWeekAnalysis(userId);
            request.setAttribute("weekAnalyses", weekAnalyses);
        } else {
            loads = loadDAO.getTodayLoads(userId);
        }

        analysis = analysisDAO.getTodayAnalysis(userId);
        if (analysis == null) {
            List<StudyLoad> todayLoads = loadDAO.getTodayLoads(userId);
            analysis = LoadBalanceCalculator.calculateLoadAnalysis(userId, todayLoads, today);
            analysisDAO.saveAnalysis(analysis);
        }

        GrowthMetric todayMetric = GrowthAnalyzer.calculateDailyGrowth(userId, today);
        metricDAO.saveMetric(todayMetric);

        request.setAttribute("loads", loads);
        request.setAttribute("analysis", analysis);
        request.setAttribute("currentView", view);
        request.setAttribute("todayDate", today.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));

        request.getRequestDispatcher("/load-balancer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            handleAddLoad(request, userId);
        } else if ("update".equals(action)) {
            handleUpdateLoad(request, userId);
        } else if ("delete".equals(action)) {
            handleDeleteLoad(request, userId);
        }

        recalculateAnalysis(userId);
        response.sendRedirect("load-balancer");
    }

    private void handleAddLoad(HttpServletRequest request, int userId) {
        String subjectName = request.getParameter("subjectName");
        String difficulty = request.getParameter("difficulty");
        String urgency = request.getParameter("urgency");
        String effort = request.getParameter("effort");
        String dateStr = request.getParameter("entryDate");

        LocalDate entryDate = (dateStr != null && !dateStr.isEmpty()) 
            ? LocalDate.parse(dateStr) 
            : LocalDate.now();

        StudyLoad load = new StudyLoad(userId, subjectName, difficulty, urgency, effort, entryDate);
        loadDAO.addStudyLoad(load);
    }

    private void handleUpdateLoad(HttpServletRequest request, int userId) {
        int entryId = Integer.parseInt(request.getParameter("entryId"));
        String subjectName = request.getParameter("subjectName");
        String difficulty = request.getParameter("difficulty");
        String urgency = request.getParameter("urgency");
        String effort = request.getParameter("effort");
        String dateStr = request.getParameter("entryDate");

        LocalDate entryDate = LocalDate.parse(dateStr);

        StudyLoad load = new StudyLoad(userId, subjectName, difficulty, urgency, effort, entryDate);
        load.setEntryId(entryId);
        loadDAO.updateStudyLoad(load);
    }

    private void handleDeleteLoad(HttpServletRequest request, int userId) {
        int entryId = Integer.parseInt(request.getParameter("entryId"));
        loadDAO.deleteStudyLoad(entryId, userId);
    }

    private void recalculateAnalysis(int userId) {
        LocalDate today = LocalDate.now();
        List<StudyLoad> todayLoads = loadDAO.getTodayLoads(userId);
        LoadAnalysis analysis = LoadBalanceCalculator.calculateLoadAnalysis(userId, todayLoads, today);
        analysisDAO.saveAnalysis(analysis);

        GrowthMetric metric = GrowthAnalyzer.calculateDailyGrowth(userId, today);
        metricDAO.saveMetric(metric);
    }
}
