package com.whoami.servlet;

import com.whoami.dao.HabitDAO;
import com.whoami.dao.TaskDAO;
import com.whoami.dao.EventDAO;
import com.whoami.dao.CategoryDAO;
import com.whoami.model.Habit;
import com.whoami.model.Task;
import com.whoami.model.Event;
import com.whoami.model.Category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/today")
public class TodayServlet extends HttpServlet {
    private HabitDAO habitDAO;
    private TaskDAO taskDAO;
    private EventDAO eventDAO;
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() {
        habitDAO = new HabitDAO();
        taskDAO = new TaskDAO();
        eventDAO = new EventDAO();
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
        
        String dateParam = request.getParameter("date");
        LocalDate selectedDate = dateParam != null ? LocalDate.parse(dateParam) : LocalDate.now();
        Date sqlDate = Date.valueOf(selectedDate);
        
        List<Habit> habits = habitDAO.getActiveHabits(userId);
        List<Task> tasks = taskDAO.getTasksForDate(userId, sqlDate);
        List<Event> events = eventDAO.getEventsByDate(userId, sqlDate);
        
        List<Category> categories = categoryDAO.getUserCategories(userId);
        
        List<TodayItem> todayItems = new ArrayList<>();
        
        for (Habit habit : habits) {
            TodayItem item = new TodayItem();
            item.setType("habit");
            item.setId(habit.getHabitId());
            item.setTitle(habit.getTitle());
            item.setCategoryName(habit.getCategory().getCategoryName());
            item.setCompleted(habitDAO.isCompletedOnDate(habit.getHabitId(), sqlDate));
            item.setTime(null);
            todayItems.add(item);
        }
        
        for (Task task : tasks) {
            TodayItem item = new TodayItem();
            item.setType("task");
            item.setId(task.getTaskId());
            item.setTitle(task.getTitle());
            item.setCategoryName(task.getCategoryName());
            item.setCompleted(task.isCompleted());
            item.setTime(task.getTaskTime());
            todayItems.add(item);
        }
        
        for (Event event : events) {
            TodayItem item = new TodayItem();
            item.setType("event");
            item.setId(event.getEventId());
            item.setTitle(event.getTitle());
            item.setCategoryName(event.getCategory().getCategoryName());
            item.setCompleted(event.isCompleted());
            item.setTime(event.getEventTime());
            todayItems.add(item);
        }
        
        todayItems.sort((a, b) -> {
            if (a.getTime() == null && b.getTime() == null) return 0;
            if (a.getTime() == null) return 1;
            if (b.getTime() == null) return -1;
            return a.getTime().compareTo(b.getTime());
        });
        
        List<Date> weekDates = new ArrayList<>();
        LocalDate startOfWeek = selectedDate.minusDays(selectedDate.getDayOfWeek().getValue() % 7);
        for (int i = 0; i < 7; i++) {
            weekDates.add(Date.valueOf(startOfWeek.plusDays(i)));
        }
        
        request.setAttribute("selectedDate", Date.valueOf(selectedDate));
        request.setAttribute("weekDates", weekDates);
        request.setAttribute("today", Date.valueOf(LocalDate.now()));
        request.setAttribute("todayItems", todayItems);
        request.setAttribute("totalItems", todayItems.size());
        request.setAttribute("completedItems", todayItems.stream().filter(TodayItem::isCompleted).count());
        request.setAttribute("categories", categories);
        
        request.getRequestDispatcher("/today.jsp").forward(request, response);
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
        String type = request.getParameter("type");
        
        if ("toggle".equals(action) && "event".equals(type)) {
            int eventId = Integer.parseInt(request.getParameter("id"));
            boolean complete = Boolean.parseBoolean(request.getParameter("complete"));
            
            if (complete) {
                eventDAO.completeEvent(eventId, userId);
            } else {
                eventDAO.uncompleteEvent(eventId, userId);
            }
        } else if ("createEvent".equals(action)) {
            try {
                String title = request.getParameter("title");
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                String eventDateStr = request.getParameter("eventDate");
                String eventTimeStr = request.getParameter("eventTime");
                String description = request.getParameter("description");
                
                if (title != null && !title.trim().isEmpty() && eventDateStr != null) {
                    java.sql.Date eventDate = java.sql.Date.valueOf(eventDateStr);
                    java.sql.Time eventTime = null;
                    if (eventTimeStr != null && !eventTimeStr.trim().isEmpty()) {
                        eventTime = java.sql.Time.valueOf(eventTimeStr + ":00");
                    }
                    
                    Event event = new Event(userId, categoryId, title.trim(), 
                                          description != null ? description.trim() : "", 
                                          eventDate, eventTime);
                    eventDAO.createEvent(event);
                }
            } catch (Exception e) {
                // Log error but continue
                e.printStackTrace();
            }
        }
        
        // Redirect back to today page
        response.sendRedirect("today");
    }
    
    public static class TodayItem {
        private String type;
        private int id;
        private String title;
        private String categoryName;
        private boolean completed;
        private java.sql.Time time;
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
        
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
        
        public java.sql.Time getTime() { return time; }
        public void setTime(java.sql.Time time) { this.time = time; }
    }
}
