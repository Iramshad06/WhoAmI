package com.whoami.dao;

import com.whoami.model.Task;
import com.whoami.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public boolean createTask(Task task) {
        String sql = "INSERT INTO tasks (user_id, category_id, title, description, task_type, task_date, task_time, is_completed) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, task.getUserId());
            stmt.setInt(2, task.getCategoryId());
            stmt.setString(3, task.getTitle());
            stmt.setString(4, task.getDescription());
            stmt.setString(5, task.getTaskType());
            stmt.setDate(6, task.getTaskDate());
            stmt.setTime(7, task.getTaskTime());
            stmt.setBoolean(8, task.isCompleted());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTask(Task task) {
        String sql = "UPDATE tasks SET category_id = ?, title = ?, description = ?, task_type = ?, task_date = ?, task_time = ? WHERE task_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, task.getCategoryId());
            stmt.setString(2, task.getTitle());
            stmt.setString(3, task.getDescription());
            stmt.setString(4, task.getTaskType());
            stmt.setDate(5, task.getTaskDate());
            stmt.setTime(6, task.getTaskTime());
            stmt.setInt(7, task.getTaskId());
            stmt.setInt(8, task.getUserId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean completeTask(int taskId, int userId) {
        String sql = "UPDATE tasks SET is_completed = TRUE, completed_at = CURRENT_TIMESTAMP WHERE task_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, taskId);
            stmt.setInt(2, userId);
            
            if (stmt.executeUpdate() > 0) {
                logTaskCompletion(taskId, userId);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean uncompleteTask(int taskId, int userId) {
        String sql = "UPDATE tasks SET is_completed = FALSE, completed_at = NULL WHERE task_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, taskId);
            stmt.setInt(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void logTaskCompletion(int taskId, int userId) {
        String sql = "INSERT INTO task_completion_logs (task_id, user_id, completion_date, completion_time) VALUES (?, ?, CURRENT_DATE, CURRENT_TIME)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, taskId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteTask(int taskId, int userId) {
        String sql = "DELETE FROM tasks WHERE task_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, taskId);
            stmt.setInt(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Task> getTodayTasks(int userId) {
        String sql = "SELECT t.*, c.category_name FROM tasks t " +
                    "JOIN task_categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? AND t.task_date = CURRENT_DATE " +
                    "ORDER BY t.is_completed ASC, t.task_time ASC, t.created_at ASC";
        
        return getTasksByQuery(sql, userId);
    }

    public List<Task> getTasksForDate(int userId, java.sql.Date date) {
        String sql = "SELECT t.*, c.category_name FROM tasks t " +
                    "JOIN task_categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? AND t.task_date = ? " +
                    "ORDER BY t.is_completed ASC, t.task_time ASC, t.created_at ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setDate(2, date);
            
            return executeTaskQuery(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Task> getUpcomingTasks(int userId) {
        String sql = "SELECT t.*, c.category_name FROM tasks t " +
                    "JOIN task_categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? AND t.task_date > CURRENT_DATE AND t.is_completed = FALSE " +
                    "ORDER BY t.task_date ASC, t.task_time ASC";
        
        return getTasksByQuery(sql, userId);
    }

    public List<Task> getCompletedTasks(int userId, int limit) {
        String sql = "SELECT t.*, c.category_name FROM tasks t " +
                    "JOIN task_categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? AND t.is_completed = TRUE " +
                    "ORDER BY t.completed_at DESC LIMIT ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, limit);
            
            return executeTaskQuery(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Task> getTasksByCategory(int userId, int categoryId) {
        String sql = "SELECT t.*, c.category_name FROM tasks t " +
                    "JOIN task_categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? AND t.category_id = ? AND t.is_completed = FALSE " +
                    "ORDER BY t.task_date ASC, t.task_time ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, categoryId);
            
            return executeTaskQuery(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Task> getOverdueTasks(int userId) {
        String sql = "SELECT t.*, c.category_name FROM tasks t " +
                    "JOIN task_categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? AND t.task_date < CURRENT_DATE AND t.is_completed = FALSE " +
                    "ORDER BY t.task_date ASC";
        
        return getTasksByQuery(sql, userId);
    }

    public List<Task> getRoutineTasks(int userId) {
        String sql = "SELECT t.*, c.category_name FROM tasks t " +
                    "JOIN task_categories c ON t.category_id = c.category_id " +
                    "WHERE t.user_id = ? AND t.task_type = 'ROUTINE' " +
                    "ORDER BY t.created_at ASC";
        
        return getTasksByQuery(sql, userId);
    }

    public Task getTaskById(int taskId, int userId) {
        String sql = "SELECT t.*, c.category_name FROM tasks t " +
                    "JOIN task_categories c ON t.category_id = c.category_id " +
                    "WHERE t.task_id = ? AND t.user_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, taskId);
            stmt.setInt(2, userId);
            
            List<Task> tasks = executeTaskQuery(stmt);
            return tasks.isEmpty() ? null : tasks.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean resetRoutineTasks(int userId) {
        String sql = "UPDATE tasks SET is_completed = FALSE, completed_at = NULL " +
                    "WHERE user_id = ? AND task_type = 'ROUTINE' AND task_date = CURRENT_DATE";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getTodayTaskCount(int userId) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ? AND task_date = CURRENT_DATE";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTodayCompletedCount(int userId) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ? AND task_date = CURRENT_DATE AND is_completed = TRUE";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getWeeklyTaskCount(int userId) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ? AND task_date >= DATEADD('DAY', -7, CURRENT_DATE)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getWeeklyCompletedCount(int userId) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ? AND task_date >= DATEADD('DAY', -7, CURRENT_DATE) AND is_completed = TRUE";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private List<Task> getTasksByQuery(String sql, int userId) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            return executeTaskQuery(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<Task> executeTaskQuery(PreparedStatement stmt) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Task task = new Task();
            task.setTaskId(rs.getInt("task_id"));
            task.setUserId(rs.getInt("user_id"));
            task.setCategoryId(rs.getInt("category_id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setTaskType(rs.getString("task_type"));
            task.setTaskDate(rs.getDate("task_date"));
            task.setTaskTime(rs.getTime("task_time"));
            task.setCompleted(rs.getBoolean("is_completed"));
            task.setCreatedAt(rs.getTimestamp("created_at"));
            task.setCompletedAt(rs.getTimestamp("completed_at"));
            task.setCategoryName(rs.getString("category_name"));
            tasks.add(task);
        }
        
        return tasks;
    }
}
