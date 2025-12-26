package com.whoami.servlet;

import com.whoami.dao.*;
import com.whoami.model.*;
import com.whoami.util.MindScoreCalculator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet("/mind-checkin")
public class MindCheckInServlet extends HttpServlet {
    private MindLogDAO mindLogDAO;
    private StudySessionDAO studySessionDAO;
    private StreakDAO streakDAO;
    
    @Override
    public void init() {
        mindLogDAO = new MindLogDAO();
        studySessionDAO = new StudySessionDAO();
        streakDAO = new StreakDAO();
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
        
        MindLog existingLog = mindLogDAO.getTodayLog(userId, today);
        request.setAttribute("existingLog", existingLog);
        
        request.getRequestDispatcher("mind-checkin.jsp").forward(request, response);
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
        
        int focus = Integer.parseInt(request.getParameter("focus"));
        int energy = Integer.parseInt(request.getParameter("energy"));
        int emotional = Integer.parseInt(request.getParameter("emotional"));
        int clarity = Integer.parseInt(request.getParameter("clarity"));
        int stress = Integer.parseInt(request.getParameter("stress"));
        
        double mindScore = MindScoreCalculator.calculateScore(focus, energy, emotional, clarity, stress);
        String mindState = MindScoreCalculator.getMindState(mindScore);
        
        // Check if there's already a log for today
        MindLog existingLog = mindLogDAO.getTodayLog(userId, today);
        boolean success = false;
        int logId = 0;
        
        if (existingLog != null) {
            // Update existing log
            existingLog.setFocusLevel(focus);
            existingLog.setEnergyLevel(energy);
            existingLog.setEmotionalLoad(emotional);
            existingLog.setClarityLevel(clarity);
            existingLog.setStressLevel(stress);
            existingLog.setMindScore(mindScore);
            existingLog.setMindState(mindState);
            
            success = mindLogDAO.saveMindLog(existingLog); // This will update due to ON DUPLICATE KEY
            logId = existingLog.getLogId();
        } else {
            // Create new log
            MindLog log = new MindLog(userId, today, focus, energy, emotional, clarity, stress, mindScore, mindState);
            success = mindLogDAO.saveMindLog(log);
            logId = log.getLogId();
        }
        
        if (success && logId > 0) {
            streakDAO.updateStreak(userId, "mind_checkin", today);
            
            String activity = getSuggestedActivity(focus, energy, emotional, clarity, stress);
            String intensity = getSuggestedIntensity(focus, energy, emotional, clarity, stress);
            
            // Check if there's already a study session for today
            StudySession existingSession = studySessionDAO.getTodaySession(userId, today);
            if (existingSession != null) {
                // Update existing session
                existingSession.setSuggestedActivity(activity);
                existingSession.setSuggestedIntensity(intensity);
                studySessionDAO.updateStudySession(existingSession);
            } else {
                // Create new session
                StudySession session2 = new StudySession(userId, logId, today, activity, intensity);
                studySessionDAO.saveStudySession(session2);
            }
            
            response.sendRedirect("study-zone");
        } else {
            request.setAttribute("error", "Failed to save check-in. Please try again.");
            request.getRequestDispatcher("mind-checkin.jsp").forward(request, response);
        }
    }
    
