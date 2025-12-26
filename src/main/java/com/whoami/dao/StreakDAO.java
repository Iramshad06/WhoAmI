package com.whoami.dao;

import com.whoami.model.Streak;
import com.whoami.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StreakDAO {
    
    public boolean initializeStreaks(int userId) {
        String sql = "INSERT IGNORE INTO streaks (user_id, streak_type, current_count, best_count) VALUES (?, ?, 0, 0)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, "mind_checkin");
            stmt.executeUpdate();
            
            stmt.setInt(1, userId);
            stmt.setString(2, "study_followed");
            stmt.executeUpdate();
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Streak getStreak(int userId, String streakType) {
        String sql = "SELECT * FROM streaks WHERE user_id = ? AND streak_type = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, streakType);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractStreakFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Streak> getAllStreaks(int userId) {
        String sql = "SELECT * FROM streaks WHERE user_id = ?";
        List<Streak> streaks = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                streaks.add(extractStreakFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return streaks;
    }
    
    public boolean updateStreak(int userId, String streakType, Date activityDate) {
        Streak streak = getStreak(userId, streakType);
        if (streak == null) {
            initializeStreaks(userId);
            streak = getStreak(userId, streakType);
        }
        
        Date lastDate = streak.getLastActivityDate();
        int newCount = streak.getCurrentCount();
        
        if (lastDate == null) {
            newCount = 1;
        } else {
            long daysDiff = (activityDate.getTime() - lastDate.getTime()) / (1000 * 60 * 60 * 24);
            if (daysDiff == 1) {
                newCount++;
            } else if (daysDiff > 1) {
                newCount = 1;
            }
        }
        
        int newBest = Math.max(streak.getBestCount(), newCount);
        
        String sql = "UPDATE streaks SET current_count = ?, best_count = ?, last_activity_date = ? WHERE user_id = ? AND streak_type = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, newCount);
            stmt.setInt(2, newBest);
            stmt.setDate(3, activityDate);
            stmt.setInt(4, userId);
            stmt.setString(5, streakType);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Streak extractStreakFromResultSet(ResultSet rs) throws SQLException {
        Streak streak = new Streak();
        streak.setStreakId(rs.getInt("streak_id"));
        streak.setUserId(rs.getInt("user_id"));
        streak.setStreakType(rs.getString("streak_type"));
        streak.setCurrentCount(rs.getInt("current_count"));
        streak.setBestCount(rs.getInt("best_count"));
        streak.setLastActivityDate(rs.getDate("last_activity_date"));
        streak.setUpdatedAt(rs.getTimestamp("updated_at"));
        return streak;
    }
}
