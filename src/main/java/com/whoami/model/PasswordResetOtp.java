package com.whoami.model;

import java.sql.Timestamp;

public class PasswordResetOtp {
    private int otpId;
    private int userId;
    private String otpCode;
    private Timestamp createdAt;
    private Timestamp expiresAt;
    private boolean isUsed;
    
    public PasswordResetOtp() {}
    
    public PasswordResetOtp(int userId, String otpCode, Timestamp createdAt, Timestamp expiresAt) {
        this.userId = userId;
        this.otpCode = otpCode;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.isUsed = false;
    }
    
    public int getOtpId() {
        return otpId;
    }
    
    public void setOtpId(int otpId) {
        this.otpId = otpId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getOtpCode() {
        return otpCode;
    }
    
    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public boolean isUsed() {
        return isUsed;
    }
    
    public void setUsed(boolean used) {
        isUsed = used;
    }
    
    public boolean isExpired() {
        return new Timestamp(System.currentTimeMillis()).after(expiresAt);
    }
    
    public boolean isValid() {
        return !isUsed && !isExpired();
    }
}
