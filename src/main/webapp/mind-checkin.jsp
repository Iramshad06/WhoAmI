<%@ page import="com.whoami.model.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mind Check-In - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        String userName = (String) session.getAttribute("userName");
        MindLog existingLog = (MindLog) request.getAttribute("existingLog");
    %>

    <jsp:include page="WEB-INF/navbar.jsp">
        <jsp:param name="activePage" value="mind-checkin" />
        <jsp:param name="activeSection" value="mind" />
        <jsp:param name="userName" value="<%= userName %>" />
    </jsp:include>

    <main class="main-content">
        <div class="container">
            <!-- Hero Section -->
            <div class="mind-checkin-hero">
                <div class="hero-content">
                    <div class="hero-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <circle cx="12" cy="12" r="10"></circle>
                            <path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"></path>
                            <line x1="12" y1="17" x2="12.01" y2="17"></line>
                        </svg>
                    </div>
                    <div class="hero-text">
                        <h1 class="hero-title">Mind Check-In</h1>
                        <p class="hero-subtitle">Take 60 seconds to understand your current mental state</p>
                    </div>
                </div>
                <div class="hero-stats">
                    <div class="stat-item">
                        <div class="stat-number"><%= existingLog != null ? "1" : "0" %></div>
                        <div class="stat-label">Today's Check-ins</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-number">5</div>
                        <div class="stat-label">Questions</div>
                    </div>
                </div>
            </div>

            <% if (existingLog != null) { %>
            <div class="alert-card alert-info">
                <div class="alert-icon">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <circle cx="12" cy="12" r="10"></circle>
                        <line x1="12" y1="16" x2="12" y2="12"></line>
                        <line x1="12" y1="8" x2="12.01" y2="8"></line>
                    </svg>
                </div>
                <div class="alert-content">
                    <h3 class="alert-title">Check-in Already Completed</h3>
                    <p class="alert-message">You've already checked in today. You can update your responses below.</p>
                </div>
            </div>
            <% } %>

            <!-- Check-in Form -->
            <div class="checkin-card">
                <form method="post" action="mind-checkin" class="checkin-form">
                    <!-- Focus Level -->
                    <div class="question-section">
                        <div class="question-header">
                            <div class="question-number">1</div>
                            <div class="question-content">
                                <h3 class="question-title">Focus Level</h3>
                                <p class="question-subtitle">How focused do you feel right now?</p>
                            </div>
                        </div>
                        <div class="options-grid">
                            <label class="option-card <%= existingLog != null && existingLog.getFocusLevel() == 1 ? "selected" : "" %>">
                                <input type="radio" name="focus" value="1" <%= existingLog != null && existingLog.getFocusLevel() == 1 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <line x1="15" y1="9" x2="9" y2="15"></line>
                                            <line x1="9" y1="9" x2="15" y2="15"></line>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">1 - Scattered</div>
                                        <div class="option-desc">Can't focus, mind wandering</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getFocusLevel() == 2 ? "selected" : "" %>">
                                <input type="radio" name="focus" value="2" <%= existingLog != null && existingLog.getFocusLevel() == 2 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <line x1="12" y1="8" x2="12" y2="16"></line>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">2 - Distracted</div>
                                        <div class="option-desc">Some focus but easily sidetracked</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getFocusLevel() == 3 ? "selected" : "" %>">
                                <input type="radio" name="focus" value="3" <%= existingLog != null && existingLog.getFocusLevel() == 3 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <path d="M12 8v4l2 2"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">3 - Moderate</div>
                                        <div class="option-desc">Decent focus with occasional lapses</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getFocusLevel() == 4 ? "selected" : "" %>">
                                <input type="radio" name="focus" value="4" <%= existingLog != null && existingLog.getFocusLevel() == 4 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <polyline points="12,6 12,12 16,10"></polyline>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">4 - Focused</div>
                                        <div class="option-desc">Good concentration, productive</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getFocusLevel() == 5 ? "selected" : "" %>">
                                <input type="radio" name="focus" value="5" <%= existingLog != null && existingLog.getFocusLevel() == 5 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">5 - Laser Sharp</div>
                                        <div class="option-desc">Peak focus, highly productive</div>
                                    </div>
                                </div>
                            </label>
                        </div>
                    </div>

                    <!-- Energy Level -->
                    <div class="question-section">
                        <div class="question-header">
                            <div class="question-number">2</div>
                            <div class="question-content">
                                <h3 class="question-title">Energy Level</h3>
                                <p class="question-subtitle">How energetic do you feel physically?</p>
                            </div>
                        </div>
                        <div class="options-grid">
                            <label class="option-card <%= existingLog != null && existingLog.getEnergyLevel() == 1 ? "selected" : "" %>">
                                <input type="radio" name="energy" value="1" <%= existingLog != null && existingLog.getEnergyLevel() == 1 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon exhausted">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M12 2l-1 7h2l-1-7z"></path>
                                            <path d="M12 9c-1.5 0-3 1-3 3 0 1.5 1 3 3 3s3-1.5 3-3c0-2-1.5-3-3-3z"></path>
                                            <path d="M8 22l8-8"></path>
                                            <path d="M16 22l-8-8"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">1 - Exhausted</div>
                                        <div class="option-desc">No energy, completely drained</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getEnergyLevel() == 2 ? "selected" : "" %>">
                                <input type="radio" name="energy" value="2" <%= existingLog != null && existingLog.getEnergyLevel() == 2 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon low">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <line x1="12" y1="8" x2="12" y2="12"></line>
                                            <line x1="12" y1="16" x2="12.01" y2="16"></line>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">2 - Low</div>
                                        <div class="option-desc">Tired, need rest soon</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getEnergyLevel() == 3 ? "selected" : "" %>">
                                <input type="radio" name="energy" value="3" <%= existingLog != null && existingLog.getEnergyLevel() == 3 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon moderate">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <path d="M12 8v4"></path>
                                            <path d="M12 16h.01"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">3 - Moderate</div>
                                        <div class="option-desc">Steady energy, manageable</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getEnergyLevel() == 4 ? "selected" : "" %>">
                                <input type="radio" name="energy" value="4" <%= existingLog != null && existingLog.getEnergyLevel() == 4 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon good">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <path d="M8 14l2 2 4-4"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">4 - Good</div>
                                        <div class="option-desc">Energetic, feeling good</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getEnergyLevel() == 5 ? "selected" : "" %>">
                                <input type="radio" name="energy" value="5" <%= existingLog != null && existingLog.getEnergyLevel() == 5 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon high">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">5 - Energized</div>
                                        <div class="option-desc">High energy, ready to go</div>
                                    </div>
                                </div>
                            </label>
                        </div>
                    </div>

                    <!-- Emotional Load -->
                    <div class="question-section">
                        <div class="question-header">
                            <div class="question-number">3</div>
                            <div class="question-content">
                                <h3 class="question-title">Emotional Load</h3>
                                <p class="question-subtitle">How emotionally burdened do you feel?</p>
                            </div>
                        </div>
                        <div class="options-grid">
                            <label class="option-card <%= existingLog != null && existingLog.getEmotionalLoad() == 1 ? "selected" : "" %>">
                                <input type="radio" name="emotional" value="1" <%= existingLog != null && existingLog.getEmotionalLoad() == 1 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon light">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">1 - Light</div>
                                        <div class="option-desc">Emotionally free, at peace</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getEmotionalLoad() == 2 ? "selected" : "" %>">
                                <input type="radio" name="emotional" value="2" <%= existingLog != null && existingLog.getEmotionalLoad() == 2 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon light">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <path d="M8 14s1.5 2 4 2 4-2 4-2"></path>
                                            <line x1="9" y1="9" x2="9.01" y2="9"></line>
                                            <line x1="15" y1="9" x2="15.01" y2="9"></line>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">2 - Mild</div>
                                        <div class="option-desc">Some emotions, manageable</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getEmotionalLoad() == 3 ? "selected" : "" %>">
                                <input type="radio" name="emotional" value="3" <%= existingLog != null && existingLog.getEmotionalLoad() == 3 ? "checked" : "" %> required>
                                <input type="radio" name="emotional" value="3" <%= existingLog != null && existingLog.getEmotionalLoad() == 3 ? "checked" : "" %>>
                                <div class="option-content">
                                    <div class="option-icon moderate">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <line x1="8" y1="15" x2="8" y2="17"></line>
                                            <line x1="16" y1="15" x2="16" y2="17"></line>
                                            <path d="M12 9c1.5 0 3 1 3 3 0 1-1 2-3 2s-3-1-3-2c0-2 1.5-3 3-3z"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">3 - Moderate</div>
                                        <div class="option-desc">Noticeable emotional weight</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getEmotionalLoad() == 4 ? "selected" : "" %>">
                                <input type="radio" name="emotional" value="4" <%= existingLog != null && existingLog.getEmotionalLoad() == 4 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon heavy">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <path d="M16 16c-1.5 1.5-4 1.5-5.5 0S7 12.5 8.5 11 12 10.5 16 12s1.5 3.5 0 4z"></path>
                                            <line x1="9" y1="9" x2="9.01" y2="9"></line>
                                            <line x1="15" y1="9" x2="15.01" y2="9"></line>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">4 - Heavy</div>
                                        <div class="option-desc">Strong emotional burden</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getEmotionalLoad() == 5 ? "selected" : "" %>">
                                <input type="radio" name="emotional" value="5" <%= existingLog != null && existingLog.getEmotionalLoad() == 5 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon heavy">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">5 - Overwhelming</div>
                                        <div class="option-desc">Emotionally overwhelmed</div>
                                    </div>
                                </div>
                            </label>
                        </div>
                    </div>

                    <!-- Mental Clarity -->
                    <div class="question-section">
                        <div class="question-header">
                            <div class="question-number">4</div>
                            <div class="question-content">
                                <h3 class="question-title">Mental Clarity</h3>
                                <p class="question-subtitle">How clear is your thinking right now?</p>
                            </div>
                        </div>
                        <div class="options-grid">
                            <label class="option-card <%= existingLog != null && existingLog.getClarityLevel() == 1 ? "selected" : "" %>">
                                <input type="radio" name="clarity" value="1" <%= existingLog != null && existingLog.getClarityLevel() == 1 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon foggy">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M3 15a4 4 0 0 1 4-4h1a5 5 0 0 1 5 5v1a4 4 0 0 1-4 4H7a4 4 0 0 1-4-4z"></path>
                                            <path d="M7 9a4 4 0 0 1 4-4h1a5 5 0 0 1 5 5v1a4 4 0 0 1-4 4"></path>
                                            <path d="M11 3a4 4 0 0 1 4-4h1a5 5 0 0 1 5 5v1a4 4 0 0 1-4 4"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">1 - Foggy</div>
                                        <div class="option-desc">Confused, unclear thinking</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getClarityLevel() == 2 ? "selected" : "" %>">
                                <input type="radio" name="clarity" value="2" <%= existingLog != null && existingLog.getClarityLevel() == 2 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon foggy">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <line x1="12" y1="8" x2="12" y2="12"></line>
                                            <line x1="12" y1="16" x2="12.01" y2="16"></line>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">2 - Hazy</div>
                                        <div class="option-desc">Some confusion, not sharp</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getClarityLevel() == 3 ? "selected" : "" %>">
                                <input type="radio" name="clarity" value="3" <%= existingLog != null && existingLog.getClarityLevel() == 3 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon moderate">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <path d="M12 8v4"></path>
                                            <path d="M12 16h.01"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">3 - Moderate</div>
                                        <div class="option-desc">Decent clarity, functional</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getClarityLevel() == 4 ? "selected" : "" %>">
                                <input type="radio" name="clarity" value="4" <%= existingLog != null && existingLog.getClarityLevel() == 4 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon clear">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <path d="M9 12l2 2 4-4"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">4 - Clear</div>
                                        <div class="option-desc">Good mental clarity</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getClarityLevel() == 5 ? "selected" : "" %>">
                                <input type="radio" name="clarity" value="5" <%= existingLog != null && existingLog.getClarityLevel() == 5 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon clear">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">5 - Crystal Clear</div>
                                        <div class="option-desc">Exceptional mental clarity</div>
                                    </div>
                                </div>
                            </label>
                        </div>
                    </div>

                    <!-- Stress Level -->
                    <div class="question-section">
                        <div class="question-header">
                            <div class="question-number">5</div>
                            <div class="question-content">
                                <h3 class="question-title">Stress Level</h3>
                                <p class="question-subtitle">How stressed do you feel right now?</p>
                            </div>
                        </div>
                        <div class="options-grid">
                            <label class="option-card <%= existingLog != null && existingLog.getStressLevel() == 1 ? "selected" : "" %>">
                                <input type="radio" name="stress" value="1" <%= existingLog != null && existingLog.getStressLevel() == 1 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon calm">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">1 - Calm</div>
                                        <div class="option-desc">Completely relaxed, no stress</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getStressLevel() == 2 ? "selected" : "" %>">
                                <input type="radio" name="stress" value="2" <%= existingLog != null && existingLog.getStressLevel() == 2 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon calm">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <path d="M8 14s1.5 2 4 2 4-2 4-2"></path>
                                            <line x1="9" y1="9" x2="9.01" y2="9"></line>
                                            <line x1="15" y1="9" x2="15.01" y2="9"></line>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">2 - Mild</div>
                                        <div class="option-desc">Minimal stress, manageable</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getStressLevel() == 3 ? "selected" : "" %>">
                                <input type="radio" name="stress" value="3" <%= existingLog != null && existingLog.getStressLevel() == 3 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon moderate">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <line x1="8" y1="15" x2="8" y2="17"></line>
                                            <line x1="16" y1="15" x2="16" y2="17"></line>
                                            <path d="M12 9c1.5 0 3 1 3 3 0 1-1 2-3 2s-3-1-3-2c0-2 1.5-3 3-3z"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">3 - Moderate</div>
                                        <div class="option-desc">Noticeable stress, handling it</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getStressLevel() == 4 ? "selected" : "" %>">
                                <input type="radio" name="stress" value="4" <%= existingLog != null && existingLog.getStressLevel() == 4 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon stressed">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <circle cx="12" cy="12" r="10"></circle>
                                            <path d="M16 16c-1.5 1.5-4 1.5-5.5 0S7 12.5 8.5 11 12 10.5 16 12s1.5 3.5 0 4z"></path>
                                            <line x1="9" y1="9" x2="9.01" y2="9"></line>
                                            <line x1="15" y1="9" x2="15.01" y2="9"></line>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">4 - High</div>
                                        <div class="option-desc">Significant stress, challenging</div>
                                    </div>
                                </div>
                            </label>
                            <label class="option-card <%= existingLog != null && existingLog.getStressLevel() == 5 ? "selected" : "" %>">
                                <input type="radio" name="stress" value="5" <%= existingLog != null && existingLog.getStressLevel() == 5 ? "checked" : "" %> required>
                                <div class="option-content">
                                    <div class="option-icon stressed">
                                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                            <path d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L4.082 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
                                        </svg>
                                    </div>
                                    <div class="option-text">
                                        <div class="option-label">5 - Overwhelmed</div>
                                        <div class="option-desc">Extremely stressed, overwhelming</div>
                                    </div>
                                </div>
                            </label>
                        </div>
                    </div>

                    <!-- Submit Button -->
                    <div class="form-actions">
                        <button type="submit" class="btn-primary-large">
                            <svg class="btn-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M9 12l2 2 4-4"></path>
                                <path d="M21 12c-1 0-3-1-3-3s2-3 3-3"></path>
                                <path d="M3 12c1 0 3-1 3-3s-2-3-3-3"></path>
                                <path d="M12 3c-1 0-3 1-3 3s2 3 3 3"></path>
                                <path d="M12 21c1 0 3-1 3-3s-2-3-3-3"></path>
                            </svg>
                            <%= existingLog != null ? "Update Check-In" : "Complete Check-In" %>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </main>

    <script src="js/theme.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Handle option card selection
            const optionCards = document.querySelectorAll('.option-card');
            optionCards.forEach(card => {
                card.addEventListener('click', function() {
                    // Find the radio button inside this card
                    const radio = this.querySelector('input[type="radio"]');
                    if (radio) {
                        radio.checked = true;
                        // Remove selected class from siblings in the same question
                        const questionSection = this.closest('.question-section');
                        questionSection.querySelectorAll('.option-card').forEach(c => {
                            c.classList.remove('selected');
                        });
                        // Add selected class to clicked card
                        this.classList.add('selected');
                    }
                });
            });

            // Also handle radio button changes
            const radioButtons = document.querySelectorAll('input[type="radio"]');
            radioButtons.forEach(radio => {
                radio.addEventListener('change', function() {
                    if (this.checked) {
                        // Remove selected class from siblings
                        const questionSection = this.closest('.question-section');
                        questionSection.querySelectorAll('.option-card').forEach(c => {
                            c.classList.remove('selected');
                        });
                        // Add selected class to parent card
                        this.closest('.option-card').classList.add('selected');
                    }
                });
            });
        });
    </script>
</body>
</html>
