function togglePasswordVisibility(fieldId) {
    const passwordField = document.getElementById(fieldId);
    const icon = document.getElementById(fieldId + '-icon');
    
    if (passwordField.type === 'password') {
        passwordField.type = 'text';
        icon.innerHTML = '<path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line>';
    } else {
        passwordField.type = 'password';
        icon.innerHTML = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle>';
    }
}

function checkPasswordStrength() {
    const password = document.getElementById('password').value;
    const strengthContainer = document.getElementById('password-strength');
    const strengthBar = document.getElementById('strength-bar-fill');
    const strengthText = document.getElementById('strength-text');
    
    if (password.length === 0) {
        strengthContainer.style.display = 'none';
        return;
    }
    
    strengthContainer.style.display = 'block';
    
    let strength = 0;
    let feedback = [];
    
    // Length check
    if (password.length >= 8) strength += 25;
    else if (password.length >= 6) strength += 15;
    
    // Contains lowercase
    if (/[a-z]/.test(password)) strength += 15;
    
    // Contains uppercase
    if (/[A-Z]/.test(password)) {
        strength += 20;
        feedback.push('uppercase');
    }
    
    // Contains numbers
    if (/\d/.test(password)) {
        strength += 20;
        feedback.push('numbers');
    }
    
    // Contains special characters
    if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
        strength += 20;
        feedback.push('special chars');
    }
    
    // Update strength bar
    strengthBar.style.width = strength + '%';
    
    // Update color and text based on strength
    if (strength < 40) {
        strengthBar.className = 'strength-bar-fill weak';
        strengthText.textContent = 'Weak password';
        strengthText.style.color = '#ef4444';
    } else if (strength < 70) {
        strengthBar.className = 'strength-bar-fill medium';
        strengthText.textContent = 'Medium strength';
        strengthText.style.color = '#f59e0b';
    } else {
        strengthBar.className = 'strength-bar-fill strong';
        strengthText.textContent = 'Strong password';
        strengthText.style.color = '#10b981';
    }
}
