$('.list-unstyled li').removeClass('active');
$('.need').addClass('active');

function Reset() {

    if (document.getElementById("ricercaAzienda") != null ) {
       document.getElementById("ricercaAzienda").value = "";
    }
    document.getElementById("ricercaTipologia").value = "";
    document.getElementById("ricercaPriorita").value = "";
    document.getElementById("ricercaStato").value = "";
    document.getElementById("ricercaOwner").value = "";
    document.getElementById("ricercaWeek").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}