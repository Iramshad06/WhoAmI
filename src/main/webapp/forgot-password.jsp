<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-card fade-in">
            <div class="auth-header">
                <h1>Forgot Password</h1>
                <p>Enter your email to receive a verification code</p>
            </div>
            
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            
            <% if (request.getAttribute("successMessage") != null) { %>
                <div class="alert alert-success">
                    <%= request.getAttribute("successMessage") %>
                </div>
            <% } %>
            
            <form method="post" action="forgot-password">
                <div class="form-group">
                    <label class="form-label" for="email">Email Address</label>
                    <input type="email" id="email" name="email" class="form-input" required 
                           placeholder="you@example.com" autocomplete="email" autofocus>
                </div>
                
                <button type="submit" class="btn btn-primary">Send Verification Code</button>
            </form>
            
            <div class="auth-footer">
                Remember your password? <a href="login">Sign in</a>
            </div>
        </div>
    </div>
    
    <script src="js/theme.js"></script>
</body>
</html>
