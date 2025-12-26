<%@ page import="com.whoami.model.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reflection - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <% 
        if (session.getAttribute("userId") == null) {
            response.sendRedirect("login");
            return;
        }
        String userName = (String) session.getAttribute("userName");
        String question = (String) request.getAttribute("question");
        Reflection existingReflection = (Reflection) request.getAttribute("existingReflection");
        String successMsg = (String) request.getAttribute("success");
        String errorMsg = (String) request.getAttribute("error");
    %>
    
    <jsp:include page="WEB-INF/navbar.jsp">
        <jsp:param name="activePage" value="reflection" />
        <jsp:param name="activeSection" value="growth" />
        <jsp:param name="userName" value="<%= userName %>" />
    </jsp:include>
    
    <main class="main-content">
        <div class="container">
            <div class="page-header">
                <h1 class="page-title">Daily Reflection</h1>
                <p class="page-subtitle">Take a moment to reflect on your day</p>
            </div>
            
            <div style="max-width: 800px; margin: 0 auto;">
                <% if (successMsg != null) { %>
                    <div class="alert alert-success"><%= successMsg %></div>
                <% } %>
                
                <% if (errorMsg != null) { %>
                    <div class="alert alert-error"><%= errorMsg %></div>
                <% } %>
                
                <div class="reflection-question">
                    <h2><%= question %></h2>
                </div>
                
                <div class="card">
                    <form method="post" action="reflection">
                        <input type="hidden" name="question" value="<%= question %>">
                        
                        <div class="form-group">
                            <label class="form-label" for="answer">Your Reflection</label>
                            <textarea id="answer" name="answer" class="reflection-textarea" 
                                      placeholder="Write your thoughts here... Be honest with yourself."
                                      <%= existingReflection != null ? "readonly" : "" %>><%= existingReflection != null ? existingReflection.getAnswer() : "" %></textarea>
                        </div>
                        
                        <% if (existingReflection == null) { %>
                            <button type="submit" class="btn btn-primary" style="width: 100%;">
                                Save Reflection
                            </button>
                        <% } else { %>
                            <div class="alert alert-info">
                                You've already reflected today. Come back tomorrow for a new question!
                            </div>
                            <a href="dashboard" class="btn btn-primary" style="width: 100%; text-decoration: none; display: block; text-align: center;">
                                Back to Dashboard
                            </a>
                        <% } %>
                    </form>
                </div>
                
                <div class="card" style="background: var(--bg-secondary);">
                    <h4 style="font-size: 16px; font-weight: 600; margin-bottom: 12px; color: var(--text-primary);">Why Reflect?</h4>
                    <p style="color: var(--text-secondary); line-height: 1.7;">
                        Daily reflection helps you build self-awareness, process emotions, and track personal growth. 
                        Just a few minutes each day can lead to profound insights about yourself and your journey.
                    </p>
                </div>
            </div>
        </div>
    </main>
    
    <script src="js/theme.js"></script>
</body>
</html>