    private String getSuggestedActivity(int focus, int energy, int emotional, int clarity, int stress) {
        // Create a unique combination key
        String combinationKey = focus + "," + energy + "," + emotional + "," + clarity + "," + stress;
        
        // Use a deterministic approach to generate unique recommendations based on the combination
        int hash = combinationKey.hashCode();
        int activityIndex = Math.abs(hash) % 125; // 5^3 = 125 possible activity combinations
        
        // Determine primary focus areas based on mental state values
        boolean highFocus = focus >= 4;
        boolean highEnergy = energy >= 4;
        boolean lowEmotional = emotional <= 2;
        boolean highClarity = clarity >= 4;
        boolean lowStress = stress <= 2;
        
        // Generate activity based on mental state combination
        if (highFocus && highEnergy && lowEmotional && highClarity && lowStress) {
            // Peak mental state - challenging activities
            String[] peakActivities = {
                "Tackle advanced calculus problems or complex mathematical proofs",
                "Engage in deep philosophical analysis or theoretical computer science",
                "Compose original music or work on advanced musical composition",
                "Design complex software architecture or system engineering",
                "Conduct advanced research in quantum physics or theoretical concepts"
            };
            return peakActivities[activityIndex % peakActivities.length];
        } else if (highFocus && highEnergy && highClarity) {
            // Strong cognitive performance
            String[] strongActivities = {
                "Implement complex algorithms or data structures",
                "Write technical documentation for advanced systems",
                "Debug complex software systems or performance optimization",
                "Learn advanced foreign language grammar and syntax",
                "Master advanced statistical analysis techniques"
            };
            return strongActivities[activityIndex % strongActivities.length];
        } else if (lowEmotional && lowStress && (highFocus || highClarity)) {
            // Calm and focused
            String[] calmActivities = {
                "Practice mindfulness meditation while studying zen philosophy",
                "Engage in contemplative reading of classical literature",
                "Study ancient history and archaeological findings",
                "Learn about sustainable architecture and green design",
                "Explore marine biology and ocean ecosystem studies"
            };
            return calmActivities[activityIndex % calmActivities.length];
        } else if (highEnergy && (focus >= 3 || clarity >= 3)) {
            // Energetic with decent focus
            String[] energeticActivities = {
                "Participate in interactive physics simulations and experiments",
                "Create dynamic presentations on emerging technologies",
                "Engage in collaborative problem-solving workshops",
                "Develop mobile applications with innovative features",
                "Explore virtual reality programming and 3D modeling"
            };
            return energeticActivities[activityIndex % energeticActivities.length];
        } else if (focus <= 2 && energy <= 2 && clarity <= 2) {
            // Low cognitive performance
            String[] lowActivities = {
                "Listen to educational podcasts on basic concepts",
                "Watch short documentary clips on historical events",
                "Review basic vocabulary with audio flashcards",
                "Browse illustrated children's books on science topics",
                "Play educational games teaching fundamental concepts"
            };
            return lowActivities[activityIndex % lowActivities.length];
        } else if (emotional >= 4 || stress >= 4) {
            // High emotional load or stress
            String[] stressActivities = {
                "Practice gentle yoga while listening to calming nature sounds",
                "Engage in light gardening or plant care activities",
                "Create vision boards for future goals and aspirations",
                "Write in a gratitude journal with positive affirmations",
                "Practice slow, mindful breathing exercises with study breaks"
            };
            return stressActivities[activityIndex % stressActivities.length];
        } else if (focus >= 3 && clarity >= 3) {
            // Decent focus and clarity
            String[] balancedActivities = {
                "Review lecture notes with concept mapping techniques",
                "Practice coding exercises with immediate feedback",
                "Study art history through virtual museum tours",
                "Learn basic astronomy through stargazing guides",
                "Explore basic chemistry through kitchen experiments"
            };
            return balancedActivities[activityIndex % balancedActivities.length];
        } else if (energy >= 3 && emotional <= 3) {
            // Moderate energy, manageable emotions
            String[] moderateActivities = {
                "Organize study materials and create summary sheets",
                "Participate in online discussion forums on academic topics",
                "Create mind maps connecting different subject areas",
                "Practice language exchange through simple conversations",
                "Explore basic economics through real-world examples"
            };
            return moderateActivities[activityIndex % moderateActivities.length];
        } else {
            // Mixed or average mental state
            String[] mixedActivities = {
                "Browse educational YouTube channels for interesting topics",
                "Read popular science articles on current discoveries",
                "Explore basic geography through interactive maps",
                "Learn simple cooking techniques from different cultures",
                "Practice basic photography composition principles"
            };
            return mixedActivities[activityIndex % mixedActivities.length];
        }
    }
    
    private String getSuggestedIntensity(int focus, int energy, int emotional, int clarity, int stress) {
        // Calculate intensity based on all mental state factors
        double focusFactor = focus / 5.0;
        double energyFactor = energy / 5.0;
        double clarityFactor = clarity / 5.0;
        double stressFactor = (6 - stress) / 5.0; // Invert stress (lower stress = higher factor)
        double emotionalFactor = (6 - emotional) / 5.0; // Invert emotional load
        
        // Weighted intensity score
        double intensityScore = (focusFactor * 0.25) + (energyFactor * 0.25) + 
                               (clarityFactor * 0.25) + (stressFactor * 0.15) + 
                               (emotionalFactor * 0.10);
        
        // Generate unique intensity recommendations based on combination
        String combinationKey = focus + "," + energy + "," + emotional + "," + clarity + "," + stress;
        int hash = Math.abs(combinationKey.hashCode());
        int intensityVariation = hash % 3; // 3 variations per intensity level
        
        if (intensityScore >= 0.8) {
            String[] highIntensities = {
                "90-120 min sessions with 10-min breaks",
                "75-min sessions with strategic 5-min pauses",
                "100-min immersive sessions with brief rests"
            };
            return highIntensities[intensityVariation];
        } else if (intensityScore >= 0.6) {
            String[] moderateIntensities = {
                "50-min sessions with 10-min breaks",
                "45-min sessions with 15-min transitions",
                "60-min sessions with 5-min mindfulness breaks"
            };
            return moderateIntensities[intensityVariation];
        } else if (intensityScore >= 0.4) {
            String[] lightIntensities = {
                "30-min sessions with ample breaks",
                "25-min sessions with 20-min rests",
                "35-min sessions with frequent 10-min pauses"
            };
            return lightIntensities[intensityVariation];
        } else if (intensityScore >= 0.2) {
            String[] veryLightIntensities = {
                "15-min sessions with extended breaks",
                "20-min sessions with 30-min rests",
                "10-min sessions with 20-min recovery"
            };
            return veryLightIntensities[intensityVariation];
        } else {
            String[] restIntensities = {
                "5-min activities with long breaks",
                "10-min activities with extended rest",
                "15-min sessions with 45-min restorative breaks"
            };
            return restIntensities[intensityVariation];
        }
    }
}
