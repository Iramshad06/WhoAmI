package com.whoami.servlet;

import com.whoami.dao.UserDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/theme")
public class ThemeServlet extends HttpServlet {
    private UserDAO userDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        String theme = request.getParameter("theme");
        
        if (theme == null || (!theme.equals("light") && !theme.equals("dark"))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        boolean updated = userDAO.updateThemePreference(userId, theme);
        
        if (updated) {
            Cookie themeCookie = new Cookie("theme", theme);
            themeCookie.setMaxAge(365 * 24 * 60 * 60);
            themeCookie.setPath("/");
            response.addCookie(themeCookie);
            
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\": true}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
