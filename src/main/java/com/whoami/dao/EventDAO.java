package com.whoami.dao;

import com.whoami.model.Event;
import com.whoami.model.Category;
import com.whoami.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    
    public int createEvent(Event event) {
        String sql = "INSERT INTO events (user_id, category_id, title, description, event_date, event_time) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, event.getUserId());
            stmt.setInt(2, event.getCategoryId());
            stmt.setString(3, event.getTitle());
            stmt.setString(4, event.getDescription());
            stmt.setDate(5, event.getEventDate());
            stmt.setTime(6, event.getEventTime());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public List<Event> getEventsByDate(int userId, Date date) {
        String sql = "SELECT e.*, c.category_name, c.is_custom " +
                    "FROM events e " +
                    "JOIN task_categories c ON e.category_id = c.category_id " +
                    "WHERE e.user_id = ? AND e.event_date = ? " +
                    "ORDER BY e.event_time ASC";
        
        List<Event> events = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setDate(2, date);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                events.add(extractEvent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    public List<Event> getUpcomingEvents(int userId, int days) {
        String sql = "SELECT e.*, c.category_name, c.is_custom " +
                    "FROM events e " +
                    "JOIN task_categories c ON e.category_id = c.category_id " +
                    "WHERE e.user_id = ? AND e.event_date >= CURRENT_DATE AND e.event_date <= DATEADD('DAY', ?, CURRENT_DATE) " +
                    "ORDER BY e.event_date ASC, e.event_time ASC";
        
        List<Event> events = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, days);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                events.add(extractEvent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    public Event getEventById(int eventId) {
        String sql = "SELECT e.*, c.category_name, c.is_custom " +
                    "FROM events e " +
                    "JOIN task_categories c ON e.category_id = c.category_id " +
                    "WHERE e.event_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractEvent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean completeEvent(int eventId, int userId) {
        String sql = "UPDATE events SET is_completed = TRUE WHERE event_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean uncompleteEvent(int eventId, int userId) {
        String sql = "UPDATE events SET is_completed = FALSE WHERE event_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateEvent(Event event) {
        String sql = "UPDATE events SET category_id = ?, title = ?, description = ?, event_date = ?, event_time = ? WHERE event_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, event.getCategoryId());
            stmt.setString(2, event.getTitle());
            stmt.setString(3, event.getDescription());
            stmt.setDate(4, event.getEventDate());
            stmt.setTime(5, event.getEventTime());
            stmt.setInt(6, event.getEventId());
            stmt.setInt(7, event.getUserId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteEvent(int eventId, int userId) {
        String sql = "DELETE FROM events WHERE event_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Event extractEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getInt("event_id"));
        event.setUserId(rs.getInt("user_id"));
        event.setCategoryId(rs.getInt("category_id"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setEventDate(rs.getDate("event_date"));
        event.setEventTime(rs.getTime("event_time"));
        event.setCompleted(rs.getBoolean("is_completed"));
        event.setCreatedAt(rs.getTimestamp("created_at"));
        
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setCustom(rs.getBoolean("is_custom"));
        event.setCategory(category);
        
        return event;
    }
}
