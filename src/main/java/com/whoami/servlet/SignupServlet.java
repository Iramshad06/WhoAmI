package com.whoami.servlet;

import com.whoami.dao.UserDAO;
import com.whoami.dao.StreakDAO;
import com.whoami.dao.CategoryDAO;
import com.whoami.model.User;
// import com.whoami.util.EmailService; // Commented out - email disabled
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private UserDAO userDAO;
    private StreakDAO streakDAO;
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
        streakDAO = new StreakDAO();
        categoryDAO = new CategoryDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("dashboard");
            return;
        }
        request.getRequestDispatcher("signup.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }
        
        if (userDAO.emailExists(email)) {
            request.setAttribute("error", "Email already registered. Please login instead.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }
        
        User user = new User(email, password, fullName);
        boolean registered = userDAO.registerUser(user);
        
        if (registered) {
            streakDAO.initializeStreaks(user.getUserId());
            categoryDAO.initializeDefaultCategories(user.getUserId());
            
            try {
                // EmailService.sendWelcomeEmail(email, fullName); // Email disabled
            } catch (Exception e) {
                System.err.println("Failed to send welcome email: " + e.getMessage());
            }
            
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getFullName());
            session.setAttribute("isNewUser", true);
            session.setMaxInactiveInterval(30 * 60);
            
            Cookie themeCookie = new Cookie("theme", "light");
            themeCookie.setMaxAge(365 * 24 * 60 * 60);
            response.addCookie(themeCookie);
            
            response.sendRedirect("dashboard");
        } else {
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        }
    }
}
