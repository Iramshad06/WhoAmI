<%@ page import="com.whoami.model.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Insights - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <% 
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        String userName = (String) session.getAttribute("userName");
        Map<String, Object> weeklyInsights = (Map<String, Object>) request.getAttribute("weeklyInsights");
        Map<String, Object> monthlyInsights = (Map<String, Object>) request.getAttribute("monthlyInsights");
        List<MindLog> weekLogs = (List<MindLog>) request.getAttribute("weekLogs");
    %>
    
    <jsp:include page="WEB-INF/navbar.jsp">
        <jsp:param name="activePage" value="insights" />
        <jsp:param name="activeSection" value="growth" />
        <jsp:param name="userName" value="<%= userName %>" />
    </jsp:include>
    
    <main class="main-content">
        <div class="container">
            <div class="page-header">
                <h1 class="page-title">Your Insights</h1>
                <p class="page-subtitle">Understand your patterns and optimize your productivity</p>
            </div>
            
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Weekly Overview</h3>
                    <p class="card-subtitle">Last 7 days</p>
                </div>
                
                <% if (weeklyInsights.containsKey("message")) { %>
                    <div class="empty-state">
                        <p><%= weeklyInsights.get("message") %></p>
                    </div>
                <% } else { %>
                    <div class="insight-narrative">
                        <%= weeklyInsights.get("narrative") %>
                    </div>
                    
                    <div class="grid grid-4">
                        <div class="stat-card">
                            <div class="stat-value" style="font-size: 36px; color: var(--info);"><%= weeklyInsights.get("avgFocus") %></div>
                            <div class="stat-label">Avg Focus</div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-value" style="font-size: 36px; color: var(--success);"><%= weeklyInsights.get("avgEnergy") %></div>
                            <div class="stat-label">Avg Energy</div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-value" style="font-size: 36px; color: var(--warning);"><%= weeklyInsights.get("avgStress") %></div>
                            <div class="stat-label">Avg Stress</div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-value" style="font-size: 36px; color: var(--primary);"><%= weeklyInsights.get("totalCheckins") %></div>
                            <div class="stat-label">Check-ins</div>
                        </div>
                    </div>
                <% } %>
            </div>
            
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Monthly Trends</h3>
                    <p class="card-subtitle">Last 30 days</p>
                </div>
                
                <% if (monthlyInsights.containsKey("message")) { %>
                    <div class="empty-state">
                        <p><%= monthlyInsights.get("message") %></p>
                    </div>
                <% } else { %>
                    <div class="insight-narrative">
                        <%= monthlyInsights.get("narrative") %>
                    </div>
                    
                    <div>
                        <div class="metric-row">
                            <span class="metric-label">Average Mind Score</span>
                            <span class="metric-value" style="color: var(--primary);"><%= monthlyInsights.get("avgScore") %>/10</span>
                        </div>
                        <div class="metric-row">
                            <span class="metric-label">Average Focus Level</span>
                            <span class="metric-value"><%= monthlyInsights.get("avgFocus") %>/10</span>
                        </div>
                        <div class="metric-row">
                            <span class="metric-label">Average Energy Level</span>
                            <span class="metric-value"><%= monthlyInsights.get("avgEnergy") %>/10</span>
                        </div>
                        <div class="metric-row">
                            <span class="metric-label">Average Stress Level</span>
                            <span class="metric-value"><%= monthlyInsights.get("avgStress") %>/10</span>
                        </div>
                        <div class="metric-row">
                            <span class="metric-label">Your Best Day</span>
                            <span class="metric-value" style="color: var(--success);"><%= monthlyInsights.get("bestDay") %></span>
                        </div>
                        <div class="metric-row">
                            <span class="metric-label">Total Check-ins</span>
                            <span class="metric-value"><%= monthlyInsights.get("totalCheckins") %> days</span>
                        </div>
                    </div>
                <% } %>
            </div>
            
            <% if (weekLogs != null && !weekLogs.isEmpty()) { %>
                <div class="card">
                    <div class="card-header">
                        <h3 class="card-title">Recent Check-ins</h3>
                        <p class="card-subtitle">Your latest mental state logs</p>
                    </div>
                    
                    <% for (MindLog log : weekLogs) { %>
                        <div style="padding: 16px; border-bottom: 1px solid var(--border-color); display: flex; justify-content: space-between; align-items: center;">
                            <div>
                                <div style="font-weight: 600; color: var(--text-primary); margin-bottom: 4px;">
                                    <%= log.getLogDate() %>
                                </div>
                                <div style="font-size: 14px; color: var(--text-secondary);">
                                    <%= log.getMindState() %>
                                </div>
                            </div>
                            <div style="text-align: right;">
                                <div style="font-size: 24px; font-weight: 700; color: var(--primary);">
                                    <%= String.format("%.1f", log.getMindScore()) %>
                                </div>
                                <div style="font-size: 12px; color: var(--text-tertiary);">
                                    /10
                                </div>
                            </div>
                        </div>
                    <% } %>
                </div>
            <% } %>
        </div>
    </main>
    
    <script src="js/theme.js"></script>
</body>
</html>
