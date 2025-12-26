<%@ page import="com.whoami.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task & Routine Zone - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <% 
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        String userName = (String) session.getAttribute("userName");
        List<Task> tasks = (List<Task>) request.getAttribute("tasks");
        List<Category> categories = (List<Category>) request.getAttribute("categories");
        TaskEfficiency efficiency = (TaskEfficiency) request.getAttribute("efficiency");
        String currentView = (String) request.getAttribute("currentView");
        String pageTitle = (String) request.getAttribute("pageTitle");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMM dd");
    %>
    
    <nav class="navbar">
        <div class="container navbar-content">
            <a href="dashboard" class="navbar-brand">WhoAmI</a>
            <div class="navbar-menu">
                <a href="dashboard" class="navbar-link">Dashboard</a>
                <div class="navbar-dropdown">
                    <button class="navbar-dropdown-toggle active">
                        My Tasks
                        <svg class="navbar-dropdown-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <polyline points="6 9 12 15 18 9"></polyline>
                        </svg>
                    </button>
                    <div class="navbar-dropdown-menu">
                        <a href="task-zone" class="navbar-dropdown-item active">Task Zone</a>
                        <a href="load-balancer" class="navbar-dropdown-item">Load Balancer</a>
                    </div>
                </div>
                <div class="navbar-dropdown">
                    <button class="navbar-dropdown-toggle">
                        My Mind
                        <svg class="navbar-dropdown-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <polyline points="6 9 12 15 18 9"></polyline>
                        </svg>
                    </button>
                    <div class="navbar-dropdown-menu">
                        <a href="mind-checkin" class="navbar-dropdown-item">Mind Check-In</a>
                        <a href="study-zone" class="navbar-dropdown-item">Study Zone</a>
                    </div>
                </div>
                <div class="navbar-dropdown">
                    <button class="navbar-dropdown-toggle">
                        My Growth
                        <svg class="navbar-dropdown-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <polyline points="6 9 12 15 18 9"></polyline>
                        </svg>
                    </button>
                    <div class="navbar-dropdown-menu">
                        <a href="growth-timeline" class="navbar-dropdown-item">Growth Timeline</a>
                        <a href="insights" class="navbar-dropdown-item">Insights</a>
                        <a href="reflection" class="navbar-dropdown-item">Reflection</a>
                    </div>
                </div>
            </div>
            <div class="navbar-user">
                <span class="user-name"><%= userName %></span>
                <button class="theme-toggle" onclick="toggleTheme()" aria-label="Toggle theme">
                    <svg id="theme-icon" class="theme-icon" viewBox="0 0 24 24" fill="currentColor">
                        <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
                    </svg>
                </button>
                <a href="logout" class="btn btn-secondary" style="padding: 8px 16px; font-size: 14px;">Logout</a>
            </div>
        </div>
    </nav>
    
    <main class="main-content">
        <div class="container">
            <div class="page-header">
                <h1 class="page-title">Task & Routine Zone</h1>
                <p class="page-subtitle">Master your day with intentional task management</p>
            </div>

            <div class="grid grid-3" style="margin-bottom: 32px;">
                <div class="card efficiency-card">
                    <div class="efficiency-header">
                        <h3 class="efficiency-title">Daily Efficiency</h3>
                        <div class="efficiency-percentage <%= efficiency.getDailyEfficiency() >= 80 ? "excellent" : efficiency.getDailyEfficiency() >= 60 ? "great" : efficiency.getDailyEfficiency() >= 40 ? "good" : "fair" %>">
                            <%= String.format("%.0f%%", efficiency.getDailyEfficiency()) %>
                        </div>
                    </div>
                    <div class="efficiency-bar-container">
                        <div class="efficiency-bar">
                            <div class="efficiency-bar-fill <%= efficiency.getDailyEfficiency() >= 80 ? "excellent" : efficiency.getDailyEfficiency() >= 60 ? "great" : efficiency.getDailyEfficiency() >= 40 ? "good" : "fair" %>" 
                                 style="width: <%= efficiency.getDailyEfficiency() %>%"></div>
                        </div>
                    </div>
                    <p class="efficiency-label"><%= efficiency.getEfficiencyLevel() %></p>
                </div>

                <div class="card efficiency-card">
                    <div class="efficiency-header">
                        <h3 class="efficiency-title">Weekly Efficiency</h3>
                        <div class="efficiency-percentage <%= efficiency.getWeeklyEfficiency() >= 80 ? "excellent" : efficiency.getWeeklyEfficiency() >= 60 ? "great" : efficiency.getWeeklyEfficiency() >= 40 ? "good" : "fair" %>">
                            <%= String.format("%.0f%%", efficiency.getWeeklyEfficiency()) %>
                        </div>
                    </div>
                    <div class="efficiency-bar-container">
                        <div class="efficiency-bar">
                            <div class="efficiency-bar-fill <%= efficiency.getWeeklyEfficiency() >= 80 ? "excellent" : efficiency.getWeeklyEfficiency() >= 60 ? "great" : efficiency.getWeeklyEfficiency() >= 40 ? "good" : "fair" %>" 
                                 style="width: <%= efficiency.getWeeklyEfficiency() %>%"></div>
                        </div>
                    </div>
                    <p class="efficiency-label">Last 7 Days</p>
                </div>

                <div class="card task-stats">
                    <div class="stat-row">
                        <span class="stat-label">Total Tasks</span>
                        <span class="stat-value"><%= efficiency.getTotalTasks() %></span>
                    </div>
                    <div class="stat-row">
                        <span class="stat-label">Completed</span>
                        <span class="stat-value success"><%= efficiency.getCompletedTasks() %></span>
                    </div>
                    <div class="stat-row">
                        <span class="stat-label">Pending</span>
                        <span class="stat-value warning"><%= efficiency.getPendingTasks() %></span>
                    </div>
                </div>
            </div>

            <div class="motivation-banner">
                <p class="motivation-text"><%= efficiency.getMotivationalQuote() %></p>
            </div>

            <div class="task-controls">
                <div class="view-tabs">
                    <a href="task-zone?view=today" class="tab <%= currentView.equals("today") ? "active" : "" %>">Today</a>
                    <a href="task-zone?view=upcoming" class="tab <%= currentView.equals("upcoming") ? "active" : "" %>">Upcoming</a>
                    <a href="task-zone?view=completed" class="tab <%= currentView.equals("completed") ? "active" : "" %>">Completed</a>
                    <a href="task-zone?view=overdue" class="tab <%= currentView.equals("overdue") ? "active" : "" %>">Overdue</a>
                </div>
                <button class="btn btn-primary" onclick="openAddTaskModal()">+ Add Task</button>
            </div>

            <div class="card task-list-card">
                <div class="card-header">
                    <h3 class="card-title"><%= pageTitle %></h3>
                    <p class="card-subtitle"><%= tasks.size() %> <%= tasks.size() == 1 ? "task" : "tasks" %></p>
                </div>

                <% if (tasks.isEmpty()) { %>
                    <div class="empty-state" style="padding: 48px 16px;">
                        <div class="empty-state-icon">TASKS</div>
                        <p><% if (currentView.equals("today")) { %>No tasks scheduled for today<% } else if (currentView.equals("completed")) { %>No completed tasks yet<% } else if (currentView.equals("overdue")) { %>No overdue tasks<% } else { %>No upcoming tasks<% } %></p>
                        <button class="btn btn-primary" onclick="openAddTaskModal()" style="margin-top: 16px;">Create Your First Task</button>
                    </div>
                <% } else { %>
                    <div class="task-list">
                        <% for (Task task : tasks) { %>
                            <div class="task-item <%= task.isCompleted() ? "completed" : "" %> <%= task.isOverdue() && !task.isCompleted() ? "overdue" : "" %>">
                                <div class="task-checkbox-wrapper">
                                    <form method="post" action="task-zone" style="margin: 0;">
                                        <input type="hidden" name="taskId" value="<%= task.getTaskId() %>">
                                        <input type="hidden" name="view" value="<%= currentView %>">
                                        <input type="hidden" name="action" value="<%= task.isCompleted() ? "uncomplete" : "complete" %>">
                                        <button type="submit" class="task-checkbox <%= task.isCompleted() ? "checked" : "" %>" aria-label="Toggle task completion">
                                            <% if (task.isCompleted()) { %>
                                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
                                                    <polyline points="20 6 9 17 4 12"></polyline>
                                                </svg>
                                            <% } %>
                                        </button>
                                    </form>
                                </div>
                                
                                <div class="task-content">
                                    <div class="task-header-row">
                                        <h4 class="task-title"><%= task.getTitle() %></h4>
                                        <div class="task-actions">
                                            <button class="task-action-btn edit-task-btn" 
                                                    data-task-id="<%= task.getTaskId() %>" 
                                                    data-title="<%= task.getTitle().replace("\"", "&quot;") %>" 
                                                    data-description="<%= task.getDescription() != null ? task.getDescription().replace("\"", "&quot;") : "" %>" 
                                                    data-type="<%= task.getTaskType() %>" 
                                                    data-category="<%= task.getCategoryId() %>" 
                                                    data-date="<%= task.getTaskDate() != null ? dateFormat.format(task.getTaskDate()) : "" %>" 
                                                    data-time="<%= task.getTaskTime() != null ? timeFormat.format(task.getTaskTime()) : "" %>">Edit</button>
                                            <form method="post" action="task-zone" style="display: inline; margin: 0;">
                                                <input type="hidden" name="taskId" value="<%= task.getTaskId() %>">
                                                <input type="hidden" name="view" value="<%= currentView %>">
                                                <input type="hidden" name="action" value="delete">
                                                <button type="submit" class="task-action-btn delete" onclick="return confirm('Delete this task?')">Delete</button>
                                            </form>
                                        </div>
                                    </div>
                                    
                                    <% if (task.getDescription() != null && !task.getDescription().isEmpty()) { %>
                                        <p class="task-description"><%= task.getDescription() %></p>
                                    <% } %>
                                    
                                    <div class="task-meta">
                                        <span class="task-badge <%= task.getTaskType().toLowerCase() %>"><%= task.getTaskType() %></span>
                                        <span class="task-badge category"><%= task.getCategoryName() %></span>
                                        <% if (task.getTaskDate() != null) { %>
                                            <span class="task-date">
                                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="width: 14px; height: 14px; margin-right: 4px;">
                                                    <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                                                    <line x1="16" y1="2" x2="16" y2="6"></line>
                                                    <line x1="8" y1="2" x2="8" y2="6"></line>
                                                    <line x1="3" y1="10" x2="21" y2="10"></line>
                                                </svg>
                                                <%= displayDateFormat.format(task.getTaskDate()) %>
                                            </span>
                                        <% } %>
                                        <% if (task.getTaskTime() != null) { %>
                                            <span class="task-time">
                                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="width: 14px; height: 14px; margin-right: 4px;">
                                                    <circle cx="12" cy="12" r="10"></circle>
                                                    <polyline points="12 6 12 12 16 14"></polyline>
                                                </svg>
                                                <%= timeFormat.format(task.getTaskTime()) %>
                                            </span>
                                        <% } %>
                                    </div>
                                </div>
                            </div>
                        <% } %>
                    </div>
                <% } %>
            </div>
        </div>
    </main>

    <div id="addTaskModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Add New Task</h3>
                <button class="modal-close" onclick="closeAddTaskModal()">&times;</button>
            </div>
            <form method="post" action="task-zone">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="view" value="<%= currentView %>">
                
                <div class="form-group">
                    <label class="form-label" for="title">Task Title</label>
                    <input type="text" id="title" name="title" class="form-input" required placeholder="Enter task title">
                </div>
                
                <div class="form-group">
                    <label class="form-label" for="description">Description (Optional)</label>
                    <textarea id="description" name="description" class="form-input" rows="3" placeholder="Add task details"></textarea>
                </div>
                
                <div class="form-group">
                    <label class="form-label" for="taskType">Task Type</label>
                    <select id="taskType" name="taskType" class="form-input" required onchange="toggleDateTimeFields()">
                        <option value="SPECIAL">Special Task</option>
                        <option value="ROUTINE">Daily Routine</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label class="form-label" for="categoryId">Category</label>
                    <select id="categoryId" name="categoryId" class="form-input" required>
                        <% for (Category cat : categories) { %>
                            <option value="<%= cat.getCategoryId() %>"><%= cat.getCategoryName() %></option>
                        <% } %>
                    </select>
                </div>
                
                <div id="dateTimeFields">
                    <div class="form-group">
                        <label class="form-label" for="taskDate">Date</label>
                        <input type="date" id="taskDate" name="taskDate" class="form-input">
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label" for="taskTime">Time (Optional)</label>
                        <input type="time" id="taskTime" name="taskTime" class="form-input">
                    </div>
                </div>
                
                <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 8px;">Create Task</button>
            </form>
        </div>
    </div>

    <div id="editTaskModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>Edit Task</h3>
                <button class="modal-close" onclick="closeEditTaskModal()">&times;</button>
            </div>
            <form method="post" action="task-zone">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="view" value="<%= currentView %>">
                <input type="hidden" id="editTaskId" name="taskId">
                
                <div class="form-group">
                    <label class="form-label" for="editTitle">Task Title</label>
                    <input type="text" id="editTitle" name="title" class="form-input" required>
                </div>
                
                <div class="form-group">
                    <label class="form-label" for="editDescription">Description</label>
                    <textarea id="editDescription" name="description" class="form-input" rows="3"></textarea>
                </div>
                
                <div class="form-group">
                    <label class="form-label" for="editTaskType">Task Type</label>
                    <select id="editTaskType" name="taskType" class="form-input" required onchange="toggleEditDateTimeFields()">
                        <option value="SPECIAL">Special Task</option>
                        <option value="ROUTINE">Daily Routine</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label class="form-label" for="editCategoryId">Category</label>
                    <select id="editCategoryId" name="categoryId" class="form-input" required>
                        <% for (Category cat : categories) { %>
                            <option value="<%= cat.getCategoryId() %>"><%= cat.getCategoryName() %></option>
                        <% } %>
                    </select>
                </div>
                
                <div id="editDateTimeFields">
                    <div class="form-group">
                        <label class="form-label" for="editTaskDate">Date</label>
                        <input type="date" id="editTaskDate" name="taskDate" class="form-input">
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label" for="editTaskTime">Time</label>
                        <input type="time" id="editTaskTime" name="taskTime" class="form-input">
                    </div>
                </div>
                
                <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 8px;">Update Task</button>
            </form>
        </div>
    </div>

    <script src="js/theme.js"></script>
    <script src="js/task-zone.js"></script>
</body>
</html>
