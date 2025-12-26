<%@ page import="com.whoami.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Personal Growth Timeline - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <% 
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        String userName = (String) session.getAttribute("userName");
        List<GrowthMetric> metrics = (List<GrowthMetric>) request.getAttribute("metrics");
        String insightMessage = (String) request.getAttribute("insightMessage");
        String trendAnalysis = (String) request.getAttribute("trendAnalysis");
        String avgGrowth = (String) request.getAttribute("avgGrowth");
        String currentView = (String) request.getAttribute("currentView");
        String todayDate = (String) request.getAttribute("todayDate");
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd");
        DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    %>
    
    <jsp:include page="WEB-INF/navbar.jsp">
        <jsp:param name="activePage" value="growth-timeline" />
        <jsp:param name="activeSection" value="growth" />
        <jsp:param name="userName" value="<%= userName %>" />
    </jsp:include>

    <div class="container" style="padding-top: 24px;">
        <div class="page-header" style="margin-bottom: 32px;">
            <div>
                <h1 style="margin: 0 0 8px 0; font-size: 32px;">Personal Growth Timeline</h1>
                <p style="margin: 0; color: var(--text-secondary); font-size: 16px;">Track your journey toward consistent improvement</p>
            </div>
        </div>

        <div class="view-tabs" style="margin-bottom: 24px;">
            <a href="growth-timeline?view=week" class="view-tab <%= "week".equals(currentView) ? "active" : "" %>">7 Days</a>
            <a href="growth-timeline?view=month" class="view-tab <%= "month".equals(currentView) ? "active" : "" %>">30 Days</a>
        </div>

        <div class="growth-summary-card">
            <div class="summary-header">
                <h2 style="margin: 0; font-size: 20px;">Growth Summary</h2>
                <span class="trend-badge <%= trendAnalysis.toLowerCase() %>"><%= trendAnalysis %></span>
            </div>

            <div class="summary-stats">
                <div class="summary-stat">
                    <div class="stat-label">Average Growth</div>
                    <div class="stat-value-large"><%= avgGrowth %>%</div>
                </div>
                <div class="summary-stat">
                    <div class="stat-label">Days Tracked</div>
                    <div class="stat-value-large"><%= metrics != null ? metrics.size() : 0 %></div>
                </div>
            </div>

            <div class="growth-insight">
                <svg class="insight-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                    <polyline points="22 4 12 14.01 9 11.01"></polyline>
                </svg>
                <p style="margin: 0;"><%= insightMessage %></p>
            </div>
        </div>

        <div class="timeline-section">
            <h2 style="margin: 0 0 24px 0; font-size: 20px;">Your Progress Timeline</h2>
            
            <% if (metrics != null && !metrics.isEmpty()) { %>
            <div class="timeline">
                <% for (int i = 0; i < metrics.size(); i++) { 
                    GrowthMetric metric = metrics.get(i);
                    boolean isLast = (i == metrics.size() - 1);
                %>
                <div class="timeline-item <%= isLast ? "timeline-item-latest" : "" %>">
                    <div class="timeline-marker">
                        <div class="timeline-dot <%= metric.getGrowthTrend().toLowerCase() %>"></div>
                        <% if (!isLast) { %>
                        <div class="timeline-line"></div>
                        <% } %>
                    </div>
                    
                    <div class="timeline-content">
                        <div class="timeline-header">
                            <span class="timeline-date"><%= metric.getMetricDate().format(fullDateFormatter) %></span>
                            <span class="growth-score <%= metric.getGrowthTrend().toLowerCase() %>">
                                <%= String.format("%.1f", metric.getOverallGrowth()) %>%
                            </span>
                        </div>

                        <div class="metric-grid">
                            <div class="metric-item">
                                <div class="metric-header">
                                    <svg class="metric-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <circle cx="12" cy="12" r="10"></circle>
                                        <line x1="12" y1="16" x2="12" y2="12"></line>
                                        <line x1="12" y1="8" x2="12.01" y2="8"></line>
                                    </svg>
                                    <span class="metric-label">Mind</span>
                                </div>
                                <div class="metric-bar">
                                    <div class="metric-bar-fill" style="width: <%= metric.getMindScore() %>%;"></div>
                                </div>
                                <span class="metric-value"><%= String.format("%.0f", metric.getMindScore()) %>%</span>
                            </div>

                            <div class="metric-item">
                                <div class="metric-header">
                                    <svg class="metric-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline>
                                    </svg>
                                    <span class="metric-label">Tasks</span>
                                </div>
                                <div class="metric-bar">
                                    <div class="metric-bar-fill" style="width: <%= metric.getTaskEfficiency() %>%;"></div>
                                </div>
                                <span class="metric-value"><%= String.format("%.0f", metric.getTaskEfficiency()) %>%</span>
                            </div>

                            <div class="metric-item">
                                <div class="metric-header">
                                    <svg class="metric-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"></path>
                                        <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"></path>
                                    </svg>
                                    <span class="metric-label">Study</span>
                                </div>
                                <div class="metric-bar">
                                    <div class="metric-bar-fill" style="width: <%= metric.getStudyConsistency() %>%;"></div>
                                </div>
                                <span class="metric-value"><%= String.format("%.0f", metric.getStudyConsistency()) %>%</span>
                            </div>

                            <div class="metric-item">
                                <div class="metric-header">
                                    <svg class="metric-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                        <path d="M12 2v20M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
                                    </svg>
                                    <span class="metric-label">Balance</span>
                                </div>
                                <div class="metric-bar">
                                    <div class="metric-bar-fill" style="width: <%= metric.getLoadBalance() %>%;"></div>
                                </div>
                                <span class="metric-value"><%= String.format("%.0f", metric.getLoadBalance()) %>%</span>
                            </div>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
            <% } else { %>
            <div class="empty-state">
                <svg class="empty-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
                    <polyline points="9 22 9 12 15 12 15 22"></polyline>
                </svg>
                <p style="margin: 16px 0 0 0; color: var(--text-secondary);">
                    Your growth journey begins now. Start tracking your mind, tasks, and study habits to see your progress over time.
                </p>
            </div>
            <% } %>
        </div>
    </div>

    <script src="js/theme.js"></script>
</body>
</html>
