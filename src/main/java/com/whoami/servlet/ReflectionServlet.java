package com.whoami.servlet;

import com.whoami.dao.*;
import com.whoami.model.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

@WebServlet("/reflection")
public class ReflectionServlet extends HttpServlet {
    private ReflectionDAO reflectionDAO;
    private static final String[] QUESTIONS = {
        "What is one thing you learned about yourself today?",
        "What made you feel most alive or energized today?",
        "If today was a chapter in a book, what would you title it?",
        "What is one small win you achieved today?",
        "What would you do differently if you could replay today?",
        "Who or what are you grateful for today?",
        "What emotion did you experience most today, and why?",
        "What is one thing you're looking forward to tomorrow?",
        "How did you take care of yourself today?",
        "What challenged you today, and what did it teach you?"
    };
    
    @Override
    public void init() {
        reflectionDAO = new ReflectionDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        Date today = Date.valueOf(LocalDate.now());
        
        Reflection existingReflection = reflectionDAO.getTodayReflection(userId, today);
        
        String question;
        if (existingReflection != null) {
            question = existingReflection.getQuestion();
            request.setAttribute("existingReflection", existingReflection);
        } else {
            Random random = new Random();
            question = QUESTIONS[random.nextInt(QUESTIONS.length)];
        }
        
        request.setAttribute("question", question);
        request.getRequestDispatcher("reflection.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        Date today = Date.valueOf(LocalDate.now());
        
        String question = request.getParameter("question");
        String answer = request.getParameter("answer");
        
        if (answer == null || answer.trim().isEmpty()) {
            request.setAttribute("error", "Please write your reflection before saving.");
            request.setAttribute("question", question);
            request.getRequestDispatcher("reflection.jsp").forward(request, response);
            return;
        }
        
        Reflection reflection = new Reflection(userId, today, question, answer);
        boolean saved = reflectionDAO.saveReflection(reflection);
        
        if (saved) {
            request.setAttribute("success", "Your reflection has been saved.");
            request.setAttribute("question", question);
            request.setAttribute("existingReflection", reflection);
            request.getRequestDispatcher("reflection.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Failed to save reflection. Please try again.");
            request.setAttribute("question", question);
            request.getRequestDispatcher("reflection.jsp").forward(request, response);
        }
    }
}
