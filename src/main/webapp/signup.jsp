<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-card fade-in">
            <div class="auth-header">
                <h1>Create Account</h1>
                <p>Start your journey to better productivity</p>
            </div>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <form method="post" action="signup">
                <div class="form-group">
                    <label class="form-label" for="fullName">Full Name</label>
                    <input type="text" id="fullName" name="fullName" class="form-input" required 
                           placeholder="Enter your full name" autocomplete="name">
                </div>
                
                <div class="form-group">
                    <label class="form-label" for="email">Email Address</label>
                    <input type="email" id="email" name="email" class="form-input" required 
                           placeholder="you@example.com" autocomplete="email">
                </div>
                
                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <div class="password-input-wrapper">
                        <input type="password" id="password" name="password" class="form-input" required 
                               placeholder="Create a password" autocomplete="new-password" minlength="6" oninput="checkPasswordStrength()">
                        <button type="button" class="password-toggle" onclick="togglePasswordVisibility('password')" aria-label="Toggle password visibility">
                            <svg id="password-icon" class="eye-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                    </div>
                    <div id="password-strength" class="password-strength" style="display: none;">
                        <div class="strength-bar">
                            <div id="strength-bar-fill" class="strength-bar-fill"></div>
                        </div>
                        <p id="strength-text" class="strength-text"></p>
                    </div>
                </div>
                
                <div class="form-group">
                    <label class="form-label" for="confirmPassword">Confirm Password</label>
                    <div class="password-input-wrapper">
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-input" required 
                               placeholder="Confirm your password" autocomplete="new-password" minlength="6">
                        <button type="button" class="password-toggle" onclick="togglePasswordVisibility('confirmPassword')" aria-label="Toggle password visibility">
                            <svg id="confirmPassword-icon" class="eye-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                    </div>
                </div>
                
                <button type="submit" class="btn btn-primary">Create Account</button>
            </form>
            
            <div class="auth-footer">
                Already have an account? <a href="login">Sign in</a>
            </div>
        </div>
    </div>
    
    <script src="js/theme.js"></script>
    <script src="js/password.js"></script>
</body>
</html>
