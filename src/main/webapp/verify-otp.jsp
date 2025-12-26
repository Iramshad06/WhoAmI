<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify Code - WhoAmI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="auth-container">
        <div class="auth-card fade-in">
            <div class="auth-header">
                <h1>Verify Your Code</h1>
                <p>Enter the 6-digit code sent to your email</p>
            </div>
            
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-error">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            
            <form method="post" action="verify-otp">
                <div class="form-group">
                    <label class="form-label" for="otp">Verification Code</label>
                    <input type="text" id="otp" name="otp" class="form-input otp-input" required 
                           placeholder="000000" maxlength="6" pattern="\d{6}" 
                           autocomplete="off" autofocus>
                    <small class="form-hint">Code expires in 10 minutes</small>
                </div>
                
                <button type="submit" class="btn btn-primary">Verify Code</button>
            </form>
            
            <div class="auth-footer">
                <a href="forgot-password">Request a new code</a>
            </div>
        </div>
    </div>
    
    <script src="js/theme.js"></script>
    <script>
        document.getElementById('otp').addEventListener('input', function(e) {
            this.value = this.value.replace(/\D/g, '');
        });
    </script>
</body>
</html>
