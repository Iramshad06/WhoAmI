<%@ page import="com.whoami.model.*" %>
<%@ page import="com.whoami.util.MindScoreCalculator" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Study Zone - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <% 
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        String userName = (String) session.getAttribute("userName");
        StudySession todaySession = (StudySession) request.getAttribute("todaySession");
    %>
    
    <jsp:include page="WEB-INF/navbar.jsp">
        <jsp:param name="activePage" value="study-zone" />
        <jsp:param name="activeSection" value="mind" />
        <jsp:param name="userName" value="<%= userName %>" />
    </jsp:include>
    
    <main class="main-content">
        <div class="container">
            <div class="page-header">
                <h1 class="page-title">Study Zone</h1>
                <p class="page-subtitle">Personalized study recommendations based on your mind state</p>
            </div>
            
            <div style="max-width: 800px; margin: 0 auto;">
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Today's Recommendation</h3>
                        <p class="card-subtitle">Based on your latest check-in</p>
                    </div>
                    
                    <div class="suggestion-card">
                        <div class="suggestion-title">Suggested Activity</div>
                        <div class="suggestion-text"><%= todaySession.getSuggestedActivity() %></div>
                    </div>
                    
                    <div class="suggestion-card">
                        <div class="suggestion-title">Recommended Intensity</div>
                        <div class="suggestion-text"><%= todaySession.getSuggestedIntensity() %></div>
                    </div>
                </div>
                
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Did You Follow This?</h3>
                        <p class="card-subtitle">Track your commitment to build streaks</p>
                    </div>
                    
                    <% if (todaySession.isFollowed()) { %>
                        <div class="alert alert-success">
                            Great job! You followed today's recommendation. Keep it up!
                        </div>
                        <a href="dashboard" class="btn btn-primary" style="width: 100%;">Back to Dashboard</a>
                    <% } else { %>
                        <form method="post" action="study-zone">
                            <input type="hidden" name="sessionId" value="<%= todaySession.getSessionId() %>">
                            
                            <div class="action-buttons">
                                <button type="submit" name="followed" value="true" class="btn btn-success" style="flex: 1;">
                                    ✓ Yes, I Followed It
                                </button>
                                <button type="submit" name="followed" value="false" class="btn btn-secondary" style="flex: 1;">
                                    Not Today
                                </button>
                            </div>
                        </form>
                    <% } %>
                </div>
                
                <div class="card" style="background: var(--bg-secondary);">
                    <h4 style="font-size: 16px; font-weight: 600; margin-bottom: 12px; color: var(--text-primary);">Pro Tips</h4>
                    <ul style="list-style: none; padding: 0;">
                        <li style="padding: 8px 0; color: var(--text-secondary);">• Take 5-minute breaks every 25-30 minutes</li>
                        <li style="padding: 8px 0; color: var(--text-secondary);">• Hydrate regularly to maintain focus</li>
                        <li style="padding: 8px 0; color: var(--text-secondary);">• Study in a well-lit, quiet environment</li>
                        <li style="padding: 8px 0; color: var(--text-secondary);">• Review difficult concepts before sleep</li>
                    </ul>
                </div>
            </div>
        </div>
    </main>
    
    <script src="js/theme.js"></script>
</body>
</html>
