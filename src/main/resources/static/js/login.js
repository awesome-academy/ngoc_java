// Toggle password visibility
function togglePassword() {
    const $passwordInput = $('#password');
    const $toggleBtn = $('.toggle-password');

    if ($passwordInput.attr('type') === 'password') {
        $passwordInput.attr('type', 'text');
        $toggleBtn.text('🙈');
    } else {
        $passwordInput.attr('type', 'password');
        $toggleBtn.text('👁️');
    }
}

// Form validation
function validateLoginForm() {
    const email = $('#email').val();
    const password = $('#password').val();
    let isValid = true;

    // Clear previous errors
    $('#emailError').text('');
    $('#passwordError').text('');

    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!email) {
        $('#emailError').text('Email is required');
        isValid = false;
    } else if (!emailRegex.test(email)) {
        $('#emailError').text('Please enter a valid email');
        isValid = false;
    }

    // Password validation
    if (!password) {
        $('#passwordError').text('Password is required');
        isValid = false;
    } else if (password.length < 6) {
        $('#passwordError').text('Password must be at least 6 characters');
        isValid = false;
    }

    return isValid;
}

// Social login handlers
function handleSocialLogin(provider) {
    alert(`Login with ${provider} will be implemented soon!`);
}

// Forgot password handler
function handleForgotPassword(e) {
    e.preventDefault();
    alert('Forgot Password page will be implemented soon!');
}

// Initialize login page
$(document).ready(function() {

    // Form submit handler
    $('.login-form').on('submit', function(e) {
        if (!validateLoginForm()) {
            e.preventDefault();
        }
    });

    // Social login buttons
    $('.social-btn').on('click', function(e) {
        e.preventDefault();
        const provider = $(this).attr('class').match(/(google|facebook|twitter)/)[1];
        const capitalizedProvider = provider.charAt(0).toUpperCase() + provider.slice(1);
        handleSocialLogin(capitalizedProvider);
    });

    // Forgot password link
    $('.forgot-password').on('click', handleForgotPassword);
});

