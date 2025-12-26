<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-card fade-in">
            <div class="auth-header">
                <h1>Welcome Back</h1>
                <p>Sign in to continue your journey</p>
            </div>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <% if (request.getAttribute("successMessage") != null) { %>
                <div class="alert alert-success">
                    <%= request.getAttribute("successMessage") %>
                </div>
            <% } %>
            
            <form method="post" action="login">
                <div class="form-group">
                    <label class="form-label" for="email">Email Address</label>
                    <input type="email" id="email" name="email" class="form-input" required 
                           placeholder="you@example.com" autocomplete="email">
                </div>
                
                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <div class="password-input-wrapper">
                        <input type="password" id="password" name="password" class="form-input" required 
                               placeholder="Enter your password" autocomplete="current-password">
                        <button type="button" class="password-toggle" onclick="togglePasswordVisibility('password')" aria-label="Toggle password visibility">
                            <svg id="password-icon" class="eye-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                    </div>
                    <div style="text-align: right; margin-top: 8px;">
                        <a href="forgot-password" style="font-size: 14px; color: var(--primary-color); text-decoration: none;">Forgot Password?</a>
                    </div>
                </div>
                
                <button type="submit" class="btn btn-primary">Sign In</button>
            </form>
            
            <div class="auth-footer">
                Don't have an account? <a href="signup">Sign up</a>
            </div>
        </div>
    </div>
    
    <script src="js/theme.js"></script>
    <script src="js/password.js"></script>
</body>
</html>
