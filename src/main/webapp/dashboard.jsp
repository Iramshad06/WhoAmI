<%@ page import="com.whoami.model.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <% 
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        String userName = (String) session.getAttribute("userName");
        MindLog todayLog = (MindLog) request.getAttribute("todayLog");
        StudySession todaySession = (StudySession) request.getAttribute("todaySession");
        Reflection todayReflection = (Reflection) request.getAttribute("todayReflection");
        List<Streak> streaks = (List<Streak>) request.getAttribute("streaks");
        Boolean welcomeMessage = (Boolean) request.getAttribute("welcomeMessage");
    %>
    
    <jsp:include page="WEB-INF/navbar.jsp">
        <jsp:param name="activePage" value="dashboard" />
        <jsp:param name="activeSection" value="" />
        <jsp:param name="userName" value="<%= userName %>" />
    </jsp:include>
    
    <main class="main-content">
        <div class="container">
            <% if (welcomeMessage != null && welcomeMessage) { %>
                <div class="welcome-banner fade-in">
                    <h2>Welcome to WhoAmI, <%= userName %>!</h2>
                    <p>Let's start by checking in with your mind. Understanding yourself is the first step to growth.</p>
                </div>
            <% } %>
            
            <div class="page-header">
                <h1 class="page-title">Your Dashboard</h1>
                <p class="page-subtitle">Track your mental state, study progress, and personal growth</p>
            </div>
            
            <div class="grid grid-3">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Today's Check-In</h3>
                        <p class="card-subtitle">Mental state assessment</p>
                    </div>
                    <% if (todayLog != null) { %>
                        <div class="stat-card">
                            <div class="stat-value" style="font-size: 32px;"><%= todayLog.getMindState() %></div>
                            <div class="stat-label">Score: <%= String.format("%.1f", todayLog.getMindScore()) %>/10</div>
                        </div>
                        <a href="mind-checkin" class="btn btn-secondary" style="width: 100%; margin-top: 16px;">Update Check-In</a>
                    <% } else { %>
                        <div class="empty-state" style="padding: 32px 16px;">
                            <div class="empty-state-icon">MIND</div>
                            <p>No check-in yet today</p>
                            <a href="mind-checkin" class="btn btn-primary" style="margin-top: 16px;">Start Check-In</a>
                        </div>
                    <% } %>
                </div>
                
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Study Recommendation</h3>
                        <p class="card-subtitle">Based on your mind state</p>
                    </div>
                    <% if (todaySession != null) { %>
                        <div style="padding: 16px 0;">
                            <p style="color: var(--text-secondary); font-size: 14px; margin-bottom: 8px;">Activity</p>
                            <p style="font-weight: 600; margin-bottom: 16px;"><%= todaySession.getSuggestedActivity() %></p>
                            <p style="color: var(--text-secondary); font-size: 14px; margin-bottom: 8px;">Intensity</p>
                            <p style="font-weight: 600; color: var(--primary);"><%= todaySession.getSuggestedIntensity() %></p>
                        </div>
                        <a href="study-zone" class="btn btn-primary" style="width: 100%; margin-top: 16px;">View Details</a>
                    <% } else { %>
                        <div class="empty-state" style="padding: 32px 16px;">
                            <div class="empty-state-icon">STUDY</div>
                            <p>Complete check-in first</p>
                        </div>
                    <% } %>
                </div>
                
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Daily Reflection</h3>
                        <p class="card-subtitle">Journal your thoughts</p>
                    </div>
                    <% if (todayReflection != null) { %>
                        <div class="stat-card">
                            <div style="font-size: 48px; margin-bottom: 16px; color: var(--success); font-weight: bold;">✓</div>
                            <div class="stat-label">Completed</div>
                        </div>
                        <a href="reflection" class="btn btn-secondary" style="width: 100%; margin-top: 16px;">View Reflection</a>
                    <% } else { %>
                        <div class="empty-state" style="padding: 32px 16px;">
                            <div class="empty-state-icon">NOTES</div>
                            <p>Not reflected yet</p>
                            <a href="reflection" class="btn btn-primary" style="margin-top: 16px;">Reflect Now</a>
                        </div>
                    <% } %>
                </div>
            </div>
            
            <div class="card" style="margin-top: 24px;">
                <div class="card-header">
                    <h3 class="card-title">Your Streaks</h3>
                    <p class="card-subtitle">Keep the momentum going</p>
                </div>
                <% if (streaks != null && !streaks.isEmpty()) { %>
                    <% for (Streak streak : streaks) { %>
                        <div class="streak-display">
                            <div class="streak-info">
                                <div class="streak-icon"><%= streak.getStreakType().equals("mind_checkin") ? "M" : "S" %></div>
                                <div class="streak-details">
                                    <h3><%= streak.getStreakType().equals("mind_checkin") ? "Mind Check-In Streak" : "Study Follow-Through Streak" %></h3>
                                    <p>Keep it going every day</p>
                                </div>
                            </div>
                            <div class="streak-count">
                                <div class="streak-current"><%= streak.getCurrentCount() %></div>
                                <div class="streak-best">Best: <%= streak.getBestCount() %> days</div>
                            </div>
                        </div>
                    <% } %>
                <% } else { %>
                    <div class="empty-state">
                        <p>Start checking in daily to build streaks</p>
                    </div>
                <% } %>
            </div>
        </div>
    </main>
    
    <script src="js/theme.js"></script>
</body>
</html>
