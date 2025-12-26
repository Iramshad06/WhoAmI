<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Today - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/premium-desktop.css">
</head>
<body>
    <jsp:include page="WEB-INF/navbar.jsp">
        <jsp:param name="activePage" value="today" />
        <jsp:param name="activeSection" value="tasks" />
        <jsp:param name="userName" value="${userName}" />
    </jsp:include>
    <div class="desktop-container">
        <div class="page-header">
            <h1 class="page-title">Today's Overview</h1>
            <p class="page-subtitle">
                <fmt:formatDate value="${selectedDate}" pattern="EEEE, MMMM d, yyyy" />
            </p>
        </div>

        <div class="date-selector">
            <c:forEach items="${weekDates}" var="date">
                <c:set var="isToday" value="${date eq today}" />
                <c:set var="isSelected" value="${date eq selectedDate}" />
                
                <a href="today?date=${date}" class="date-pill ${isSelected ? 'active' : ''} ${isToday ? 'today' : ''}">
                    <div class="date-day">
                        <fmt:formatDate value="${date}" pattern="EEE" />
                    </div>
                    <div class="date-number">
                        <fmt:formatDate value="${date}" pattern="d" />
                    </div>
                    <c:if test="${isToday}">
                        <div class="date-label">Today</div>
                    </c:if>
                </a>
            </c:forEach>
        </div>

        <div class="stats-grid" style="margin-bottom: 32px;">
            <div class="stat-card">
                <div class="stat-card-value">${totalItems}</div>
                <div class="stat-card-label">Total Items</div>
            </div>
            <div class="stat-card">
                <div class="stat-card-value">${completedItems}</div>
                <div class="stat-card-label">Completed</div>
            </div>
            <div class="stat-card">
                <div class="stat-card-value">
                    <c:choose>
                        <c:when test="${totalItems > 0}">
                            <fmt:formatNumber value="${(completedItems / totalItems) * 100}" maxFractionDigits="0" />%
                        </c:when>
                        <c:otherwise>0%</c:otherwise>
                    </c:choose>
                </div>
                <div class="stat-card-label">Progress</div>
            </div>
            <div class="stat-card">
                <div class="stat-card-value">${totalItems - completedItems}</div>
                <div class="stat-card-label">Remaining</div>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty todayItems}">
                <div class="section-card">
                    <div class="empty-state">
                        <div class="empty-icon">🎉</div>
                        <div class="empty-title">No Items Scheduled</div>
                        <div class="empty-text">You have a free day! Relax or create new tasks.</div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="desktop-grid">
                    <div class="grid-column">
                        <div class="section-card">
                            <div class="section-header">
                                <h2 class="section-title">Tasks & Habits</h2>
                                <span class="section-count">
                                    <c:set var="taskHabitCount" value="0" />
                                    <c:forEach items="${todayItems}" var="item">
                                        <c:if test="${item.type eq 'task' or item.type eq 'habit'}">
                                            <c:set var="taskHabitCount" value="${taskHabitCount + 1}" />
                                        </c:if>
                                    </c:forEach>
                                    ${taskHabitCount} items
                                </span>
                            </div>
                            <div class="item-list">
                                <c:forEach items="${todayItems}" var="item">
                                    <c:if test="${item.type eq 'task' or item.type eq 'habit'}">
                                        <div class="item-card" onclick="openItemDetails('${item.type}', ${item.id})">
                                            <div class="checkbox-circle ${item.completed ? 'checked' : ''}" 
                                                 onclick="event.stopPropagation(); toggleTodayItem('${item.type}', ${item.id}, ${!item.completed})">
                                            </div>
                                            <div class="item-content">
                                                <div class="item-title">${item.title}</div>
                                                <div class="item-meta">
                                                    <span class="type-badge ${item.type}">${item.type}</span>
                                                    <c:if test="${not empty item.categoryName}">
                                                        <span class="item-time">${item.categoryName}</span>
                                                    </c:if>
                                                    <c:if test="${not empty item.time}">
                                                        <span class="item-time">
                                                            <fmt:formatDate value="${item.time}" pattern="HH:mm" />
                                                        </span>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                    </div>

                    <div class="grid-column">
                        <div class="section-card">
                            <div class="section-header">
                                <h2 class="section-title">Events</h2>
                                <span class="section-count">
                                    <c:set var="eventCount" value="0" />
                                    <c:forEach items="${todayItems}" var="item">
                                        <c:if test="${item.type eq 'event'}">
                                            <c:set var="eventCount" value="${eventCount + 1}" />
                                        </c:if>
                                    </c:forEach>
                                    ${eventCount} events
                                </span>
                            </div>
                            <c:choose>
                                <c:when test="${eventCount eq 0}">
                                    <div class="empty-state" style="padding: 32px 16px;">
                                        <div class="empty-icon" style="font-size: 48px;">📅</div>
                                        <div class="empty-title" style="font-size: 16px;">No Events Today</div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="item-list">
                                        <c:forEach items="${todayItems}" var="item">
                                            <c:if test="${item.type eq 'event'}">
                                                <div class="item-card" onclick="openItemDetails('event', ${item.id})">
                                                    <div class="checkbox-circle ${item.completed ? 'checked' : ''}" 
                                                         onclick="event.stopPropagation(); toggleTodayItem('event', ${item.id}, ${!item.completed})">
                                                    </div>
                                                    <div class="item-content">
                                                        <div class="item-title">${item.title}</div>
                                                        <div class="item-meta">
                                                            <span class="type-badge event">EVENT</span>
                                                            <c:if test="${not empty item.categoryName}">
                                                                <span class="item-time">${item.categoryName}</span>
                                                            </c:if>
                                                            <c:if test="${not empty item.time}">
                                                                <span class="item-time">
                                                                    <fmt:formatDate value="${item.time}" pattern="HH:mm" />
                                                                </span>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="section-card">
                            <div class="section-header">
                                <h2 class="section-title">Quick Actions</h2>
                            </div>
                            <div style="display: flex; flex-direction: column; gap: 12px;">
                                <button class="btn-primary" onclick="window.location.href='habits'">+ Add Habit</button>
                                <button class="btn-primary" onclick="openEventModal()">+ Add Event</button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Add Event Modal -->
    <div class="modal" id="addEventModal">
        <div class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title">Create Event</h2>
            </div>
            <form action="today" method="post">
                <input type="hidden" name="action" value="createEvent">
                <input type="hidden" name="date" value="${selectedDate}">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="form-label">Event Title</label>
                        <input type="text" name="title" class="form-input" placeholder="e.g., Doctor Appointment" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Category</label>
                        <select name="categoryId" class="form-select" required>
                            <option value="">Select Category</option>
                            <c:forEach items="${categories}" var="category">
                                <option value="${category.categoryId}">${category.categoryName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label">Date</label>
                            <input type="date" name="eventDate" class="form-input" value="${selectedDate}" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Time (Optional)</label>
                            <input type="time" name="eventTime" class="form-input">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Description (Optional)</label>
                        <textarea name="description" class="form-textarea" placeholder="Add details about this event..." rows="3"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn-secondary" onclick="closeModal('addEventModal')">Cancel</button>
                    <button type="submit" class="btn-primary">Create Event</button>
                </div>
            </form>
        </div>
    </div>

    <script src="js/today.js"></script>
    <script src="js/theme.js"></script>
</body>
</html>
