$('.list-unstyled li').removeClass('active');
$('.hr').addClass('active');


function Reset() {
    document.getElementById("nome").value = "";
    document.getElementById("cognome").value = "";
    document.getElementById("email").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}