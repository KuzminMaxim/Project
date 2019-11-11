function checkFields() {
    var fName = document.getElementById("name").value;
    var fPassword = document.getElementById("pass").value;
    if (fName == 0){
        alert("Enter name!");
        return false;
    }
    if (fPassword == 0){
        alert("Enter password!");
        return false;
    }
    else alert('Name: '+fName+" ,Password: "+fPassword);
    return true;
}