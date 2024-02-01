$('.list-unstyled li').removeClass('active');
$('.staffing').addClass('active');

function Reset() {
    document.getElementById("ricercaNome").value = "";
    document.getElementById("ricercaCognome").value = "";
    document.getElementById("ricercaEmail").value = "";
    document.getElementById("ricercaTipologia").value = "";
    document.getElementById("ricercaTipo").value = "";
    document.getElementById("ricercaStato").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}