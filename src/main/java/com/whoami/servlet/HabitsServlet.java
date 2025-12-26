package com.whoami.servlet;

import com.whoami.dao.HabitDAO;
import com.whoami.dao.CategoryDAO;
import com.whoami.model.Habit;
import com.whoami.model.Category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/habits")
public class HabitsServlet extends HttpServlet {
    private HabitDAO habitDAO;
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() {
        habitDAO = new HabitDAO();
        categoryDAO = new CategoryDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        
        List<Habit> habits = habitDAO.getActiveHabits(userId);
        if (habits == null) {
            habits = new ArrayList<>();
        }
        
        List<Category> categories = categoryDAO.getUserCategories(userId);
        if (categories == null) {
            categories = new ArrayList<>();
        }
        
        java.util.Map<Integer, List<Date>> habitCompletions = new java.util.HashMap<>();
        java.time.LocalDate today = java.time.LocalDate.now();
        java.util.List<Date> weekDays = new java.util.ArrayList<>();
        
        for (int i = 0; i < 7; i++) {
            weekDays.add(Date.valueOf(today.minusDays(6 - i)));
        }
        
        for (Habit habit : habits) {
            List<Date> completions = habitDAO.getCompletionDates(habit.getHabitId(), 7);
            if (completions != null) {
                habitCompletions.put(habit.getHabitId(), completions);
            }
        }
        
        request.setAttribute("habits", habits);
        request.setAttribute("categories", categories);
        request.setAttribute("habitCompletions", habitCompletions);
        request.setAttribute("weekDays", weekDays);
        request.setAttribute("today", Date.valueOf(today));
        request.getRequestDispatcher("/habits.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            createHabit(request, userId);
        } else if ("complete".equals(action)) {
            completeHabit(request, userId);
        } else if ("uncomplete".equals(action)) {
            uncompleteHabit(request, userId);
        } else if ("update".equals(action)) {
            updateHabit(request, userId);
        } else if ("delete".equals(action)) {
            deleteHabit(request, userId);
        }
        
        response.sendRedirect("habits");
    }
    
    private void createHabit(HttpServletRequest request, int userId) {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String frequency = request.getParameter("frequency");
        
        Habit habit = new Habit(userId, categoryId, title, description, frequency);
        habitDAO.createHabit(habit);
    }
    
    private void completeHabit(HttpServletRequest request, int userId) {
        int habitId = Integer.parseInt(request.getParameter("habitId"));
        String dateStr = request.getParameter("date");
        
        Date date = dateStr != null ? Date.valueOf(dateStr) : Date.valueOf(LocalDate.now());
        habitDAO.completeHabit(habitId, userId, date);
    }
    
    private void uncompleteHabit(HttpServletRequest request, int userId) {
        int habitId = Integer.parseInt(request.getParameter("habitId"));
        String dateStr = request.getParameter("date");
        
        Date date = dateStr != null ? Date.valueOf(dateStr) : Date.valueOf(LocalDate.now());
        habitDAO.uncompleteHabit(habitId, date);
    }
    
    private void updateHabit(HttpServletRequest request, int userId) {
        int habitId = Integer.parseInt(request.getParameter("habitId"));
        Habit habit = habitDAO.getHabitById(habitId);
        
        if (habit != null && habit.getUserId() == userId) {
            habit.setTitle(request.getParameter("title"));
            habit.setDescription(request.getParameter("description"));
            habit.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
            habit.setFrequency(request.getParameter("frequency"));
            habitDAO.updateHabit(habit);
        }
    }
    
    private void deleteHabit(HttpServletRequest request, int userId) {
        int habitId = Integer.parseInt(request.getParameter("habitId"));
        habitDAO.deleteHabit(habitId, userId);
    }
}
