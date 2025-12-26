package com.whoami.model;

import java.sql.Timestamp;

public class Category {
    private int categoryId;
    private int userId;
    private String categoryName;
    private boolean isCustom;
    private Timestamp createdAt;

    public Category() {}

    public Category(int userId, String categoryName, boolean isCustom) {
        this.userId = userId;
        this.categoryName = categoryName;
        this.isCustom = isCustom;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getIcon() {
        switch(categoryName.toLowerCase()) {
            case "studies": return "ST";
            case "religious": return "RE";
            case "personal": return "PE";
            case "sports": return "SP";
            case "entertainment": return "EN";
            case "social": return "SO";
            case "health": return "HE";
            case "work": return "WO";
            case "outdoor": return "OU";
            case "home": return "HO";
            default: return "OT";
        }
    }
    
    public String getColor() {
        switch(categoryName.toLowerCase()) {
            case "studies": return "#667eea";
            case "religious": return "#48bb78";
            case "personal": return "#ed8936";
            case "sports": return "#f56565";
            case "entertainment": return "#9f7aea";
            case "social": return "#38b2ac";
            case "health": return "#fc8181";
            case "work": return "#4299e1";
            case "outdoor": return "#68d391";
            case "home": return "#f6ad55";
            default: return "#718096";
        }
    }
}
