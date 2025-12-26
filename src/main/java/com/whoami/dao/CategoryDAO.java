package com.whoami.dao;

import com.whoami.model.Category;
import com.whoami.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public boolean createCategory(Category category) {
        String sql = "INSERT INTO task_categories (user_id, category_name, is_custom) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, category.getUserId());
            stmt.setString(2, category.getCategoryName());
            stmt.setBoolean(3, category.isCustom());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Category> getUserCategories(int userId) {
        String sql = "SELECT * FROM task_categories WHERE user_id = ? ORDER BY is_custom ASC, category_name ASC";
        List<Category> categories = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setUserId(rs.getInt("user_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setCustom(rs.getBoolean("is_custom"));
                category.setCreatedAt(rs.getTimestamp("created_at"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return categories;
    }

    public Category getCategoryById(int categoryId, int userId) {
        String sql = "SELECT * FROM task_categories WHERE category_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setUserId(rs.getInt("user_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setCustom(rs.getBoolean("is_custom"));
                category.setCreatedAt(rs.getTimestamp("created_at"));
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public Category getCategoryByName(int userId, String categoryName) {
        String sql = "SELECT * FROM task_categories WHERE user_id = ? AND category_name = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, categoryName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setUserId(rs.getInt("user_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setCustom(rs.getBoolean("is_custom"));
                category.setCreatedAt(rs.getTimestamp("created_at"));
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public boolean deleteCategory(int categoryId, int userId) {
        String sql = "DELETE FROM task_categories WHERE category_id = ? AND user_id = ? AND is_custom = TRUE";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            stmt.setInt(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean categoryExists(int userId, String categoryName) {
        return getCategoryByName(userId, categoryName) != null;
    }

    public void initializeDefaultCategories(int userId) {
        String[] defaultCategories = {
            "Studies", "Religious", "Personal", "Sports", "Entertainment",
            "Social", "Health", "Work", "Outdoor", "Home", "Others"
        };
        
        for (String catName : defaultCategories) {
            if (!categoryExists(userId, catName)) {
                Category category = new Category(userId, catName, false);
                createCategory(category);
            }
        }
    }
}
