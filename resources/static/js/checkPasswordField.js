function checkPassword() {
    var fPassword = document.getElementById("pass").value;

    if (fPassword === 0){
        alert("Enter password!");
        return false;
    }
    if (fPassword !== 0){
        alert("Password was changed! " +
            "New password: "+fPassword);
        return true;
    }
}