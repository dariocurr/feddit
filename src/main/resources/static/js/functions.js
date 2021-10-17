function showChangePasswordForm() {
  document.getElementById("changePasswordForm").classList.toggle("hidden");
}

function checkPassword() {
    password = document.getElementById('password');
    confirmPassword = document.getElementById('confirm_password');

    if (password.value != "" && confirmPassword.value != "" && password.value == confirmPassword.value) {
        confirmPassword.style.borderColor = "green"
        password.style.borderColor = "black"
        return true
    } else {
        confirmPassword.style.borderColor = "black"
        return false
    }
 }

 function checkErrorPassword(){
    if (!checkPassword()) {
        document.getElementById('errorPassword').innerHTML = "Passwords doesn't match.";
        document.getElementById('errorPassword').style.color = "red"
        document.getElementById('password').style.borderColor = "red"
        document.getElementById('confirm_password').style.borderColor = "red"
    }
 }