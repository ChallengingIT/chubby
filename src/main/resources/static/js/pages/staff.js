function alertSt(message) {
    alertify.confirm("Note", message, null, null);
}

$('.list-unstyled li').removeClass('active');
$('.dipendenti').addClass('active');

$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});

function Reset() {
    document.getElementById("nome").value = "";
    document.getElementById("cognome").value = "";
    document.getElementById("email").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}