package com.whoami.dao;

import com.whoami.model.StudyLoad;
import com.whoami.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudyLoadDAO {

    public boolean addStudyLoad(StudyLoad load) {
        String sql = "INSERT INTO study_load_entries (user_id, subject_name, difficulty, urgency, effort, entry_date) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, load.getUserId());
            stmt.setString(2, load.getSubjectName());
            stmt.setString(3, load.getDifficulty());
            stmt.setString(4, load.getUrgency());
            stmt.setString(5, load.getEffort());
            stmt.setDate(6, Date.valueOf(load.getEntryDate()));
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<StudyLoad> getLoadsByDate(int userId, LocalDate date) {
        List<StudyLoad> loads = new ArrayList<>();
        String sql = "SELECT * FROM study_load_entries WHERE user_id = ? AND entry_date = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    loads.add(mapResultSetToStudyLoad(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loads;
    }

    public List<StudyLoad> getTodayLoads(int userId) {
        return getLoadsByDate(userId, LocalDate.now());
    }

    public List<StudyLoad> getWeekLoads(int userId) {
        List<StudyLoad> loads = new ArrayList<>();
        String sql = "SELECT * FROM study_load_entries WHERE user_id = ? AND entry_date >= ? AND entry_date <= ? ORDER BY entry_date DESC, created_at DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            LocalDate today = LocalDate.now();
            LocalDate weekAgo = today.minusDays(7);
            
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(weekAgo));
            stmt.setDate(3, Date.valueOf(today));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    loads.add(mapResultSetToStudyLoad(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loads;
    }

    public boolean updateStudyLoad(StudyLoad load) {
        String sql = "UPDATE study_load_entries SET subject_name = ?, difficulty = ?, urgency = ?, effort = ?, entry_date = ? WHERE entry_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, load.getSubjectName());
            stmt.setString(2, load.getDifficulty());
            stmt.setString(3, load.getUrgency());
            stmt.setString(4, load.getEffort());
            stmt.setDate(5, Date.valueOf(load.getEntryDate()));
            stmt.setInt(6, load.getEntryId());
            stmt.setInt(7, load.getUserId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudyLoad(int entryId, int userId) {
        String sql = "DELETE FROM study_load_entries WHERE entry_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, entryId);
            stmt.setInt(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getTotalLoadCount(int userId, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM study_load_entries WHERE user_id = ? AND entry_date = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private StudyLoad mapResultSetToStudyLoad(ResultSet rs) throws SQLException {
        StudyLoad load = new StudyLoad();
        load.setEntryId(rs.getInt("entry_id"));
        load.setUserId(rs.getInt("user_id"));
        load.setSubjectName(rs.getString("subject_name"));
        load.setDifficulty(rs.getString("difficulty"));
        load.setUrgency(rs.getString("urgency"));
        load.setEffort(rs.getString("effort"));
        load.setEntryDate(rs.getDate("entry_date").toLocalDate());
        load.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return load;
    }
}
