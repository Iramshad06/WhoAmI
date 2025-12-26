package com.whoami.servlet;

import com.whoami.dao.*;
import com.whoami.model.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet("/study-zone")
public class StudyZoneServlet extends HttpServlet {
    private StudySessionDAO studySessionDAO;
    private StreakDAO streakDAO;
    
    @Override
    public void init() {
        studySessionDAO = new StudySessionDAO();
        streakDAO = new StreakDAO();
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
        Date today = Date.valueOf(LocalDate.now());
        
        StudySession todaySession = studySessionDAO.getTodaySession(userId, today);
        
        if (todaySession == null) {
            response.sendRedirect("mind-checkin");
            return;
        }
        
        request.setAttribute("todaySession", todaySession);
        request.getRequestDispatcher("study-zone.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        int sessionId = Integer.parseInt(request.getParameter("sessionId"));
        boolean followed = Boolean.parseBoolean(request.getParameter("followed"));
        Date today = Date.valueOf(LocalDate.now());
        
        boolean updated = studySessionDAO.markAsFollowed(sessionId, followed);
        
        if (updated && followed) {
            streakDAO.updateStreak(userId, "study_followed", today);
        }
        
        response.sendRedirect("dashboard");
    }
}
