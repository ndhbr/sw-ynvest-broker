function toggleBankPassword() {
    const pw = document.getElementById("bank-password");
    const o = document.getElementById("bank-password-display");
    const eyeOffIcon = document.getElementById("bank-password-icon-eye-off");
    const eyeIcon = document.getElementById("bank-password-icon-eye");

    if (o != null && o.innerHTML === "●●●●●●●●●●") {
        o.innerText = pw.value;
        eyeOffIcon.style.display = 'none';
        eyeIcon.style.display = 'inline';
    } else {
        o.innerText = "●●●●●●●●●●";
        eyeOffIcon.style.display = 'inline';
        eyeIcon.style.display = 'none';
    }
}