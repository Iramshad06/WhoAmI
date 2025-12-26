package com.whoami.dao;

import com.whoami.model.MindLog;
import com.whoami.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MindLogDAO {
    
    public boolean saveMindLog(MindLog log) {
        String sql = "INSERT INTO mind_logs (user_id, log_date, focus_level, energy_level, emotional_load, clarity_level, stress_level, mind_score, mind_state) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE focus_level = VALUES(focus_level), energy_level = VALUES(energy_level), emotional_load = VALUES(emotional_load), clarity_level = VALUES(clarity_level), stress_level = VALUES(stress_level), mind_score = VALUES(mind_score), mind_state = VALUES(mind_state)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, log.getUserId());
            stmt.setDate(2, log.getLogDate());
            stmt.setInt(3, log.getFocusLevel());
            stmt.setInt(4, log.getEnergyLevel());
            stmt.setInt(5, log.getEmotionalLoad());
            stmt.setInt(6, log.getClarityLevel());
            stmt.setInt(7, log.getStressLevel());
            stmt.setDouble(8, log.getMindScore());
            stmt.setString(9, log.getMindState());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    log.setLogId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public MindLog getTodayLog(int userId, Date today) {
        String sql = "SELECT * FROM mind_logs WHERE user_id = ? AND log_date = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setDate(2, today);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractMindLogFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<MindLog> getRecentLogs(int userId, int days) {
        String sql = "SELECT * FROM mind_logs WHERE user_id = ? ORDER BY log_date DESC LIMIT ?";
        List<MindLog> logs = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, days);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                logs.add(extractMindLogFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
    
    public List<MindLog> getLogsBetweenDates(int userId, Date startDate, Date endDate) {
        String sql = "SELECT * FROM mind_logs WHERE user_id = ? AND log_date BETWEEN ? AND ? ORDER BY log_date DESC";
        List<MindLog> logs = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                logs.add(extractMindLogFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
    
    public MindLog getLogById(int logId) {
        String sql = "SELECT * FROM mind_logs WHERE log_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, logId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractMindLogFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private MindLog extractMindLogFromResultSet(ResultSet rs) throws SQLException {
        MindLog log = new MindLog();
        log.setLogId(rs.getInt("log_id"));
        log.setUserId(rs.getInt("user_id"));
        log.setLogDate(rs.getDate("log_date"));
        log.setFocusLevel(rs.getInt("focus_level"));
        log.setEnergyLevel(rs.getInt("energy_level"));
        log.setEmotionalLoad(rs.getInt("emotional_load"));
        log.setClarityLevel(rs.getInt("clarity_level"));
        log.setStressLevel(rs.getInt("stress_level"));
        log.setMindScore(rs.getDouble("mind_score"));
        log.setMindState(rs.getString("mind_state"));
        log.setCreatedAt(rs.getTimestamp("created_at"));
        return log;
    }
}
