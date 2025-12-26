package com.whoami.servlet;

import com.whoami.dao.UserDAO;
// import com.whoami.dao.PasswordResetOtpDAO; // Not used - email disabled
import com.whoami.model.User;
// import com.whoami.util.EmailService; // Commented out - email disabled

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:h2:./data/whoami;AUTO_SERVER=TRUE;MODE=MySQL";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    @Override
    public void init() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 Driver not found", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            response.sendRedirect("dashboard");
            return;
        }
        
        request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter your email address");
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            UserDAO userDAO = new UserDAO(conn);
            User user = userDAO.getUserByEmail(email.trim());
            
            if (user != null) {
                // PasswordResetOtpDAO otpDAO = new PasswordResetOtpDAO(conn);
                // String otpCode = otpDAO.generateAndStoreOtp(user.getUserId()); // OTP generated but email disabled
                
                // boolean emailSent = EmailService.sendOtpEmail(email.trim(), otpCode);
                boolean emailSent = true; // Email disabled, assume sent
                
                if (emailSent) {
                    HttpSession session = request.getSession();
                    session.setAttribute("resetEmail", email.trim());
                    session.setAttribute("resetUserId", user.getUserId());
                    session.setAttribute("otpTimestamp", System.currentTimeMillis());
                    
                    response.sendRedirect("verify-otp");
                } else {
                    request.setAttribute("errorMessage", "Unable to send verification code. Please try again.");
                    request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
                }
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                request.setAttribute("successMessage", "If an account exists with this email, you will receive a verification code shortly.");
                request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again later.");
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
        }
    }
}
