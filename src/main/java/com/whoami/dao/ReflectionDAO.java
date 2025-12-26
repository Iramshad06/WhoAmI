package com.whoami.dao;

import com.whoami.model.Reflection;
import com.whoami.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReflectionDAO {
    
    public boolean saveReflection(Reflection reflection) {
        String sql = "INSERT INTO reflections (user_id, reflection_date, question, answer) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE answer = VALUES(answer)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, reflection.getUserId());
            stmt.setDate(2, reflection.getReflectionDate());
            stmt.setString(3, reflection.getQuestion());
            stmt.setString(4, reflection.getAnswer());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    reflection.setReflectionId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Reflection getTodayReflection(int userId, Date today) {
        String sql = "SELECT * FROM reflections WHERE user_id = ? AND reflection_date = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setDate(2, today);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractReflectionFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Reflection> getRecentReflections(int userId, int days) {
        String sql = "SELECT * FROM reflections WHERE user_id = ? ORDER BY reflection_date DESC LIMIT ?";
        List<Reflection> reflections = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, days);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reflections.add(extractReflectionFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reflections;
    }
    
    private Reflection extractReflectionFromResultSet(ResultSet rs) throws SQLException {
        Reflection reflection = new Reflection();
        reflection.setReflectionId(rs.getInt("reflection_id"));
        reflection.setUserId(rs.getInt("user_id"));
        reflection.setReflectionDate(rs.getDate("reflection_date"));
        reflection.setQuestion(rs.getString("question"));
        reflection.setAnswer(rs.getString("answer"));
        reflection.setCreatedAt(rs.getTimestamp("created_at"));
        return reflection;
    }
}
