<%@ page import="com.whoami.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Study Load Balancer - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <% 
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        String userName = (String) session.getAttribute("userName");
        List<StudyLoad> loads = (List<StudyLoad>) request.getAttribute("loads");
        LoadAnalysis analysis = (LoadAnalysis) request.getAttribute("analysis");
        List<LoadAnalysis> weekAnalyses = (List<LoadAnalysis>) request.getAttribute("weekAnalyses");
        String currentView = (String) request.getAttribute("currentView");
        String todayDate = (String) request.getAttribute("todayDate");
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd");
    %>
    
    <jsp:include page="WEB-INF/navbar.jsp">
        <jsp:param name="activePage" value="load-balancer" />
        <jsp:param name="activeSection" value="tasks" />
        <jsp:param name="userName" value="<%= userName %>" />
    </jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="load-balancer-container">
                <!-- Hero Section -->
                <div class="load-balancer-hero">
                    <div class="hero-content">
                        <div class="hero-text">
                            <h1 class="hero-title">Study Load Balancer</h1>
                            <p class="hero-subtitle">Optimize your academic workload with intelligent distribution and insights</p>
                        </div>
                        <div class="hero-actions">
                            <button onclick="openAddLoadModal()" class="btn-primary-large">
                                <svg class="btn-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M12 5v14M5 12h14"></path>
                                </svg>
                                Add Subject
                            </button>
                        </div>
                    </div>
                    <div class="hero-visual">
                        <div class="load-distribution-chart">
                            <div class="chart-placeholder">
                                <svg viewBox="0 0 120 120" class="chart-icon">
                                    <circle cx="60" cy="60" r="40" fill="none" stroke="currentColor" stroke-width="2" opacity="0.2"></circle>
                                    <circle cx="60" cy="60" r="25" fill="none" stroke="currentColor" stroke-width="3" opacity="0.4"></circle>
                                    <circle cx="60" cy="60" r="15" fill="currentColor" opacity="0.6"></circle>
                                </svg>
                                <p class="chart-label">Load Distribution</p>
                            </div>
                        </div>
                    </div>
                </div>

        <!-- View Tabs -->
        <div class="view-tabs">
            <a href="load-balancer?view=today" class="view-tab <%= "today".equals(currentView) ? "active" : "" %>">
                <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="12" cy="12" r="10"></circle>
                    <polyline points="12,6 12,12 16,14"></polyline>
                </svg>
                Today
            </a>
            <a href="load-balancer?view=week" class="view-tab <%= "week".equals(currentView) ? "active" : "" %>">
                <svg class="tab-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                    <line x1="16" y1="2" x2="16" y2="6"></line>
                    <line x1="8" y1="2" x2="8" y2="6"></line>
                    <line x1="3" y1="10" x2="21" y2="10"></line>
                </svg>
                This Week
            </a>
        </div>

        <% if ("today".equals(currentView) && analysis != null) { %>
        <!-- Load Analysis Card -->
        <div class="analysis-card">
            <div class="analysis-card-header">
                <div class="analysis-title-section">
                    <h2 class="analysis-title"><%= todayDate %></h2>
                    <div class="analysis-status">
                        <div class="status-indicator <%= analysis.getLoadCategory().toLowerCase() %>"></div>
                        <span class="status-text"><%= analysis.getLoadCategory() %></span>
                    </div>
                </div>
                <div class="analysis-metrics">
                    <div class="metric-item">
                        <div class="metric-value"><%= analysis.getTotalEntries() %></div>
                        <div class="metric-label">Subjects</div>
                    </div>
                    <div class="metric-item">
                        <div class="metric-value"><%= analysis.getLoadScore() %></div>
                        <div class="metric-label">Load Score</div>
                    </div>
                </div>
            </div>

            <div class="analysis-progress-section">
                <div class="progress-container">
                    <div class="progress-bar">
                        <div class="progress-fill <%= analysis.getLoadCategory().toLowerCase() %>"
                             style="width: <%= Math.min(analysis.getLoadScore(), 100) %>%;"></div>
                    </div>
                    <div class="progress-labels">
                        <span>Light</span>
                        <span>Balanced</span>
                        <span>Heavy</span>
                        <span>Overloaded</span>
                    </div>
                </div>
            </div>

            <div class="analysis-insight-section">
                <div class="insight-content">
                    <svg class="insight-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <circle cx="12" cy="12" r="10"></circle>
                        <line x1="12" y1="16" x2="12" y2="12"></line>
                        <line x1="12" y1="8" x2="12.01" y2="8"></line>
                    </svg>
                    <p class="insight-text"><%= analysis.getInsightMessage() %></p>
                </div>
            </div>
        </div>
        <% } %>

        <% if ("week".equals(currentView) && weekAnalyses != null && !weekAnalyses.isEmpty()) { %>
        <!-- Weekly Overview -->
        <div class="weekly-overview-section">
            <div class="section-header">
                <h2 class="section-title">Weekly Load Distribution</h2>
                <p class="section-subtitle">Track your study load across the week</p>
            </div>

            <div class="week-grid">
                <% for (LoadAnalysis weekAnalysis : weekAnalyses) { %>
                <div class="week-day-card">
                    <div class="week-day-header">
                        <div class="week-day-date"><%= weekAnalysis.getAnalysisDate().format(dateFormatter) %></div>
                        <div class="week-day-status">
                            <div class="status-dot <%= weekAnalysis.getLoadCategory().toLowerCase() %>"></div>
                            <span class="status-label"><%= weekAnalysis.getLoadCategory() %></span>
                        </div>
                    </div>

                    <div class="week-day-metrics">
                        <div class="metric">
                            <div class="metric-number"><%= weekAnalysis.getTotalEntries() %></div>
                            <div class="metric-label">Subjects</div>
                        </div>
                        <div class="metric">
                            <div class="metric-number"><%= weekAnalysis.getLoadScore() %></div>
                            <div class="metric-label">Score</div>
                        </div>
                    </div>

                    <div class="week-day-progress">
                        <div class="progress-track">
                            <div class="progress-fill <%= weekAnalysis.getLoadCategory().toLowerCase() %>"
                                 style="width: <%= Math.min(weekAnalysis.getLoadScore(), 100) %>%;"></div>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
        </div>
        <% } %>

        <!-- Load Entries Section -->
        <div class="load-entries-section">
            <div class="section-header">
                <h2 class="section-title">
                    <%= "week".equals(currentView) ? "All Subjects" : "Today's Subjects" %>
                </h2>
                <p class="section-subtitle">
                    <%= "week".equals(currentView) ? "Manage all your study subjects" : "Your study subjects for today" %>
                </p>
            </div>

            <% if (loads != null && !loads.isEmpty()) { %>
            <div class="load-entries-grid">
                <% for (StudyLoad load : loads) { %>
                <div class="load-entry-card">
                    <div class="load-entry-header">
                        <div class="subject-info">
                            <h3 class="subject-name"><%= load.getSubjectName() %></h3>
                            <% if ("week".equals(currentView)) { %>
                            <div class="subject-date"><%= load.getEntryDate().format(dateFormatter) %></div>
                            <% } %>
                        </div>
                        <div class="entry-actions">
                            <button class="action-btn edit-btn" aria-label="Edit"
                                    data-entry-id="<%= load.getEntryId() %>"
                                    data-subject="<%= load.getSubjectName().replace("\"", "&quot;") %>"
                                    data-difficulty="<%= load.getDifficulty() %>"
                                    data-urgency="<%= load.getUrgency() %>"
                                    data-effort="<%= load.getEffort() %>"
                                    data-date="<%= load.getEntryDate() %>">
                                <svg class="action-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                                </svg>
                            </button>
                            <button class="action-btn delete-btn" aria-label="Delete"
                                    data-entry-id="<%= load.getEntryId() %>">
                                <svg class="action-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                    <polyline points="3 6 5 6 21 6"></polyline>
                                    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                                </svg>
                            </button>
                        </div>
                    </div>

                    <div class="load-entry-attributes">
                        <div class="attribute-item">
                            <span class="attribute-label">Difficulty</span>
                            <span class="attribute-value difficulty-<%= load.getDifficulty().toLowerCase() %>">
                                <%= load.getDifficulty() %>
                            </span>
                        </div>
                        <div class="attribute-item">
                            <span class="attribute-label">Urgency</span>
                            <span class="attribute-value urgency-<%= load.getUrgency().toLowerCase() %>">
                                <%= load.getUrgency() %>
                            </span>
                        </div>
                        <div class="attribute-item">
                            <span class="attribute-label">Effort</span>
                            <span class="attribute-value effort-<%= load.getEffort().toLowerCase() %>">
                                <%= load.getEffort() %>
                            </span>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
            <% } else { %>
            <!-- Empty State -->
            <div class="empty-state">
                <div class="empty-state-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                        <line x1="16" y1="2" x2="16" y2="6"></line>
                        <line x1="8" y1="2" x2="8" y2="6"></line>
                        <line x1="3" y1="10" x2="21" y2="10"></line>
                    </svg>
                </div>
                <h3 class="empty-state-title">No subjects yet</h3>
                <p class="empty-state-description">
                    Add your first study subject to start balancing your academic workload intelligently.
                </p>
                <button onclick="openAddLoadModal()" class="btn-primary">
                    <svg class="btn-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M12 5v14M5 12h14"></path>
                    </svg>
                    Add Subject
                </button>
            </div>
            <% } %>
        </div>
    </div>
    </main>

    <!-- Add/Edit Modal -->
    <div id="loadModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <h2 id="modalTitle" class="modal-title">Add Subject</h2>
                <button class="modal-close-btn" onclick="closeLoadModal()" aria-label="Close">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <line x1="18" y1="6" x2="6" y2="18"></line>
                        <line x1="6" y1="6" x2="18" y2="18"></line>
                    </svg>
                </button>
            </div>

            <form id="loadForm" method="post" action="load-balancer" class="modal-form">
                <input type="hidden" name="action" id="formAction" value="add">
                <input type="hidden" name="entryId" id="entryId">

                <div class="form-section">
                    <div class="form-field">
                        <label for="subjectName" class="form-field-label">Subject Name</label>
                        <input type="text" id="subjectName" name="subjectName" class="form-field-input"
                               placeholder="e.g., Mathematics, Physics, Chemistry" required maxlength="200">
                    </div>
                </div>

                <div class="form-section">
                    <h3 class="form-section-title">Subject Characteristics</h3>
                    <div class="form-grid">
                        <div class="form-field">
                            <label for="difficulty" class="form-field-label">Difficulty Level</label>
                            <select id="difficulty" name="difficulty" class="form-field-select" required>
                                <option value="">Select difficulty</option>
                                <option value="Easy">Easy</option>
                                <option value="Medium">Medium</option>
                                <option value="Hard">Hard</option>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="urgency" class="form-field-label">Urgency Level</label>
                            <select id="urgency" name="urgency" class="form-field-select" required>
                                <option value="">Select urgency</option>
                                <option value="Low">Low</option>
                                <option value="Medium">Medium</option>
                                <option value="High">High</option>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="effort" class="form-field-label">Effort Required</label>
                            <select id="effort" name="effort" class="form-field-select" required>
                                <option value="">Select effort</option>
                                <option value="Light">Light</option>
                                <option value="Moderate">Moderate</option>
                                <option value="Heavy">Heavy</option>
                            </select>
                        </div>

                        <div class="form-field">
                            <label for="entryDate" class="form-field-label">Study Date <span class="optional">(Optional)</span></label>
                            <input type="date" id="entryDate" name="entryDate" class="form-field-input">
                        </div>
                    </div>
                </div>

                <div class="modal-actions">
                    <button type="button" onclick="closeLoadModal()" class="btn-secondary">Cancel</button>
                    <button type="submit" class="btn-primary">Save Subject</button>
                </div>
            </form>
        </div>
    </div>

    <script src="js/theme.js"></script>
    <script src="js/load-balancer.js"></script>
</body>
</html>
