package com.whoami.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MotivationalQuotes {

    private static final Map<String, String[]> quotes = new HashMap<>();
    
    static {
        quotes.put("excellent", new String[] {
            "Exceptional focus. You're operating at peak performance.",
            "Your consistency is building unstoppable momentum.",
            "This is mastery in action. Keep this rhythm.",
            "You're not just completing tasks—you're building discipline.",
            "Excellence is a habit, and you're proving it daily."
        });
        
        quotes.put("great", new String[] {
            "Strong progress. You're building powerful habits.",
            "Your dedication is creating real change.",
            "This level of commitment leads to extraordinary results.",
            "You're showing up. That's what separates achievers.",
            "Consistency at this level compounds into success."
        });
        
        quotes.put("good", new String[] {
            "Solid effort. Each completed task is a victory.",
            "You're making meaningful progress today.",
            "Good work. Your future self will thank you.",
            "Every task completed strengthens your discipline.",
            "You're building momentum. Keep going."
        });
        
        quotes.put("fair", new String[] {
            "You've started. That's the hardest part.",
            "Progress over perfection. You're moving forward.",
            "Small steps lead to big transformations.",
            "You showed up. That's what matters.",
            "Every effort counts. Keep building."
        });
        
        quotes.put("needsFocus", new String[] {
            "Today is a fresh opportunity. One task at a time.",
            "Your potential is waiting. Start with one small step.",
            "The best time to begin is now. Choose one task.",
            "Clarity comes through action. Pick your first task.",
            "You have everything you need to start. Begin."
        });
    }

    public static String getQuoteForEfficiency(double efficiency) {
        String category;
        
        if (efficiency >= 80) {
            category = "excellent";
        } else if (efficiency >= 60) {
            category = "great";
        } else if (efficiency >= 40) {
            category = "good";
        } else if (efficiency >= 20) {
            category = "fair";
        } else {
            category = "needsFocus";
        }
        
        String[] categoryQuotes = quotes.get(category);
        Random random = new Random();
        int index = random.nextInt(categoryQuotes.length);
        
        return categoryQuotes[index];
    }

    public static String getRandomQuote() {
        String[] allCategories = {"excellent", "great", "good", "fair", "needsFocus"};
        Random random = new Random();
        String category = allCategories[random.nextInt(allCategories.length)];
        String[] categoryQuotes = quotes.get(category);
        
        return categoryQuotes[random.nextInt(categoryQuotes.length)];
    }
}
