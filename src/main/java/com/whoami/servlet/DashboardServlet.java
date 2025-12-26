package com.whoami.servlet;

import com.whoami.dao.*;
import com.whoami.model.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private MindLogDAO mindLogDAO;
    private StudySessionDAO studySessionDAO;
    private StreakDAO streakDAO;
    private ReflectionDAO reflectionDAO;
    
    @Override
    public void init() {
        mindLogDAO = new MindLogDAO();
        studySessionDAO = new StudySessionDAO();
        streakDAO = new StreakDAO();
        reflectionDAO = new ReflectionDAO();
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
        
        MindLog todayLog = mindLogDAO.getTodayLog(userId, today);
        StudySession todaySession = studySessionDAO.getTodaySession(userId, today);
        Reflection todayReflection = reflectionDAO.getTodayReflection(userId, today);
        List<Streak> streaks = streakDAO.getAllStreaks(userId);
        List<MindLog> recentLogs = mindLogDAO.getRecentLogs(userId, 7);
        
        request.setAttribute("todayLog", todayLog);
        request.setAttribute("todaySession", todaySession);
        request.setAttribute("todayReflection", todayReflection);
        request.setAttribute("streaks", streaks);
        request.setAttribute("recentLogs", recentLogs);
        
        Boolean isNewUser = (Boolean) session.getAttribute("isNewUser");
        if (isNewUser != null && isNewUser) {
            request.setAttribute("welcomeMessage", true);
            session.removeAttribute("isNewUser");
        }
        
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
