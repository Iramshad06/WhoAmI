<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-card fade-in">
            <div class="auth-header">
                <h1>Create New Password</h1>
                <p>Choose a strong password for your account</p>
            </div>
            
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            
            <form method="post" action="reset-password">
                <div class="form-group">
                    <label class="form-label" for="newPassword">New Password</label>
                    <div class="password-input-wrapper">
                        <input type="password" id="newPassword" name="newPassword" class="form-input" required 
                               placeholder="At least 6 characters" minlength="6" autocomplete="new-password">
                        <button type="button" class="password-toggle" onclick="togglePasswordVisibility('newPassword')" aria-label="Toggle password visibility">
                            <svg id="newPassword-icon" class="eye-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                    </div>
                </div>
                
                <div class="form-group">
                    <label class="form-label" for="confirmPassword">Confirm Password</label>
                    <div class="password-input-wrapper">
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-input" required 
                               placeholder="Re-enter your password" minlength="6" autocomplete="new-password">
                        <button type="button" class="password-toggle" onclick="togglePasswordVisibility('confirmPassword')" aria-label="Toggle password visibility">
                            <svg id="confirmPassword-icon" class="eye-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                    </div>
                </div>
                
                <button type="submit" class="btn btn-primary">Reset Password</button>
            </form>
        </div>
    </div>
    
    <script src="js/theme.js"></script>
    <script src="js/password.js"></script>
</body>
</html>
