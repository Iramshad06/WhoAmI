package com.whoami.dao;

import com.whoami.model.StudySession;
import com.whoami.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudySessionDAO {
    
    public boolean saveStudySession(StudySession session) {
        String sql = "INSERT INTO study_sessions (user_id, log_id, session_date, suggested_activity, suggested_intensity, followed) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, session.getUserId());
            stmt.setInt(2, session.getLogId());
            stmt.setDate(3, session.getSessionDate());
            stmt.setString(4, session.getSuggestedActivity());
            stmt.setString(5, session.getSuggestedIntensity());
            stmt.setBoolean(6, session.isFollowed());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    session.setSessionId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public StudySession getTodaySession(int userId, Date today) {
        String sql = "SELECT * FROM study_sessions WHERE user_id = ? AND session_date = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setDate(2, today);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractStudySessionFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateStudySession(StudySession session) {
        String sql = "UPDATE study_sessions SET suggested_activity = ?, suggested_intensity = ?, followed = ? WHERE session_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, session.getSuggestedActivity());
            stmt.setString(2, session.getSuggestedIntensity());
            stmt.setBoolean(3, session.isFollowed());
            stmt.setInt(4, session.getSessionId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean markAsFollowed(int sessionId, boolean followed) {
        String sql = "UPDATE study_sessions SET followed = ? WHERE session_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, followed);
            stmt.setInt(2, sessionId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<StudySession> getRecentSessions(int userId, int days) {
        String sql = "SELECT * FROM study_sessions WHERE user_id = ? ORDER BY session_date DESC LIMIT ?";
        List<StudySession> sessions = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, days);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sessions.add(extractStudySessionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sessions;
    }
    
    private StudySession extractStudySessionFromResultSet(ResultSet rs) throws SQLException {
        StudySession session = new StudySession();
        session.setSessionId(rs.getInt("session_id"));
        session.setUserId(rs.getInt("user_id"));
        session.setLogId(rs.getInt("log_id"));
        session.setSessionDate(rs.getDate("session_date"));
        session.setSuggestedActivity(rs.getString("suggested_activity"));
        session.setSuggestedIntensity(rs.getString("suggested_intensity"));
        session.setFollowed(rs.getBoolean("followed"));
        session.setCreatedAt(rs.getTimestamp("created_at"));
        return session;
    }
}
