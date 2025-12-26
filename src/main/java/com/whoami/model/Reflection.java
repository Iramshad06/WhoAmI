package com.whoami.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Reflection {
    private int reflectionId;
    private int userId;
    private Date reflectionDate;
    private String question;
    private String answer;
    private Timestamp createdAt;
    
    public Reflection() {}
    
    public Reflection(int userId, Date reflectionDate, String question, String answer) {
        this.userId = userId;
        this.reflectionDate = reflectionDate;
        this.question = question;
        this.answer = answer;
    }
    
    public int getReflectionId() {
        return reflectionId;
    }
    
    public void setReflectionId(int reflectionId) {
        this.reflectionId = reflectionId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public Date getReflectionDate() {
        return reflectionDate;
    }
    
    public void setReflectionDate(Date reflectionDate) {
        this.reflectionDate = reflectionDate;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
