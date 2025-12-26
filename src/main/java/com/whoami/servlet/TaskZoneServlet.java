package com.whoami.servlet;

import com.whoami.dao.CategoryDAO;
import com.whoami.dao.TaskDAO;
import com.whoami.model.Category;
import com.whoami.model.Task;
import com.whoami.model.TaskEfficiency;
import com.whoami.util.RoutineTaskResetter;
import com.whoami.util.TaskEfficiencyCalculator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@WebServlet("/task-zone")
public class TaskZoneServlet extends HttpServlet {

    private TaskDAO taskDAO = new TaskDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private TaskEfficiencyCalculator efficiencyCalculator = new TaskEfficiencyCalculator();
    private RoutineTaskResetter routineResetter = new RoutineTaskResetter();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        routineResetter.performDailyReset(userId);

        String view = request.getParameter("view");
        if (view == null) view = "today";

        List<Task> tasks;
        String pageTitle;
        
        switch(view) {
            case "upcoming":
                tasks = taskDAO.getUpcomingTasks(userId);
                pageTitle = "Upcoming Tasks";
                break;
            case "completed":
                tasks = taskDAO.getCompletedTasks(userId, 50);
                pageTitle = "Completed Tasks";
                break;
            case "overdue":
                tasks = taskDAO.getOverdueTasks(userId);
                pageTitle = "Overdue Tasks";
                break;
            default:
                tasks = taskDAO.getTodayTasks(userId);
                pageTitle = "Today's Tasks";
                view = "today";
        }

        List<Category> categories = categoryDAO.getUserCategories(userId);
        TaskEfficiency efficiency = efficiencyCalculator.calculateEfficiency(userId);

        request.setAttribute("tasks", tasks);
        request.setAttribute("categories", categories);
        request.setAttribute("efficiency", efficiency);
        request.setAttribute("currentView", view);
        request.setAttribute("pageTitle", pageTitle);

        request.getRequestDispatcher("task-zone.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            handleAddTask(request, userId);
        } else if ("edit".equals(action)) {
            handleEditTask(request, userId);
        } else if ("delete".equals(action)) {
            handleDeleteTask(request, userId);
        } else if ("complete".equals(action)) {
            handleCompleteTask(request, userId);
        } else if ("uncomplete".equals(action)) {
            handleUncompleteTask(request, userId);
        } else if ("addCategory".equals(action)) {
            handleAddCategory(request, userId);
        }

        String view = request.getParameter("view");
        if (view != null && !view.isEmpty()) {
            response.sendRedirect("task-zone?view=" + view);
        } else {
            response.sendRedirect("task-zone");
        }
    }

    private void handleAddTask(HttpServletRequest request, int userId) {
        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String taskType = request.getParameter("taskType");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String dateStr = request.getParameter("taskDate");
            String timeStr = request.getParameter("taskTime");

            Date taskDate = null;
            Time taskTime = null;

            if (taskType.equals("ROUTINE")) {
                taskDate = new Date(System.currentTimeMillis());
            } else if (dateStr != null && !dateStr.isEmpty()) {
                taskDate = Date.valueOf(dateStr);
            }

            if (timeStr != null && !timeStr.isEmpty()) {
                taskTime = Time.valueOf(timeStr + ":00");
            }

            Task task = new Task(userId, categoryId, title, description, taskType, taskDate, taskTime);
            taskDAO.createTask(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEditTask(HttpServletRequest request, int userId) {
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String taskType = request.getParameter("taskType");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String dateStr = request.getParameter("taskDate");
            String timeStr = request.getParameter("taskTime");

            Task task = taskDAO.getTaskById(taskId, userId);
            if (task != null) {
                task.setTitle(title);
                task.setDescription(description);
                task.setTaskType(taskType);
                task.setCategoryId(categoryId);

                if (taskType.equals("ROUTINE")) {
                    task.setTaskDate(new Date(System.currentTimeMillis()));
                } else if (dateStr != null && !dateStr.isEmpty()) {
                    task.setTaskDate(Date.valueOf(dateStr));
                }

                if (timeStr != null && !timeStr.isEmpty()) {
                    task.setTaskTime(Time.valueOf(timeStr + ":00"));
                }

                taskDAO.updateTask(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteTask(HttpServletRequest request, int userId) {
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            taskDAO.deleteTask(taskId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCompleteTask(HttpServletRequest request, int userId) {
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            taskDAO.completeTask(taskId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleUncompleteTask(HttpServletRequest request, int userId) {
        try {
            int taskId = Integer.parseInt(request.getParameter("taskId"));
            taskDAO.uncompleteTask(taskId, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAddCategory(HttpServletRequest request, int userId) {
        try {
            String categoryName = request.getParameter("categoryName");
            if (categoryName != null && !categoryName.trim().isEmpty()) {
                if (!categoryDAO.categoryExists(userId, categoryName)) {
                    Category category = new Category(userId, categoryName, true);
                    categoryDAO.createCategory(category);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
