package com.whoami.servlet;

import com.whoami.dao.PasswordResetOtpDAO;
import com.whoami.model.PasswordResetOtp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

@WebServlet("/verify-otp")
public class VerifyOtpServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:h2:./data/whoami;AUTO_SERVER=TRUE;MODE=MySQL";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static final long OTP_TIMEOUT_MS = 10 * 60 * 1000;
    
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
        
        if (session == null || session.getAttribute("resetEmail") == null) {
            response.sendRedirect("forgot-password");
            return;
        }
        
        Long otpTimestamp = (Long) session.getAttribute("otpTimestamp");
        if (otpTimestamp != null && (System.currentTimeMillis() - otpTimestamp > OTP_TIMEOUT_MS)) {
            session.removeAttribute("resetEmail");
            session.removeAttribute("resetUserId");
            session.removeAttribute("otpTimestamp");
            
            request.setAttribute("errorMessage", "Verification code expired. Please request a new one.");
            request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
            return;
        }
        
        request.getRequestDispatcher("/verify-otp.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("resetUserId") == null) {
            response.sendRedirect("forgot-password");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("resetUserId");
        String otpCode = request.getParameter("otp");
        
        if (otpCode == null || otpCode.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter the verification code");
            request.getRequestDispatcher("/verify-otp.jsp").forward(request, response);
            return;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            PasswordResetOtpDAO otpDAO = new PasswordResetOtpDAO(conn);
            PasswordResetOtp otp = otpDAO.validateOtp(userId, otpCode.trim());
            
            if (otp != null && otp.isValid()) {
                session.setAttribute("validatedOtpId", otp.getOtpId());
                response.sendRedirect("reset-password");
            } else {
                request.setAttribute("errorMessage", "Invalid or expired verification code. Please try again.");
                request.getRequestDispatcher("/verify-otp.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            request.getRequestDispatcher("/verify-otp.jsp").forward(request, response);
        }
    }
}
