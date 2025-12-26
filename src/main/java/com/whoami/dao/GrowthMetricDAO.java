package com.whoami.dao;

import com.whoami.model.GrowthMetric;
import com.whoami.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GrowthMetricDAO {

    public boolean saveMetric(GrowthMetric metric) {
        String sql = "INSERT INTO growth_metrics (user_id, metric_date, mind_score, task_efficiency, study_consistency, load_balance, overall_growth) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE mind_score = ?, task_efficiency = ?, study_consistency = ?, load_balance = ?, overall_growth = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, metric.getUserId());
            stmt.setDate(2, Date.valueOf(metric.getMetricDate()));
            stmt.setDouble(3, metric.getMindScore());
            stmt.setDouble(4, metric.getTaskEfficiency());
            stmt.setDouble(5, metric.getStudyConsistency());
            stmt.setDouble(6, metric.getLoadBalance());
            stmt.setDouble(7, metric.getOverallGrowth());
            stmt.setDouble(8, metric.getMindScore());
            stmt.setDouble(9, metric.getTaskEfficiency());
            stmt.setDouble(10, metric.getStudyConsistency());
            stmt.setDouble(11, metric.getLoadBalance());
            stmt.setDouble(12, metric.getOverallGrowth());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public GrowthMetric getMetricByDate(int userId, LocalDate date) {
        String sql = "SELECT * FROM growth_metrics WHERE user_id = ? AND metric_date = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMetric(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<GrowthMetric> getWeeklyMetrics(int userId) {
        List<GrowthMetric> metrics = new ArrayList<>();
        String sql = "SELECT * FROM growth_metrics WHERE user_id = ? AND metric_date >= ? AND metric_date <= ? ORDER BY metric_date ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            LocalDate today = LocalDate.now();
            LocalDate weekAgo = today.minusDays(6);
            
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(weekAgo));
            stmt.setDate(3, Date.valueOf(today));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    metrics.add(mapResultSetToMetric(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metrics;
    }

    public List<GrowthMetric> getMonthlyMetrics(int userId) {
        List<GrowthMetric> metrics = new ArrayList<>();
        String sql = "SELECT * FROM growth_metrics WHERE user_id = ? AND metric_date >= ? AND metric_date <= ? ORDER BY metric_date ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            LocalDate today = LocalDate.now();
            LocalDate monthAgo = today.minusDays(29);
            
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(monthAgo));
            stmt.setDate(3, Date.valueOf(today));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    metrics.add(mapResultSetToMetric(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metrics;
    }

    public double getAverageGrowth(int userId, int days) {
        String sql = "SELECT AVG(overall_growth) FROM growth_metrics WHERE user_id = ? AND metric_date >= ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            LocalDate startDate = LocalDate.now().minusDays(days - 1);
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(startDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private GrowthMetric mapResultSetToMetric(ResultSet rs) throws SQLException {
        GrowthMetric metric = new GrowthMetric();
        metric.setMetricId(rs.getInt("metric_id"));
        metric.setUserId(rs.getInt("user_id"));
        metric.setMetricDate(rs.getDate("metric_date").toLocalDate());
        metric.setMindScore(rs.getDouble("mind_score"));
        metric.setTaskEfficiency(rs.getDouble("task_efficiency"));
        metric.setStudyConsistency(rs.getDouble("study_consistency"));
        metric.setLoadBalance(rs.getDouble("load_balance"));
        metric.setOverallGrowth(rs.getDouble("overall_growth"));
        metric.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return metric;
    }
}
