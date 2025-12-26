package com.whoami.dao;

import com.whoami.model.Habit;
// import com.whoami.model.HabitCompletion; // Not used
import com.whoami.model.Category;
import com.whoami.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HabitDAO {
    
    public int createHabit(Habit habit) {
        String sql = "INSERT INTO habits (user_id, category_id, title, description, frequency) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, habit.getUserId());
            stmt.setInt(2, habit.getCategoryId());
            stmt.setString(3, habit.getTitle());
            stmt.setString(4, habit.getDescription());
            stmt.setString(5, habit.getFrequency());
            
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
    
    public List<Habit> getActiveHabits(int userId) {
        String sql = "SELECT h.*, c.category_name, c.is_custom " +
                    "FROM habits h " +
                    "JOIN task_categories c ON h.category_id = c.category_id " +
                    "WHERE h.user_id = ? AND h.is_active = TRUE " +
                    "ORDER BY h.created_at DESC";
        
        List<Habit> habits = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Habit habit = extractHabit(rs);
                habits.add(habit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return habits;
    }
    
    public Habit getHabitById(int habitId) {
        String sql = "SELECT h.*, c.category_name, c.is_custom " +
                    "FROM habits h " +
                    "JOIN task_categories c ON h.category_id = c.category_id " +
                    "WHERE h.habit_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, habitId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractHabit(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean completeHabit(int habitId, int userId, Date completionDate) {
        String checkSql = "SELECT completion_id FROM habit_completions WHERE habit_id = ? AND completion_date = ?";
        String insertSql = "INSERT INTO habit_completions (habit_id, user_id, completion_date) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            
            checkStmt.setInt(1, habitId);
            checkStmt.setDate(2, completionDate);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                return false;
            }
            
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, habitId);
                insertStmt.setInt(2, userId);
                insertStmt.setDate(3, completionDate);
                insertStmt.executeUpdate();
                
                updateHabitStats(habitId);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean uncompleteHabit(int habitId, Date completionDate) {
        String sql = "DELETE FROM habit_completions WHERE habit_id = ? AND completion_date = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, habitId);
            stmt.setDate(2, completionDate);
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                updateHabitStats(habitId);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Date> getCompletionDates(int habitId, int days) {
        String sql = "SELECT completion_date FROM habit_completions " +
                    "WHERE habit_id = ? AND completion_date >= DATEADD('DAY', ?, CURRENT_DATE) " +
                    "ORDER BY completion_date DESC";
        
        List<Date> dates = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, habitId);
            stmt.setInt(2, -days);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                dates.add(rs.getDate("completion_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates;
    }
    
    public boolean isCompletedToday(int habitId) {
        String sql = "SELECT completion_id FROM habit_completions WHERE habit_id = ? AND completion_date = CURRENT_DATE";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, habitId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isCompletedOnDate(int habitId, Date date) {
        String sql = "SELECT completion_id FROM habit_completions WHERE habit_id = ? AND completion_date = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, habitId);
            stmt.setDate(2, date);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private void updateHabitStats(int habitId) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String totalSql = "SELECT COUNT(*) as total FROM habit_completions WHERE habit_id = ?";
            int totalCompletions = 0;
            
            try (PreparedStatement stmt = conn.prepareStatement(totalSql)) {
                stmt.setInt(1, habitId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    totalCompletions = rs.getInt("total");
                }
            }
            
            int currentStreak = calculateCurrentStreak(habitId, conn);
            int bestStreak = calculateBestStreak(habitId, conn);
            
            String updateSql = "UPDATE habits SET current_streak = ?, best_streak = ?, total_completions = ? WHERE habit_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                stmt.setInt(1, currentStreak);
                stmt.setInt(2, Math.max(bestStreak, currentStreak));
                stmt.setInt(3, totalCompletions);
                stmt.setInt(4, habitId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private int calculateCurrentStreak(int habitId, Connection conn) throws SQLException {
        String sql = "SELECT completion_date FROM habit_completions WHERE habit_id = ? ORDER BY completion_date DESC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, habitId);
            ResultSet rs = stmt.executeQuery();
            
            int streak = 0;
            LocalDate expectedDate = LocalDate.now();
            
            while (rs.next()) {
                LocalDate completionDate = rs.getDate("completion_date").toLocalDate();
                
                if (completionDate.equals(expectedDate) || 
                    (streak == 0 && completionDate.equals(expectedDate.minusDays(1)))) {
                    streak++;
                    expectedDate = completionDate.minusDays(1);
                } else {
                    break;
                }
            }
            return streak;
        }
    }
    
    private int calculateBestStreak(int habitId, Connection conn) throws SQLException {
        String sql = "SELECT completion_date FROM habit_completions WHERE habit_id = ? ORDER BY completion_date ASC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, habitId);
            ResultSet rs = stmt.executeQuery();
            
            int maxStreak = 0;
            int currentStreak = 0;
            LocalDate previousDate = null;
            
            while (rs.next()) {
                LocalDate completionDate = rs.getDate("completion_date").toLocalDate();
                
                if (previousDate == null || completionDate.equals(previousDate.plusDays(1))) {
                    currentStreak++;
                } else {
                    maxStreak = Math.max(maxStreak, currentStreak);
                    currentStreak = 1;
                }
                previousDate = completionDate;
            }
            return Math.max(maxStreak, currentStreak);
        }
    }
    
    public boolean updateHabit(Habit habit) {
        String sql = "UPDATE habits SET category_id = ?, title = ?, description = ?, frequency = ? WHERE habit_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, habit.getCategoryId());
            stmt.setString(2, habit.getTitle());
            stmt.setString(3, habit.getDescription());
            stmt.setString(4, habit.getFrequency());
            stmt.setInt(5, habit.getHabitId());
            stmt.setInt(6, habit.getUserId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteHabit(int habitId, int userId) {
        String sql = "UPDATE habits SET is_active = FALSE WHERE habit_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, habitId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Habit extractHabit(ResultSet rs) throws SQLException {
        Habit habit = new Habit();
        habit.setHabitId(rs.getInt("habit_id"));
        habit.setUserId(rs.getInt("user_id"));
        habit.setCategoryId(rs.getInt("category_id"));
        habit.setTitle(rs.getString("title"));
        habit.setDescription(rs.getString("description"));
        habit.setFrequency(rs.getString("frequency"));
        habit.setCurrentStreak(rs.getInt("current_streak"));
        habit.setBestStreak(rs.getInt("best_streak"));
        habit.setTotalCompletions(rs.getInt("total_completions"));
        habit.setCreatedAt(rs.getTimestamp("created_at"));
        habit.setActive(rs.getBoolean("is_active"));
        
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setCustom(rs.getBoolean("is_custom"));
        habit.setCategory(category);
        
        return habit;
    }
}
