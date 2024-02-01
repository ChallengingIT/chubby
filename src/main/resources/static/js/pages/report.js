$('.list-unstyled li').removeClass('active');
$('.dipendenti').addClass('active');

$(document).ready(function() {
    setTimeout(function() { $("#success-alert").alert('close'); }, 3000);
});

function Reset() {
    document.getElementById("anno").value = "";
    document.getElementById("mese").value = "";
    document.getElementById("inizio").value = "";
    document.getElementById("fine").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}