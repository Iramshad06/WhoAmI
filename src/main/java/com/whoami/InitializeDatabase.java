package com.whoami;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class InitializeDatabase {
    public static void main(String[] args) {
        String url = "jdbc:h2:./data/whoami;AUTO_SERVER=TRUE;MODE=MySQL";
        String user = "sa";
        String password = "";
        
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            
            System.out.println("Creating database schema...");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                "email VARCHAR(255) UNIQUE NOT NULL," +
                "password_hash VARCHAR(255) NOT NULL," +
                "full_name VARCHAR(100) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "last_login TIMESTAMP NULL," +
                "theme_preference VARCHAR(10) DEFAULT 'light')");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS mind_logs (" +
                "log_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "log_date DATE NOT NULL," +
                "focus_level INT NOT NULL," +
                "energy_level INT NOT NULL," +
                "emotional_load INT NOT NULL," +
                "clarity_level INT NOT NULL," +
                "stress_level INT NOT NULL," +
                "mind_score DECIMAL(5,2) NOT NULL," +
                "mind_state VARCHAR(50) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_user_date ON mind_logs(user_id, log_date)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS study_sessions (" +
                "session_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "log_id INT NOT NULL," +
                "session_date DATE NOT NULL," +
                "suggested_activity VARCHAR(100) NOT NULL," +
                "suggested_intensity VARCHAR(50) NOT NULL," +
                "followed BOOLEAN DEFAULT FALSE," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE," +
                "FOREIGN KEY (log_id) REFERENCES mind_logs(log_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS streaks (" +
                "streak_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "streak_type VARCHAR(50) NOT NULL," +
                "current_count INT DEFAULT 0," +
                "best_count INT DEFAULT 0," +
                "last_activity_date DATE NULL," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_user_streak ON streaks(user_id, streak_type)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS reflections (" +
                "reflection_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "reflection_date DATE NOT NULL," +
                "question TEXT NOT NULL," +
                "answer TEXT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_user_reflection ON reflections(user_id, reflection_date)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS insights (" +
                "insight_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "insight_period VARCHAR(20) NOT NULL," +
                "period_start DATE NOT NULL," +
                "period_end DATE NOT NULL," +
                "avg_focus DECIMAL(5,2)," +
                "avg_energy DECIMAL(5,2)," +
                "avg_stress DECIMAL(5,2)," +
                "best_day VARCHAR(20)," +
                "insight_text TEXT NOT NULL," +
                "generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS task_categories (" +
                "category_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "category_name VARCHAR(50) NOT NULL," +
                "is_custom BOOLEAN DEFAULT FALSE," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_user_category ON task_categories(user_id, category_name)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS tasks (" +
                "task_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "category_id INT NOT NULL," +
                "title VARCHAR(200) NOT NULL," +
                "description TEXT," +
                "task_type VARCHAR(20) NOT NULL," +
                "task_date DATE," +
                "task_time TIME," +
                "is_completed BOOLEAN DEFAULT FALSE," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "completed_at TIMESTAMP NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE," +
                "FOREIGN KEY (category_id) REFERENCES task_categories(category_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_user_date ON tasks(user_id, task_date)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_user_type ON tasks(user_id, task_type)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_user_completed ON tasks(user_id, is_completed)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS habits (" +
                "habit_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "category_id INT NOT NULL," +
                "title VARCHAR(200) NOT NULL," +
                "description TEXT," +
                "frequency VARCHAR(20) NOT NULL," +
                "current_streak INT DEFAULT 0," +
                "best_streak INT DEFAULT 0," +
                "total_completions INT DEFAULT 0," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "is_active BOOLEAN DEFAULT TRUE," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE," +
                "FOREIGN KEY (category_id) REFERENCES task_categories(category_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_habit_user ON habits(user_id, is_active)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS habit_completions (" +
                "completion_id INT AUTO_INCREMENT PRIMARY KEY," +
                "habit_id INT NOT NULL," +
                "user_id INT NOT NULL," +
                "completion_date DATE NOT NULL," +
                "completion_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (habit_id) REFERENCES habits(habit_id) ON DELETE CASCADE," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_habit_date ON habit_completions(habit_id, completion_date)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_completion_user_date ON habit_completions(user_id, completion_date)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS events (" +
                "event_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "category_id INT NOT NULL," +
                "title VARCHAR(200) NOT NULL," +
                "description TEXT," +
                "event_date DATE NOT NULL," +
                "event_time TIME," +
                "is_completed BOOLEAN DEFAULT FALSE," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE," +
                "FOREIGN KEY (category_id) REFERENCES task_categories(category_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_event_user_date ON events(user_id, event_date)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS task_completion_logs (" +
                "log_id INT AUTO_INCREMENT PRIMARY KEY," +
                "task_id INT NOT NULL," +
                "user_id INT NOT NULL," +
                "completion_date DATE NOT NULL," +
                "completion_time TIME NOT NULL," +
                "efficiency_score DECIMAL(5,2)," +
                "FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_user_completion_date ON task_completion_logs(user_id, completion_date)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS study_load_entries (" +
                "entry_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "subject_name VARCHAR(200) NOT NULL," +
                "difficulty VARCHAR(20) NOT NULL," +
                "urgency VARCHAR(20) NOT NULL," +
                "effort VARCHAR(20) NOT NULL," +
                "entry_date DATE NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_load_entries_user_date ON study_load_entries(user_id, entry_date)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS load_analysis (" +
                "analysis_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "analysis_date DATE NOT NULL," +
                "total_entries INT DEFAULT 0," +
                "difficulty_score INT DEFAULT 0," +
                "urgency_score INT DEFAULT 0," +
                "effort_score INT DEFAULT 0," +
                "load_score INT DEFAULT 0," +
                "load_category VARCHAR(20)," +
                "insight_message TEXT," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_load_analysis_user_date ON load_analysis(user_id, analysis_date)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS growth_metrics (" +
                "metric_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "metric_date DATE NOT NULL," +
                "mind_score DECIMAL(5,2) DEFAULT 0," +
                "task_efficiency DECIMAL(5,2) DEFAULT 0," +
                "study_consistency DECIMAL(5,2) DEFAULT 0," +
                "load_balance DECIMAL(5,2) DEFAULT 0," +
                "overall_growth DECIMAL(5,2) DEFAULT 0," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_growth_metrics_user_date ON growth_metrics(user_id, metric_date)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS password_reset_otps (" +
                "otp_id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "otp_code VARCHAR(10) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "expires_at TIMESTAMP NOT NULL," +
                "is_used BOOLEAN DEFAULT FALSE," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)");
            
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_otp_user ON password_reset_otps(user_id, is_used)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_otp_expiry ON password_reset_otps(expires_at)");
            
            System.out.println("✓ Database schema created successfully!");
            
            System.out.println("Creating demo user...");
            stmt.execute("INSERT INTO users (email, password_hash, full_name) VALUES " +
                "('demo@whoami.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Demo User')");
            
            System.out.println("Creating default task categories...");
            String[] categories = {"Studies", "Religious", "Personal", "Sports", "Entertainment", "Social", "Health", "Work", "Outdoor", "Home", "Others"};
            for (String cat : categories) {
                stmt.execute("INSERT INTO task_categories (user_id, category_name, is_custom) " +
                    "SELECT 1, '" + cat + "', FALSE WHERE NOT EXISTS " +
                    "(SELECT 1 FROM task_categories WHERE user_id = 1 AND category_name = '" + cat + "')");
            }
            
            System.out.println("✓ Demo user created!");
            System.out.println("✓ Task categories initialized!");
            System.out.println("\nDemo Login:");
            System.out.println("  Email: demo@whoami.com");
            System.out.println("  Password: password");
            
            stmt.close();
            conn.close();
            
            System.out.println("\n========================================");
            System.out.println("Database initialization complete!");
            System.out.println("========================================");
            
        } catch (Exception e) {
            System.out.println("Note: " + e.getMessage());
        }
    }
}
