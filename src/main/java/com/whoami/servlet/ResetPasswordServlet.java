package com.whoami.servlet;

import com.whoami.dao.UserDAO;
import com.whoami.dao.PasswordResetOtpDAO;
import com.whoami.model.User;
// import com.whoami.util.EmailService; // Commented out - email disabled

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    
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
        
        if (session == null || session.getAttribute("validatedOtpId") == null) {
            response.sendRedirect("forgot-password");
            return;
        }
        
        request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("validatedOtpId") == null) {
            response.sendRedirect("forgot-password");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("resetUserId");
        Integer otpId = (Integer) session.getAttribute("validatedOtpId");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Password cannot be empty");
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (newPassword.length() < 6) {
            request.setAttribute("errorMessage", "Password must be at least 6 characters long");
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
            return;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            UserDAO userDAO = new UserDAO(conn);
            PasswordResetOtpDAO otpDAO = new PasswordResetOtpDAO(conn);
            
            String passwordHash = hashPassword(newPassword);
            boolean updated = userDAO.updatePassword(userId, passwordHash);
            
            if (updated) {
                otpDAO.markOtpAsUsed(otpId);
                
                User user = userDAO.getUserById(userId);
                if (user != null) {
                    // EmailService.sendPasswordChangeConfirmation(user.getEmail(), user.getFullName()); // Email disabled
                }
                
                session.removeAttribute("resetEmail");
                session.removeAttribute("resetUserId");
                session.removeAttribute("otpTimestamp");
                session.removeAttribute("validatedOtpId");
                
                request.setAttribute("successMessage", "Password reset successful! You can now log in with your new password.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Failed to update password. Please try again.");
                request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
        }
    }
    
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        
        return hexString.toString();
    }
}
