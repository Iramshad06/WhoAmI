// Commented out due to missing javax.mail dependency
/*
package com.whoami.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Date;

public class EmailService {
    
    // Email feature is now ENABLED with your Gmail credentials
    private static final boolean ENABLE_EMAIL = true;
    
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    
    // Gmail account configured
    private static final String SENDER_EMAIL = "iramshadofficial@gmail.com";
    
    // Gmail App Password (16-character code with spaces)
    private static final String SENDER_PASSWORD = "tdka mrfz kvjg xbtd";
    
    private static final String SENDER_NAME = "WhoAmI Platform";
    
    private static Session getMailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });
    }
    
    public static boolean sendWelcomeEmail(String recipientEmail, String userName) {
        if (!ENABLE_EMAIL) {
            System.out.println("Email feature disabled. Skipping welcome email to: " + recipientEmail);
            return true; // Return true to not break signup flow
        }
        
        try {
            Session session = getMailSession();
            Message message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(SENDER_EMAIL, SENDER_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Welcome to WhoAmI - Your Wellness Journey Begins");
            message.setSentDate(new Date());
            
            String htmlContent = buildWelcomeEmailHtml(userName);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            
            Transport.send(message);
            return true;
            
        } catch (Exception e) {
            System.err.println("Failed to send welcome email to " + recipientEmail + ": " + e.getMessage());
            return false;
        }
    }
    
    public static boolean sendOtpEmail(String recipientEmail, String otp) {
        if (!ENABLE_EMAIL) {
            System.out.println("\n========================================");
            System.out.println("EMAIL DISABLED - OTP CODE FOR: " + recipientEmail);
            System.out.println("========================================");
            System.out.println("COPY THIS OTP CODE: " + otp);
            System.out.println("========================================\n");
            return true; // Return true to not break OTP flow
        }
        
        try {
            Session session = getMailSession();
            Message message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(SENDER_EMAIL, SENDER_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Password Reset - Your Security Code");
            message.setSentDate(new Date());
            
            String htmlContent = buildOtpEmailHtml(otp);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            
            Transport.send(message);
            return true;
            
        } catch (Exception e) {
            System.err.println("Failed to send OTP email to " + recipientEmail + ": " + e.getMessage());
            return false;
        }
    }
    
    public static boolean sendPasswordChangeConfirmation(String recipientEmail, String userName) {
        if (!ENABLE_EMAIL) {
            System.out.println("Email feature disabled. Skipping password change confirmation to: " + recipientEmail);
            return true; // Return true to not break password reset flow
        }
        
        try {
            Session session = getMailSession();
            Message message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(SENDER_EMAIL, SENDER_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Password Successfully Changed");
            message.setSentDate(new Date());
            
            String htmlContent = buildPasswordChangeEmailHtml(userName);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            
            Transport.send(message);
            return true;
            
        } catch (Exception e) {
            System.err.println("Failed to send password change confirmation to " + recipientEmail + ": " + e.getMessage());
            return false;
        }
    }
    
    private static String buildWelcomeEmailHtml(String userName) {
        return "<!DOCTYPE html>" +
               "<html><head><meta charset='UTF-8'><style>" +
               "body{font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;line-height:1.6;color:#2d3748;margin:0;padding:0;background:#f7fafc}" +
               ".container{max-width:600px;margin:40px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 6px rgba(0,0,0,0.1)}" +
               ".header{background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);padding:40px 30px;text-align:center}" +
               ".header h1{color:#ffffff;margin:0;font-size:28px;font-weight:600}" +
               ".content{padding:40px 30px}" +
               ".content h2{color:#2d3748;font-size:22px;margin:0 0 20px;font-weight:600}" +
               ".content p{color:#4a5568;margin:0 0 16px;font-size:16px}" +
               ".highlight{background:#edf2f7;border-left:4px solid #667eea;padding:16px 20px;margin:24px 0;border-radius:4px}" +
               ".highlight p{margin:0;color:#2d3748;font-size:15px}" +
               ".footer{background:#f7fafc;padding:24px 30px;text-align:center;border-top:1px solid #e2e8f0}" +
               ".footer p{color:#718096;font-size:14px;margin:0}" +
               "</style></head><body>" +
               "<div class='container'>" +
               "<div class='header'><h1>Welcome to WhoAmI</h1></div>" +
               "<div class='content'>" +
               "<h2>Hello " + escapeHtml(userName) + ",</h2>" +
               "<p>We're thrilled to have you join WhoAmI, your personal wellness companion designed to help you balance your academic life, mental wellness, and personal growth.</p>" +
               "<div class='highlight'><p><strong>What you can do with WhoAmI:</strong><br>" +
               "• Track tasks and routines with intelligent load balancing<br>" +
               "• Monitor your mental wellness through daily check-ins<br>" +
               "• Visualize your personal growth over time<br>" +
               "• Get supportive insights tailored to your journey</p></div>" +
               "<p>Your wellness matters. We're here to support you every step of the way with a calm, judgment-free space that respects your privacy.</p>" +
               "<p>Ready to begin? Log in to your account and explore your dashboard.</p>" +
               "<p style='margin-top:32px;color:#4a5568;font-size:15px'>Stay well,<br><strong>The WhoAmI Team</strong></p>" +
               "</div>" +
               "<div class='footer'><p>This email was sent to you because you created an account on WhoAmI. Your privacy and security are our priority.</p></div>" +
               "</div></body></html>";
    }
    
    private static String buildOtpEmailHtml(String otp) {
        return "<!DOCTYPE html>" +
               "<html><head><meta charset='UTF-8'><style>" +
               "body{font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;line-height:1.6;color:#2d3748;margin:0;padding:0;background:#f7fafc}" +
               ".container{max-width:600px;margin:40px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 6px rgba(0,0,0,0.1)}" +
               ".header{background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);padding:40px 30px;text-align:center}" +
               ".header h1{color:#ffffff;margin:0;font-size:28px;font-weight:600}" +
               ".content{padding:40px 30px;text-align:center}" +
               ".content h2{color:#2d3748;font-size:22px;margin:0 0 20px;font-weight:600}" +
               ".content p{color:#4a5568;margin:0 0 16px;font-size:16px}" +
               ".otp-box{background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);color:#ffffff;font-size:36px;font-weight:700;letter-spacing:8px;padding:24px;border-radius:8px;margin:32px 0;font-family:monospace}" +
               ".warning{background:#fff5f5;border-left:4px solid #fc8181;padding:16px 20px;margin:24px 0;border-radius:4px;text-align:left}" +
               ".warning p{margin:0;color:#742a2a;font-size:14px}" +
               ".footer{background:#f7fafc;padding:24px 30px;text-align:center;border-top:1px solid #e2e8f0}" +
               ".footer p{color:#718096;font-size:14px;margin:0}" +
               "</style></head><body>" +
               "<div class='container'>" +
               "<div class='header'><h1>Password Reset Request</h1></div>" +
               "<div class='content'>" +
               "<h2>Your Security Code</h2>" +
               "<p>We received a request to reset your password. Use the code below to continue:</p>" +
               "<div class='otp-box'>" + escapeHtml(otp) + "</div>" +
               "<p style='color:#718096;font-size:14px'>This code will expire in 10 minutes.</p>" +
               "<div class='warning'><p><strong>Security Note:</strong> If you didn't request a password reset, please ignore this email. Your account remains secure.</p></div>" +
               "</div>" +
               "<div class='footer'><p>This is an automated security email from WhoAmI. Never share this code with anyone.</p></div>" +
               "</div></body></html>";
    }
    
    private static String buildPasswordChangeEmailHtml(String userName) {
        return "<!DOCTYPE html>" +
               "<html><head><meta charset='UTF-8'><style>" +
               "body{font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;line-height:1.6;color:#2d3748;margin:0;padding:0;background:#f7fafc}" +
               ".container{max-width:600px;margin:40px auto;background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 6px rgba(0,0,0,0.1)}" +
               ".header{background:linear-gradient(135deg,#48bb78 0%,#38a169 100%);padding:40px 30px;text-align:center}" +
               ".header h1{color:#ffffff;margin:0;font-size:28px;font-weight:600}" +
               ".content{padding:40px 30px}" +
               ".content h2{color:#2d3748;font-size:22px;margin:0 0 20px;font-weight:600}" +
               ".content p{color:#4a5568;margin:0 0 16px;font-size:16px}" +
               ".success{background:#f0fff4;border-left:4px solid #48bb78;padding:16px 20px;margin:24px 0;border-radius:4px}" +
               ".success p{margin:0;color:#22543d;font-size:15px}" +
               ".footer{background:#f7fafc;padding:24px 30px;text-align:center;border-top:1px solid #e2e8f0}" +
               ".footer p{color:#718096;font-size:14px;margin:0}" +
               "</style></head><body>" +
               "<div class='container'>" +
               "<div class='header'><h1>Password Changed Successfully</h1></div>" +
               "<div class='content'>" +
               "<h2>Hello " + escapeHtml(userName) + ",</h2>" +
               "<p>Your WhoAmI account password has been successfully changed.</p>" +
               "<div class='success'><p>Your account is now secured with your new password. You can log in immediately using your updated credentials.</p></div>" +
               "<p>If you did not make this change, please contact us immediately to secure your account.</p>" +
               "<p style='margin-top:32px;color:#4a5568;font-size:15px'>Stay secure,<br><strong>The WhoAmI Team</strong></p>" +
               "</div>" +
               "<div class='footer'><p>This is a security notification from WhoAmI. Your account safety is our priority.</p></div>" +
               "</div></body></html>";
    }
    
    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
*/
