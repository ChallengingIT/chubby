$('.list-unstyled li').removeClass('active');
$('.need').addClass('active');

function Reset() {
    document.getElementById("nome").value = "";
    document.getElementById("cognome").value = "";
    document.getElementById("tipologia").value = "";
    document.getElementById("job").value = "";
    document.getElementById("seniority").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}