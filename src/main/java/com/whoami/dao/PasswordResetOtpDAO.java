package com.whoami.dao;

import com.whoami.model.PasswordResetOtp;
import java.sql.*;
import java.util.Random;

public class PasswordResetOtpDAO {
    private Connection conn;
    
    public PasswordResetOtpDAO(Connection conn) {
        this.conn = conn;
    }
    
    public String generateAndStoreOtp(int userId) throws SQLException {
        invalidateExistingOtps(userId);
        
        String otpCode = generateSecureOtp();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiry = new Timestamp(now.getTime() + (10 * 60 * 1000));
        
        String sql = "INSERT INTO password_reset_otps (user_id, otp_code, created_at, expires_at, is_used) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, otpCode);
            stmt.setTimestamp(3, now);
            stmt.setTimestamp(4, expiry);
            stmt.setBoolean(5, false);
            stmt.executeUpdate();
        }
        
        return otpCode;
    }
    
    public PasswordResetOtp validateOtp(int userId, String otpCode) throws SQLException {
        String sql = "SELECT * FROM password_reset_otps WHERE user_id = ? AND otp_code = ? AND is_used = FALSE ORDER BY created_at DESC LIMIT 1";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, otpCode);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                PasswordResetOtp otp = new PasswordResetOtp();
                otp.setOtpId(rs.getInt("otp_id"));
                otp.setUserId(rs.getInt("user_id"));
                otp.setOtpCode(rs.getString("otp_code"));
                otp.setCreatedAt(rs.getTimestamp("created_at"));
                otp.setExpiresAt(rs.getTimestamp("expires_at"));
                otp.setUsed(rs.getBoolean("is_used"));
                
                return otp.isValid() ? otp : null;
            }
        }
        
        return null;
    }
    
    public void markOtpAsUsed(int otpId) throws SQLException {
        String sql = "UPDATE password_reset_otps SET is_used = TRUE WHERE otp_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, otpId);
            stmt.executeUpdate();
        }
    }
    
    public void invalidateExistingOtps(int userId) throws SQLException {
        String sql = "UPDATE password_reset_otps SET is_used = TRUE WHERE user_id = ? AND is_used = FALSE";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
    
    public void cleanupExpiredOtps() throws SQLException {
        String sql = "DELETE FROM password_reset_otps WHERE expires_at < ? OR is_used = TRUE";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis() - (24 * 60 * 60 * 1000)));
            stmt.executeUpdate();
        }
    }
    
    private String generateSecureOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
