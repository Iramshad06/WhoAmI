<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Habits - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/premium-desktop.css">
</head>
<body>
    <jsp:include page="WEB-INF/navbar.jsp">
        <jsp:param name="activePage" value="habits" />
        <jsp:param name="activeSection" value="tasks" />
        <jsp:param name="userName" value="${userName}" />
    </jsp:include>
    <div class="desktop-container">
        <div class="page-header">
            <h1 class="page-title">Habits Dashboard</h1>
            <p class="page-subtitle">Track your daily habits and build lasting streaks</p>
        </div>

        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px;">
            <div class="stats-grid" style="flex: 1;">
                <div class="stat-card">
                    <div class="stat-card-value">${habits.size()}</div>
                    <div class="stat-card-label">Active Habits</div>
                </div>
                <div class="stat-card">
                    <div class="stat-card-value">
                        <c:set var="totalStreak" value="0" />
                        <c:forEach items="${habits}" var="habit">
                            <c:set var="totalStreak" value="${totalStreak + habit.currentStreak}" />
                        </c:forEach>
                        ${totalStreak}
                    </div>
                    <div class="stat-card-label">Total Streaks</div>
                </div>
                <div class="stat-card">
                    <div class="stat-card-value">
                        <c:set var="bestStreak" value="0" />
                        <c:forEach items="${habits}" var="habit">
                            <c:if test="${habit.bestStreak > bestStreak}">
                                <c:set var="bestStreak" value="${habit.bestStreak}" />
                            </c:if>
                        </c:forEach>
                        ${bestStreak}
                    </div>
                    <div class="stat-card-label">Best Streak</div>
                </div>
            </div>
            <button class="btn-primary" onclick="showAddHabitModal()">+ New Habit</button>
        </div>

        <c:choose>
            <c:when test="${empty habits}">
                <div class="section-card">
                    <div class="empty-state">
                        <div class="empty-icon">📊</div>
                        <div class="empty-title">No Habits Yet</div>
                        <div class="empty-text">Create your first habit to start building positive routines</div>
                        <button class="btn-primary" onclick="showAddHabitModal()" style="margin-top: 24px;">Create First Habit</button>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach items="${habits}" var="habit">
                    <div class="horizontal-card">
                        <div class="card-left">
                            <div class="habit-icon" style="background: linear-gradient(135deg, ${habit.category.color}, ${habit.category.color}cc);">
                                ${habit.category.icon}
                            </div>
                            <div class="habit-info">
                                <div class="habit-title">${habit.title}</div>
                                <div class="habit-frequency">${habit.frequency}</div>
                                <div style="margin-top: 8px;">
                                    <span class="category-badge">
                                        <span class="category-icon">${habit.category.icon}</span>
                                        ${habit.category.categoryName}
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="card-center">
                            <div class="progress-row">
                                <c:forEach items="${weekDays}" var="day" varStatus="status">
                                    <c:set var="isCompleted" value="false" />
                                    <c:set var="dayCompletion" value="${habitCompletions[habit.habitId]}" />
                                    <c:if test="${not empty dayCompletion}">
                                        <c:forEach items="${dayCompletion}" var="date">
                                            <c:if test="${date eq day}">
                                                <c:set var="isCompleted" value="true" />
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                    
                                    <c:set var="isToday" value="${day eq today}" />
                                    
                                    <div class="day-indicator" onclick="toggleHabitDay(${habit.habitId}, '${day}', ${isCompleted})">
                                        <div class="day-label">
                                            <fmt:formatDate value="${day}" pattern="EEE" />
                                        </div>
                                        <div class="day-circle ${isCompleted ? 'completed' : ''} ${isToday ? 'today' : ''}">
                                            <fmt:formatDate value="${day}" pattern="d" />
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <div class="card-right">
                            <div class="habit-stats">
                                <div class="stat-item">
                                    <div class="stat-value">${habit.currentStreak}</div>
                                    <div class="stat-label">Current</div>
                                </div>
                                <div class="stat-item">
                                    <div class="stat-value">${habit.bestStreak}</div>
                                    <div class="stat-label">Best</div>
                                </div>
                                <div class="stat-item">
                                    <div class="stat-value">
                                        <fmt:formatNumber value="${habit.getCompletionPercentage(30)}" maxFractionDigits="0" />%
                                    </div>
                                    <div class="stat-label">30-Day</div>
                                </div>
                            </div>
                            <div class="progress-bar-container">
                                <div class="progress-bar" style="width: ${habit.getCompletionPercentage(30)}%"></div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="modal" id="addHabitModal">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title">Create New Habit</h2>
            </div>
            <form action="habits" method="post">
                <input type="hidden" name="action" value="create">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="form-label">Habit Title</label>
                        <input type="text" name="title" class="form-input" placeholder="e.g., Morning Exercise" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Category</label>
                        <select name="categoryId" class="form-select" required>
                            <option value="">Select Category</option>
                            <c:forEach items="${categories}" var="category">
                                <option value="${category.categoryId}">${category.icon} ${category.categoryName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Frequency</label>
                        <select name="frequency" class="form-select" required>
                            <option value="Daily">Daily</option>
                            <option value="Weekdays">Weekdays</option>
                            <option value="Weekends">Weekends</option>
                            <option value="Weekly">Weekly</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Description (Optional)</label>
                        <textarea name="description" class="form-textarea" placeholder="Add notes about this habit..."></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn-secondary" onclick="closeModal('addHabitModal')">Cancel</button>
                    <button type="submit" class="btn-primary">Create Habit</button>
                </div>
            </form>
        </div>
    </div>

    <script src="js/habits.js"></script>
    <script src="js/theme.js"></script>
</body>
</html>
