package com.whoami.dao;

import com.whoami.model.LoadAnalysis;
import com.whoami.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoadAnalysisDAO {

    public boolean saveAnalysis(LoadAnalysis analysis) {
        String sql = "INSERT INTO load_analysis (user_id, analysis_date, total_entries, difficulty_score, urgency_score, effort_score, load_score, load_category, insight_message) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE total_entries = ?, difficulty_score = ?, urgency_score = ?, effort_score = ?, load_score = ?, load_category = ?, insight_message = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, analysis.getUserId());
            stmt.setDate(2, Date.valueOf(analysis.getAnalysisDate()));
            stmt.setInt(3, analysis.getTotalEntries());
            stmt.setInt(4, analysis.getDifficultyScore());
            stmt.setInt(5, analysis.getUrgencyScore());
            stmt.setInt(6, analysis.getEffortScore());
            stmt.setInt(7, analysis.getLoadScore());
            stmt.setString(8, analysis.getLoadCategory());
            stmt.setString(9, analysis.getInsightMessage());
            stmt.setInt(10, analysis.getTotalEntries());
            stmt.setInt(11, analysis.getDifficultyScore());
            stmt.setInt(12, analysis.getUrgencyScore());
            stmt.setInt(13, analysis.getEffortScore());
            stmt.setInt(14, analysis.getLoadScore());
            stmt.setString(15, analysis.getLoadCategory());
            stmt.setString(16, analysis.getInsightMessage());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public LoadAnalysis getAnalysisByDate(int userId, LocalDate date) {
        String sql = "SELECT * FROM load_analysis WHERE user_id = ? AND analysis_date = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAnalysis(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LoadAnalysis getTodayAnalysis(int userId) {
        return getAnalysisByDate(userId, LocalDate.now());
    }

    public List<LoadAnalysis> getWeekAnalysis(int userId) {
        List<LoadAnalysis> analyses = new ArrayList<>();
        String sql = "SELECT * FROM load_analysis WHERE user_id = ? AND analysis_date >= ? AND analysis_date <= ? ORDER BY analysis_date DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            LocalDate today = LocalDate.now();
            LocalDate weekAgo = today.minusDays(7);
            
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(weekAgo));
            stmt.setDate(3, Date.valueOf(today));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    analyses.add(mapResultSetToAnalysis(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return analyses;
    }

    private LoadAnalysis mapResultSetToAnalysis(ResultSet rs) throws SQLException {
        LoadAnalysis analysis = new LoadAnalysis();
        analysis.setAnalysisId(rs.getInt("analysis_id"));
        analysis.setUserId(rs.getInt("user_id"));
        analysis.setAnalysisDate(rs.getDate("analysis_date").toLocalDate());
        analysis.setTotalEntries(rs.getInt("total_entries"));
        analysis.setDifficultyScore(rs.getInt("difficulty_score"));
        analysis.setUrgencyScore(rs.getInt("urgency_score"));
        analysis.setEffortScore(rs.getInt("effort_score"));
        analysis.setLoadScore(rs.getInt("load_score"));
        analysis.setLoadCategory(rs.getString("load_category"));
        analysis.setInsightMessage(rs.getString("insight_message"));
        analysis.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return analysis;
    }
}
