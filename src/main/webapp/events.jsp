<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Event - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/premium-desktop.css">
</head>
<body>
    <jsp:include page="WEB-INF/navbar.jsp">
        <jsp:param name="activePage" value="" />
        <jsp:param name="activeSection" value="" />
        <jsp:param name="userName" value="${userName}" />
    </jsp:include>

    <div class="desktop-container">
        <div class="page-header">
            <h1 class="page-title">Create Event</h1>
            <p class="page-subtitle">Add a one-time event or activity for a specific date</p>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error" style="margin-bottom: 24px;">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Event Details</h2>
            </div>
            <div class="card-body">
                <form method="post" action="events">
                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label" for="title">Event Title *</label>
                            <input type="text" id="title" name="title" class="form-input" placeholder="e.g., Doctor Appointment" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label" for="category">Category *</label>
                            <select id="category" name="category" class="form-select" required>
                                <option value="">Select Category</option>
                                <c:forEach items="${categories}" var="category">
                                    <option value="${category.categoryId}">${category.categoryName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label" for="eventDate">Date *</label>
                            <input type="date" id="eventDate" name="eventDate" class="form-input" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label" for="eventTime">Time (Optional)</label>
                            <input type="time" id="eventTime" name="eventTime" class="form-input">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="description">Description (Optional)</label>
                        <textarea id="description" name="description" class="form-textarea" placeholder="Add details about this event..." rows="3"></textarea>
                    </div>

                    <div class="form-actions">
                        <a href="today" class="btn-secondary">Cancel</a>
                        <button type="submit" class="btn-primary">Create Event</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        // Set minimum date to today
        document.addEventListener('DOMContentLoaded', function() {
            const dateInput = document.getElementById('eventDate');
            const today = new Date().toISOString().split('T')[0];
            dateInput.setAttribute('min', today);
            dateInput.value = today;
        });
    </script>
    <script src="js/theme.js"></script>
</body>
</html>